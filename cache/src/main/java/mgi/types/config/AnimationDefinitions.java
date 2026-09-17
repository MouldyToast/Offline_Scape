package mgi.types.config;

import org.jesse.CacheManager;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mgi.Indice;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;
import mgi.utilities.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class AnimationDefinitions implements Definitions, Cloneable {

    private static final Logger log = LoggerFactory.getLogger(AnimationDefinitions.class);
    public static AnimationDefinitions[] definitions;
    /**
     * The id of the animation.
     */
    private int id;
    private int precedenceAnimating;
    /**
     * An array of frame ids. The value is a bitpacked number, with bits past 16 being the skeleton id.
     */
    private int[] frameIds;
    private int[] mergedBoneGroups;
    /**
     * Animation priority level.
     */
    private int priority;
    private int frameStep;
    /**
     * The length of each frame, with one value being equal to one actual frame, capping at 20 milliseconds (1 second / 50 FPS)
     */
    private int[] frameLengths;
    private boolean stretches;
    private int[] extraFrameIds;
    /**
     * The id of the item held in the left hand. If the id is 0, the left hand item is not displayed by the client.
     */
    private int leftHandItem;
    private int forcedPriority;
    /**
     * The id of the item held in the right hand. If the id is 0, the held right hand item is not displayed by the client.
     */
    private int rightHandItem;
    /**
     * The maximum number of times the animation can replay itself.
     */
    private int iterations;
    private int replyMode;
    /**
     * An array of sound effects per each frame. (Used in the legacy formats.)
     */
    private Map<Integer, Sound> soundEffects;
    private boolean[] animMayaMasks;
    private int animMayaStart;
    private int animMayaEnd;
    /**
     * In the CURRENT format this maps frames to lists of sounds.
     * In legacy formats, it maps frame ids to a single sound.
     */
    private Map<Integer, ?> animMayaFrameSounds;
    private int animMayaID = -1;

    /**
     * Instead of a boolean flag, we now choose one of three formats.
     */
    private AnimFormat animFormat = AnimFormat.CURRENT;
    private static int size;

    public static AnimationDefinitions get(final int id) {
        if (id < 0 || id >= size) {
            return null;
        }
        return definitions[id];
    }

    /**
     * Gets a list of all the animations that share the skeleton of the animation in arguments.
     *
     * @param animationId the animation to compare
     * @return a list of animations.
     */
    public static List<Integer> getSkeletonAnimations(final int animationId) {
        final AnimationDefinitions d = AnimationDefinitions.get(animationId);
        if (d == null) {
            throw new IllegalStateException("Animation is null.");
        }
        if (d.frameIds == null) {
            throw new IllegalStateException("Animation images are null - unable to compare.");
        }
        final int frameId = d.frameIds[0] >> 16;
        final List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.ANIMATION_DEFINITIONS); i++) {
            final AnimationDefinitions defs = AnimationDefinitions.get(i);
            if (defs == null) {
                continue;
            }
            if (defs.frameIds == null) {
                continue;
            }
            if (defs.frameIds[0] >> 16 == frameId) {
                ids.add(i);
            }
        }
        return ids;
    }

    public static IntArrayList getAnimationIdsByFrameId(final int frameId, final IntOpenHashSet linkedAnimations) {
        final IntArrayList list = new IntArrayList();
        for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.ANIMATION_DEFINITIONS); i++) {
            if (linkedAnimations != null && linkedAnimations.contains(i)) {
                continue;
            }
            final AnimationDefinitions definitions = AnimationDefinitions.get(i);
            if (definitions == null) {
                continue;
            }
            if (definitions.getFrameIds() != null) {
                if (ArrayUtils.contains(definitions.getFrameIds(), frameId)) {
                    if (!list.contains(i)) {
                        list.add(i);
                    }
                }
            }
            if (definitions.getExtraFrameIds() != null) {
                if (ArrayUtils.contains(definitions.getExtraFrameIds(), frameId)) {
                    if (!list.contains(i)) {
                        list.add(i);
                    }
                }
            }
        }
        return list;
    }

    public static int getSkeletonId(final int animationId) {
        final AnimationDefinitions definitions = get(animationId);
        if (definitions == null) {
            return -1;
        }
        final int[] frames = definitions.frameIds;
        if (frames == null || frames.length == 0) {
            return -1;
        }
        return frames[0] >> 16;
    }

    /**
     * The enum that determines which decoding/encoding scheme to use.
     */
    public static enum AnimFormat {
        CURRENT(false, false),
        PRE_OPCODE_CHANGE(false, true),
        OLDEST(true, true);

        private final boolean oldFormat;
        private final boolean oldest;

        AnimFormat(boolean oldFormat, boolean oldest) {
            this.oldFormat = oldFormat;
            this.oldest = oldest;
        }

        public boolean isOldFormat() {
            return oldFormat;
        }

        public boolean isOldest() {
            return oldest;
        }
    }

    // ––– Constructors –––

    public AnimationDefinitions(final int id, final ByteBuffer buffer) {
        this(id, buffer, AnimFormat.CURRENT);
    }

    public AnimationDefinitions(final int id, final ByteBuffer buffer, AnimFormat format) {
        this.id = id;
        this.animFormat = format;
        setDefaults();
        decode(buffer);
    }

    private void setDefaults() {
        frameStep = -1;
        stretches = false;
        forcedPriority = 5;
        leftHandItem = -1;
        rightHandItem = -1;
        iterations = 99;
        precedenceAnimating = -1;
        priority = -1;
        replyMode = 2;
    }

    public AnimationDefinitions() {
    }

    @Override
    public AnimationDefinitions clone() throws CloneNotSupportedException {
        return (AnimationDefinitions) super.clone();
    }

    /**
     * For converting a sequence into the pre_opcode format:
     * pre_opcode corresponds to (oldFormat = false, oldest = true).
     */
    public static AnimationDefinitions decodeNew(int id, ByteBuffer buffer) {
        log.info("Converting sequence into pre_opcode format: {}", id);
        return new AnimationDefinitions(id, buffer, AnimFormat.CURRENT);
    }

    // ––– Decoding –––

    @Override
    public void load() {
        try {
            final Cache cache = CacheManager.getCache();
            final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
            final Group animations = configs.findGroupByID(GroupType.SEQUENCE);
            size = animations.getHighestFileId();
            definitions = new AnimationDefinitions[animations.getHighestFileId()];
            for (int id = 0; id < animations.getHighestFileId(); id++) {
                final File file = animations.findFileByID(id);
                if (file == null) {
                    continue;
                }
                final ByteBuffer buffer = file.getData();
                if (buffer == null) {
                    continue;
                }
                definitions[id] = new AnimationDefinitions(id, buffer);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void decode(final ByteBuffer buffer) {
        while (true) {
            final int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                break;
            }
            decode(buffer, opcode);
        }
    }

    @Override
    public void decode(ByteBuffer buffer, int opcode) {
        switch (animFormat) {
            case CURRENT:
                decodeCurrent(buffer, opcode);
                break;
            case PRE_OPCODE_CHANGE:
                decodePreOpcode(buffer, opcode);
                break;
            case OLDEST:
                decodeOldest(buffer, opcode);
                break;
        }
    }

    private void decodeCurrent(ByteBuffer buffer, final int opcode) {
        switch (opcode) {
            case 1: {
                final int count = buffer.readUnsignedShort();
                frameLengths = new int[count];
                for (int index = 0; index < count; index++) {
                    frameLengths[index] = buffer.readUnsignedShort();
                }
                frameIds = new int[count];
                for (int index = 0; index < count; index++) {
                    frameIds[index] = buffer.readUnsignedShort();
                }
                for (int index = 0; index < count; index++) {
                    frameIds[index] += (buffer.readUnsignedShort()) << 16;
                }
                return;
            }
            case 2:
                frameStep = buffer.readUnsignedShort();
                return;
            case 3: {
                final int count = buffer.readUnsignedByte();
                mergedBoneGroups = new int[count + 1];
                for (int index = 0; index < count; index++) {
                    mergedBoneGroups[index] = buffer.readUnsignedByte();
                }
                mergedBoneGroups[count] = 9999999;
                return;
            }
            case 4:
                stretches = true;
                return;
            case 5:
                forcedPriority = buffer.readUnsignedByte();
                return;
            case 6:
                leftHandItem = buffer.readUnsignedShort();
                if (leftHandItem > 0) {
                    leftHandItem -= 512;
                }
                return;
            case 7:
                rightHandItem = buffer.readUnsignedShort();
                if (rightHandItem > 0) {
                    rightHandItem -= 512;
                }
                return;
            case 8:
                iterations = buffer.readUnsignedByte();
                return;
            case 9:
                precedenceAnimating = buffer.readUnsignedByte();
                return;
            case 10:
                priority = buffer.readUnsignedByte();
                return;
            case 11:
                replyMode = buffer.readUnsignedByte();
                return;
            case 12: {
                final int count = buffer.readUnsignedByte();
                extraFrameIds = new int[count];
                for (int index = 0; index < count; index++) {
                    extraFrameIds[index] = buffer.readUnsignedShort();
                }
                for (int index = 0; index < count; index++) {
                    extraFrameIds[index] += (buffer.readUnsignedShort()) << 16;
                }
                return;
            }
            case 13:
                animMayaID = buffer.readInt();
                return;
            case 14: {
                final int count = buffer.readUnsignedShort();
                // In the current format we expect multiple sounds per frame.
                Map<Integer, List<Sound>> frameSounds = new HashMap<>();
                for (int i = 0; i < count; i++) {
                    final int frame = buffer.readUnsignedShort();
                    frameSounds.computeIfAbsent(frame, k -> new ArrayList<>()).add(readSound(buffer));
                }
                animMayaFrameSounds = frameSounds;
                break;
            }
            case 15:
                animMayaStart = buffer.readUnsignedShort();
                animMayaEnd = buffer.readUnsignedShort();
                return;
            case 16:
                buffer.readByte();
                return;
            case 17: {
                animMayaMasks = new boolean[256];
                final int count = buffer.readUnsignedByte();
                for (int i = 0; i < count; i++) {
                    animMayaMasks[buffer.readUnsignedByte()] = true;
                }
                break;
            }
            case 18:
                buffer.readString();
                return;
            case 19:
                return;
            default:
                break;
        }
    }

    /**
     * Decoding using the original opcodes but with oldFormat = false (and oldest = true).
     */
    private void decodePreOpcode(ByteBuffer buffer, final int opcode) {
        switch (opcode) {
            case 1: {
                final int count = buffer.readUnsignedShort();
                frameLengths = new int[count];
                for (int index = 0; index < count; index++) {
                    frameLengths[index] = buffer.readUnsignedShort();
                }
                frameIds = new int[count];
                for (int index = 0; index < count; index++) {
                    frameIds[index] = buffer.readUnsignedShort();
                }
                for (int index = 0; index < count; index++) {
                    frameIds[index] += (buffer.readUnsignedShort()) << 16;
                }
                break;
            }
            case 2:
                frameStep = buffer.readUnsignedShort();
                break;
            case 3: {
                final int count = buffer.readUnsignedByte();
                mergedBoneGroups = new int[count + 1];
                for (int index = 0; index < count; index++) {
                    mergedBoneGroups[index] = buffer.readUnsignedByte();
                }
                mergedBoneGroups[count] = 9999999;
                break;
            }
            case 4:
                stretches = true;
                break;
            case 5:
                forcedPriority = buffer.readUnsignedByte();
                break;
            case 6:
                leftHandItem = buffer.readUnsignedShort();
                if (leftHandItem > 0) {
                    leftHandItem -= 512;
                }
                break;
            case 7:
                rightHandItem = buffer.readUnsignedShort();
                if (rightHandItem > 0) {
                    rightHandItem -= 512;
                }
                break;
            case 8:
                iterations = buffer.readUnsignedByte();
                break;
            case 9:
                precedenceAnimating = buffer.readUnsignedByte();
                break;
            case 10:
                priority = buffer.readUnsignedByte();
                break;
            case 11:
                replyMode = buffer.readUnsignedByte();
                break;
            case 12: {
                final int count = buffer.readUnsignedByte();
                extraFrameIds = new int[count];
                for (int index = 0; index < count; index++) {
                    extraFrameIds[index] = buffer.readUnsignedShort();
                }
                for (int index = 0; index < count; index++) {
                    extraFrameIds[index] += (buffer.readUnsignedShort()) << 16;
                }
                break;
            }
            case 13: {
                final int count = buffer.readUnsignedByte();
                soundEffects = new HashMap<>();
                for (int index = 0; index < count; index++) {
                    // For PRE_OPCODE_CHANGE, use the non-old branch:
                    soundEffects.put(index, readFrameSound(buffer));
                }
                break;
            }
            case 14:
                animMayaID = buffer.readInt();
                break;
            case 15: {
                final int count = buffer.readUnsignedShort();
                Map<Integer, Sound> mayaSounds = new HashMap<>();
                for (int i = 0; i < count; i++) {
                    int id = buffer.readUnsignedShort();
                    mayaSounds.put(id, readFrameSound(buffer));
                }
                animMayaFrameSounds = mayaSounds;
                break;
            }
            case 16:
                animMayaStart = buffer.readUnsignedShort();
                animMayaEnd = buffer.readUnsignedShort();
                break;
            case 17: {
                animMayaMasks = new boolean[256];
                final int count = buffer.readUnsignedByte();
                for (int i = 0; i < count; i++) {
                    animMayaMasks[buffer.readUnsignedByte()] = true;
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * Decoding using the original opcodes with oldFormat = true (and oldest = true).
     */
    private void decodeOldest(ByteBuffer buffer, final int opcode) {
        switch (opcode) {
            case 1: {
                final int count = buffer.readUnsignedShort();
                frameLengths = new int[count];
                for (int index = 0; index < count; index++) {
                    frameLengths[index] = buffer.readUnsignedShort();
                }
                frameIds = new int[count];
                for (int index = 0; index < count; index++) {
                    frameIds[index] = buffer.readUnsignedShort();
                }
                for (int index = 0; index < count; index++) {
                    frameIds[index] += (buffer.readUnsignedShort()) << 16;
                }
                break;
            }
            case 2:
                frameStep = buffer.readUnsignedShort();
                break;
            case 3: {
                final int count = buffer.readUnsignedByte();
                mergedBoneGroups = new int[count + 1];
                for (int index = 0; index < count; index++) {
                    mergedBoneGroups[index] = buffer.readUnsignedByte();
                }
                mergedBoneGroups[count] = 9999999;
                break;
            }
            case 4:
                stretches = true;
                break;
            case 5:
                forcedPriority = buffer.readUnsignedByte();
                break;
            case 6:
                leftHandItem = buffer.readUnsignedShort();
                if (leftHandItem > 0) {
                    leftHandItem -= 512;
                }
                break;
            case 7:
                rightHandItem = buffer.readUnsignedShort();
                if (rightHandItem > 0) {
                    rightHandItem -= 512;
                }
                break;
            case 8:
                iterations = buffer.readUnsignedByte();
                break;
            case 9:
                precedenceAnimating = buffer.readUnsignedByte();
                break;
            case 10:
                priority = buffer.readUnsignedByte();
                break;
            case 11:
                replyMode = buffer.readUnsignedByte();
                break;
            case 12: {
                final int count = buffer.readUnsignedByte();
                extraFrameIds = new int[count];
                for (int index = 0; index < count; index++) {
                    extraFrameIds[index] = buffer.readUnsignedShort();
                }
                for (int index = 0; index < count; index++) {
                    extraFrameIds[index] += (buffer.readUnsignedShort()) << 16;
                }
                break;
            }
            case 13: {
                final int count = buffer.readUnsignedByte();
                soundEffects = new HashMap<>();
                for (int index = 0; index < count; index++) {
                    int effectId = buffer.readMedium();
                    final int soundId = effectId >> 8;
                    final int radius = effectId & 31;
                    final int volume = (effectId >> 4) & 7;
                    Sound sound = new Sound(soundId, radius, volume, 0);
                    sound.effect = effectId;
                    soundEffects.put(index, sound);
                }
                break;
            }
            case 14:
                animMayaID = buffer.readInt();
                break;
            case 15: {
                final int count = buffer.readUnsignedShort();
                Map<Integer, Sound> mayaSounds = new HashMap<>();
                for (int i = 0; i < count; i++) {
                    int id = buffer.readUnsignedShort();
                    int effect = buffer.readMedium();
                    final int soundId = effect >> 8;
                    final int radius = effect & 31;
                    final int volume = (effect >> 4) & 7;
                    Sound sound = new Sound(soundId, radius, volume, 0);
                    sound.effect = effect;
                    mayaSounds.put(id, sound);
                }
                animMayaFrameSounds = mayaSounds;
                break;
            }
            case 16:
                animMayaStart = buffer.readUnsignedShort();
                animMayaEnd = buffer.readUnsignedShort();
                break;
            case 17: {
                animMayaMasks = new boolean[256];
                final int count = buffer.readUnsignedByte();
                for (int i = 0; i < count; i++) {
                    animMayaMasks[buffer.readUnsignedByte()] = true;
                }
                break;
            }
            default:
                break;
        }
    }

    // ––– Encoding –––

    @Override
    public ByteBuffer encode() {
        switch (animFormat) {
            case CURRENT:
                return encodeCurrent();
            case PRE_OPCODE_CHANGE:
                return encodePreOpcode();
            case OLDEST:
                return encodeOldest();
            default:
                return encodeCurrent();
        }
    }

    /**
     * Encoding for the current format.
     */
    private ByteBuffer encodeCurrent() {
        final ByteBuffer buffer = new ByteBuffer(1024 * 10 * 10);
        if (frameIds != null) {
            buffer.writeByte(1);
            buffer.writeShort(frameLengths.length);
            for (final int frameLength : frameLengths) {
                buffer.writeShort(frameLength);
            }
            for (final int frameId : frameIds) {
                buffer.writeShort(frameId & 0xFFFF);
            }
            for (final int frameId : frameIds) {
                buffer.writeShort(frameId >> 16);
            }
        }
        if (frameStep != -1) {
            buffer.writeByte(2);
            buffer.writeShort(frameStep);
        }
        if (mergedBoneGroups != null) {
            buffer.writeByte(3);
            buffer.writeByte(mergedBoneGroups.length - 1);
            for (int i = 0, len = mergedBoneGroups.length - 1; i < len; i++) {
                buffer.writeByte(mergedBoneGroups[i]);
            }
        }
        if (stretches) {
            buffer.writeByte(4);
        }
        if (forcedPriority != 5) {
            buffer.writeByte(5);
            buffer.writeByte(forcedPriority);
        }
        if (leftHandItem != -1) {
            buffer.writeByte(6);
            buffer.writeShort(leftHandItem == 65535 ? 0 : leftHandItem);
        }
        if (rightHandItem != -1) {
            buffer.writeByte(7);
            buffer.writeShort(rightHandItem == 65535 ? 0 : rightHandItem);
        }
        if (iterations != 99) {
            buffer.writeByte(8);
            buffer.writeByte(iterations);
        }
        if (precedenceAnimating != -1) {
            buffer.writeByte(9);
            buffer.writeByte(precedenceAnimating);
        }
        if (priority != -1) {
            buffer.writeByte(10);
            buffer.writeByte(priority);
        }
        if (replyMode != 2) {
            buffer.writeByte(11);
            buffer.writeByte(replyMode);
        }
        if (extraFrameIds != null) {
            buffer.writeByte(12);
            buffer.writeByte(extraFrameIds.length);
            for (final int frameId : extraFrameIds) {
                buffer.writeShort(frameId & 0xFFFF);
            }
            for (final int frameId : extraFrameIds) {
                buffer.writeShort(frameId >> 16);
            }
        }
        if (animMayaID != -1) {
            buffer.writeByte(13);
            buffer.writeInt(animMayaID);
        }
        if (animMayaFrameSounds != null) {
            buffer.writeByte(14);
            if (animMayaFrameSounds instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Integer, List<Sound>> frameSounds = (Map<Integer, List<Sound>>) animMayaFrameSounds;
                buffer.writeShort(frameSounds.size());
                for (Map.Entry<Integer, List<Sound>> entry : frameSounds.entrySet()) {
                    int frame = entry.getKey();
                    for (Sound sound : entry.getValue()) {
                        buffer.writeShort(frame);
                        writeSound(buffer, sound);
                    }
                }
            }
        }
        if (animMayaStart != 0 || animMayaEnd != 0) {
            buffer.writeByte(15);
            buffer.writeShort(animMayaStart);
            buffer.writeShort(animMayaEnd);
        }
        if (animMayaMasks != null) {
            buffer.writeByte(17);
            int count = 0;
            for (boolean mask : animMayaMasks) {
                if (mask) {
                    count++;
                }
            }
            buffer.writeByte(count);
            for (int i = 0; i < animMayaMasks.length; i++) {
                if (animMayaMasks[i]) {
                    buffer.writeByte(i);
                }
            }
        }
        buffer.writeByte(0);
        return buffer;
    }

    /**
     * Encoding for the legacy pre_opcode format (oldFormat = false, oldest = true).
     */
    private ByteBuffer encodePreOpcode() {
        final ByteBuffer buffer = new ByteBuffer(1024 * 10 * 10);
        if (frameIds != null) {
            buffer.writeByte(1);
            buffer.writeShort(frameLengths.length);
            for (final int frameLength : frameLengths) {
                buffer.writeShort(frameLength);
            }
            for (final int frameId : frameIds) {
                buffer.writeShort(frameId);
            }
            for (final int frameId : frameIds) {
                buffer.writeShort(frameId >> 16);
            }
        }
        if (frameStep != -1) {
            buffer.writeByte(2);
            buffer.writeShort(frameStep);
        }
        if (mergedBoneGroups != null) {
            buffer.writeByte(3);
            buffer.writeByte(mergedBoneGroups.length - 1);
            for (int i = 0, len = mergedBoneGroups.length - 1; i < len; i++) {
                buffer.writeByte(mergedBoneGroups[i]);
            }
        }
        if (stretches) {
            buffer.writeByte(4);
        }
        if (forcedPriority != 5) {
            buffer.writeByte(5);
            buffer.writeByte(forcedPriority);
        }
        if (leftHandItem != -1) {
            buffer.writeByte(6);
            // In the legacy format, left hand items are offset differently.
            buffer.writeShort(leftHandItem == 0 ? 0 : leftHandItem + 512);
        }
        if (rightHandItem != -1) {
            buffer.writeByte(7);
            buffer.writeShort(rightHandItem == 0 ? 0 : rightHandItem + 512);
        }
        if (iterations != 99) {
            buffer.writeByte(8);
            buffer.writeByte(iterations);
        }
        if (precedenceAnimating != -1) {
            buffer.writeByte(9);
            buffer.writeByte(precedenceAnimating);
        }
        if (priority != -1) {
            buffer.writeByte(10);
            buffer.writeByte(priority);
        }
        if (replyMode != 2) {
            buffer.writeByte(11);
            buffer.writeByte(replyMode);
        }
        if (extraFrameIds != null) {
            buffer.writeByte(12);
            buffer.writeByte(extraFrameIds.length);
            for (final int frameId : extraFrameIds) {
                buffer.writeShort(frameId & 0xFFFF);
            }
            for (final int frameId : extraFrameIds) {
                buffer.writeShort(frameId >> 16);
            }
        }
        if (soundEffects != null) {
            buffer.writeByte(13);
            buffer.writeByte(soundEffects.size());
            // For each sound effect, write using the legacy (non-old branch) method.
            for (int i = 0; i < soundEffects.size(); i++) {
                Sound sound = soundEffects.get(i);
                if (sound != null) {
                    buffer.writeMedium(sound.effect);
                }
            }
        }
        if (animMayaID != -1) {
            buffer.writeByte(14);
            buffer.writeInt(animMayaID);
        }
        if (animMayaFrameSounds != null) {
            buffer.writeByte(15);
            if (animMayaFrameSounds instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Integer, Sound> mayaSounds = (Map<Integer, Sound>) animMayaFrameSounds;
                buffer.writeShort(mayaSounds.size());
                for (Map.Entry<Integer, Sound> entry : mayaSounds.entrySet()) {
                    buffer.writeShort(entry.getKey());
                    Sound sound = entry.getValue();
                    if (sound != null) {
                        buffer.writeMedium(sound.effect);
                    }
                }
            }
        }
        if (animMayaStart != 0 || animMayaEnd != 0) {
            buffer.writeByte(16);
            buffer.writeShort(animMayaStart);
            buffer.writeShort(animMayaEnd);
        }
        if (animMayaMasks != null) {
            buffer.writeByte(17);
            int count = 0;
            for (boolean mask : animMayaMasks) {
                if (mask) {
                    count++;
                }
            }
            buffer.writeByte(count);
            for (int i = 0; i < animMayaMasks.length; i++) {
                if (animMayaMasks[i]) {
                    buffer.writeByte(i);
                }
            }
        }
        buffer.writeByte(0);
        return buffer;
    }

    /**
     * For OLDEST we use the same encoding as PRE_OPCODE_CHANGE.
     */
    private ByteBuffer encodeOldest() {
        return encodePreOpcode();
    }

    // ––– Sound Reading/Writing Helpers –––

    /**
     * Reads a sound using the CURRENT format.
     */
    private static Sound readSound(ByteBuffer buffer) {
        int id = buffer.readUnsignedShort();
        int weight = buffer.readUnsignedByte();
        int loops = buffer.readUnsignedByte();
        int location = buffer.readUnsignedByte();
        int retain = buffer.readUnsignedByte();
        return new Sound(id, weight, loops, location, retain);
    }

    /**
     * Reads a sound in legacy mode.
     */
    private Sound readFrameSound(ByteBuffer buffer) {
        int id = buffer.readUnsignedShort();
        int loops = buffer.readUnsignedByte();
        int location = buffer.readUnsignedByte();
        int retain = buffer.readUnsignedByte();
        return new Sound(id, loops, location, retain);
    }

    /**
     * Writes a sound in the CURRENT format.
     */
    private static void writeSound(ByteBuffer buffer, Sound sound) {
        buffer.writeShort(sound.id);
        buffer.writeByte(sound.weight);
        buffer.writeByte(sound.loops);
        buffer.writeByte(sound.location);
        buffer.writeByte(sound.retain);
    }

    @Override
    public void pack() {
        pack(id, encode());
    }

    public static void pack(int id, ByteBuffer bytes) {
        final Archive archive = CacheManager.getCache().getArchive(ArchiveType.CONFIGS);
        final Group animations = archive.findGroupByID(GroupType.SEQUENCE);
        animations.addFile(new File(id, bytes));
    }

    public IntArrayList getUniqueFrames() {
        final IntArrayList list = new IntArrayList();
        if (frameIds != null) {
            for (final int frame : frameIds) {
                if (!list.contains(frame)) {
                    list.add(frame);
                }
            }
        }
        if (extraFrameIds != null) {
            for (final int frame : extraFrameIds) {
                if (!list.contains(frame)) {
                    list.add(frame);
                }
            }
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrecedenceAnimating() {
        return precedenceAnimating;
    }

    public void setPrecedenceAnimating(int precedenceAnimating) {
        this.precedenceAnimating = precedenceAnimating;
    }

    public int[] getFrameIds() {
        return frameIds;
    }

    public void setFrameIds(int[] frameIds) {
        this.frameIds = frameIds;
    }

    public int[] getMergedBoneGroups() {
        return mergedBoneGroups;
    }

    public void setMergedBoneGroups(int[] mergedBoneGroups) {
        this.mergedBoneGroups = mergedBoneGroups;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getFrameStep() {
        return frameStep;
    }

    public void setFrameStep(int frameStep) {
        this.frameStep = frameStep;
    }

    public int[] getFrameLengths() {
        return frameLengths;
    }

    public void setFrameLengths(int[] frameLengths) {
        this.frameLengths = frameLengths;
    }

    public boolean isStretches() {
        return stretches;
    }

    public void setStretches(boolean stretches) {
        this.stretches = stretches;
    }

    public int[] getExtraFrameIds() {
        return extraFrameIds;
    }

    public void setExtraFrameIds(int[] extraFrameIds) {
        this.extraFrameIds = extraFrameIds;
    }

    public int getLeftHandItem() {
        return leftHandItem;
    }

    public void setLeftHandItem(int leftHandItem) {
        this.leftHandItem = leftHandItem;
    }

    public int getForcedPriority() {
        return forcedPriority;
    }

    public void setForcedPriority(int forcedPriority) {
        this.forcedPriority = forcedPriority;
    }

    public int getRightHandItem() {
        return rightHandItem;
    }

    public void setRightHandItem(int rightHandItem) {
        this.rightHandItem = rightHandItem;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getReplyMode() {
        return replyMode;
    }

    public void setReplyMode(int replyMode) {
        this.replyMode = replyMode;
    }

    public Map<Integer, Sound> getSoundEffects() {
        return soundEffects;
    }

    public void setSoundEffects(Map<Integer, Sound> soundEffects) {
        this.soundEffects = Objects.requireNonNullElseGet(soundEffects, HashMap::new);
    }

    @Override
    public String toString() {
        return "AnimationDefinitions(id=" + this.getId() + ", precedenceAnimating=" + this.getPrecedenceAnimating() + ", frameIds=" + Arrays.toString(this.getFrameIds()) + ", mergedBoneGroups=" + Arrays.toString(this.getMergedBoneGroups()) + ", priority=" + this.getPriority() + ", frameStep=" + this.getFrameStep() + ", frameLengths=" + Arrays.toString(this.getFrameLengths()) + ", stretches=" + this.isStretches() + ", extraFrameIds=" + Arrays.toString(this.getExtraFrameIds()) + ", leftHandItem=" + this.getLeftHandItem() + ", forcedPriority=" + this.getForcedPriority() + ", rightHandItem=" + this.getRightHandItem() + ", iterations=" + this.getIterations() + ", replyMode=" + this.getReplyMode() + ")";
    }

    public AnimationDefinitions copy(int id) {
        final AnimationDefinitions copy = new AnimationDefinitions();
        copy.id = id;
        copy.precedenceAnimating = precedenceAnimating;
        if (frameIds != null)
            copy.frameIds = Arrays.copyOf(frameIds, frameIds.length);
        if (mergedBoneGroups != null)
            copy.mergedBoneGroups = Arrays.copyOf(mergedBoneGroups, mergedBoneGroups.length);
        copy.priority = priority;
        copy.frameStep = frameStep;
        if (frameLengths != null)
            copy.frameLengths = Arrays.copyOf(frameLengths, frameLengths.length);
        copy.stretches = stretches;
        if (extraFrameIds != null)
            copy.extraFrameIds = Arrays.copyOf(extraFrameIds, extraFrameIds.length);
        copy.leftHandItem = leftHandItem;
        copy.forcedPriority = forcedPriority;
        copy.rightHandItem = rightHandItem;
        copy.iterations = iterations;
        copy.replyMode = replyMode;
        copy.soundEffects = soundEffects;
        return copy;
    }

    public int getDuration() {
        int duration = 0;
        if (frameLengths == null) {
            return 0;
        }
        for (final int i : frameLengths) {
            if (i > 30) {
                continue;
            }
            duration += i * 20;
        }
        return duration;
    }

    public int getClientTicks() {
        if (frameLengths == null) {
            return 0;
        }
        int ticks = 0;
        for (final int i : frameLengths) {
            if (i > 30) {
                continue;
            }
            ticks += i;
        }
        return ticks;
    }

// ––– Inner Sound Class –––

    public static final class Sound extends ArrayList<Sound> {
        public int id;
        public int weight;
        public int loops;
        public int location;
        public int retain;
        /**
         * Legacy field used in older formats.
         */
        public int effect = -1;

        public Sound(int id, int weight, int loops, int location, int retain) {
            this.id = id;
            this.weight = weight;
            this.loops = loops;
            this.location = location;
            this.retain = retain;
        }

        // Overloaded constructor for legacy reading (defaults weight to 0)
        public Sound(int id, int loops, int location, int retain) {
            this(id, 0, loops, location, retain);
        }

        public int getId() {
            return id;
        }

        public int getWeight() {
            return weight;
        }

        public int getLoops() {
            return loops;
        }

        public int getLocation() {
            return location;
        }

        public int getRetain() {
            return retain;
        }
    }
}
