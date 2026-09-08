package org.jesse.game.world.entity.player;

import org.jesse.game.GameInterface;
import org.jesse.game.content.treasuretrails.TreasureTrail;
import org.jesse.game.model.music.Music;
import org.jesse.game.model.music.MusicLoader;
import org.jesse.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.region.area.plugins.MusicPlugin;
import org.jesse.logger.NearRealityLogger;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.types.config.DBRowDefinition;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
import mgi.types.config.enums.StringEnum;
import net.runelite.cache.util.ScriptVarType;
import org.slf4j.Logger;

import java.util.*;

import static mgi.types.config.DBRowDefinition.tableRows;

/**
 * @author Kris | 21. veebr 2018 : 4:27.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class MusicHandler {

	private static final Logger logger = NearRealityLogger.getLogger(MusicHandler.class);

    public static final int[] VARP_IDS = new int[]{20, 21, 22, 23, 24, 25, 298, 311, 346, 414, 464, 598, 662, 721, 906,
            1009, 1338, 1681, 2065, 2237, 2950, 3418, 3575};

    public static StringEnum MUSIC_SLOT_NAME_ENUM = null;
    public static IntEnum MUSIC_SLOT_INTERFACE_ENUM = null;
    public static StringEnum MUSIC_SLOT_UNLOCK_ENUM = null;

    /**
     * The given player who owns this handler.
     */
    private final transient Player player;
    /**
     * An int2int map containing the varps and their values.
     */
    private final Int2IntOpenHashMap unlockedTracks;
    /**
     * The music track that's currently playing.
     */
    private transient Music currentlyPlaying;
    /**
     * The amount of ticks the current song has played for, and the amount of ticks at which this song ends.
     */
    private transient int ticks;
    private transient int nextSongAtTicks;
    /**
     * Whether the music player is currently stopped or not.
     */
    private transient boolean stopped;
    private transient boolean forcePlayNext;

    MusicHandler(final Player player) {
        this.player = player;
        unlockedTracks = new Int2IntOpenHashMap(VARP_IDS.length);
    }

    /**
     * Refreshes the varps to portray on the music tab interface.
     */
    void refreshListConfigs() {
        final VarManager manager = player.getVarManager();
        final boolean reset = isDefaultsReset();
        if (!reset) {
            setDefaultsReset();
        }
        for (int i = VARP_IDS.length - 1; i >= 0; i--) {
            final int varp = VARP_IDS[i];
            if (!reset) {
                unlockedTracks.put(varp, unlockedTracks.get(varp) - (unlockedTracks.get(varp) & MusicLoader.getExcludedVarpValue(i)));
            }
            final int value = unlockedTracks.get(varp) | MusicLoader.DEFAULT_VARP_VALUES[i];
            manager.sendVar(varp, value);
        }
    }

    public void stop() {
        setStopped(true);
        player.getPacketDispatcher().sendMusic(-1);
    }

    private boolean isDefaultsReset() {
        return player.getBooleanAttribute("reset default music tracks");
    }

    private void setDefaultsReset() {
        player.addAttribute("reset default music tracks", 1);
    }

    /**
     * Restarts the currently playing track again(after setting music volume from 0 to > 0)
     */
    public void restartCurrent() {
        final Music music = currentlyPlaying;
        if (music == null) {
            return;
        }
        if (stopped) stopped = false;
        nextSongAtTicks = music.getDuration();
        currentlyPlaying = music;
        ticks = 0;
    }

    /**
     * Plays a random track.
     */
    public void playRandomTrack() {
        Set<Music> list = null;
        if (player.getArea() != null && player.getArea() instanceof final MusicPlugin plugin) {
            list = plugin.getMusics(player);
        }

        if (list == null || list.isEmpty()) {
            list = World.getRegion(player.getLocation().getRegionId()).getMusicTracks();
            if (list == null || list.isEmpty()) {
                return;
            }
        }
        final Music randomTrack = Utils.getRandomCollectionElement(list);
        if (randomTrack == null || randomTrack == currentlyPlaying) {
            return;
        }
        final String trackName = randomTrack.getName();
        final OptionalInt slot = MUSIC_SLOT_NAME_ENUM.getKey(trackName);
        if (slot.isPresent()) {
            try {
                play(slot.getAsInt());
            } catch (Exception e) {
                logger.error("Music failed to play " + randomTrack, e);
            }
        } else {
            logger.debug("Slot not found for track name \"" + trackName + "\"" + " " + randomTrack.getMusicId());
        }
    }

    /**
     * Unlocks all the music tracks associated with the given region id.
     */
    public void unlock(final int regionId) {
        final Set<Music> list = World.getRegion(regionId).getMusicTracks();
        if (list == null || list.isEmpty()) {
            return;
        }
        for (final Music music : list) {
            unlock(music, false);
        }
        if (!player.getBooleanSetting(Setting.AUTO_MUSIC)) {
            return;
        }
        playRandomTrack();
    }

    public void unlock(final Music music) {
        unlock(music, true);
    }

    public void unlock(final Music music, final boolean play) {
        final OptionalInt optionalSlot = MUSIC_SLOT_NAME_ENUM.getKey(music.getName());
        if (!optionalSlot.isPresent()) return;
        final int slot = optionalSlot.getAsInt();
        final OptionalInt optionalMusicIndex = MUSIC_SLOT_INTERFACE_ENUM.getValue(slot);
        if (!optionalMusicIndex.isPresent()) {
            return;
        }
        final int musicIndex = optionalMusicIndex.getAsInt();
        final int index = (musicIndex >> 14 & 16383) - 1;
        if (index >= VARP_IDS.length) {
            return;
        }
        final int varp = VARP_IDS[index];
        final int value = player.getVarManager().getValue(varp) | (1 << (musicIndex & 16383));
        if (!isUnlocked(slot)) {
            unlockedTracks.put(varp, value - (value & MusicLoader.DEFAULT_VARP_VALUES[index]));
            player.getVarManager().sendVar(varp, value);
            final boolean unlocked = player.getEmotesHandler().isUnlocked(Emote.AIR_GUITAR);
            if (!unlocked && unlockedMusicCount() >= 500) {
                player.getEmotesHandler().unlock(Emote.AIR_GUITAR);
                player.sendMessage(Colour.RS_GREEN.wrap("Congratulations, you've unlocked the Air Guitar emote!"));
            }
        }
        if (play) {
            if (currentlyPlaying == music) {
                return;
            }
            play(slot);
        }
    }

    public int unlockedMusicCount() {
        int count = 0;
        final VarManager varManager = player.getVarManager();
        for (final int varp : VARP_IDS) {
            count += Integer.bitCount(varManager.getValue(varp));
        }
        return count;
    }

    /**
     * Processes the music player.
     */
    void processMusicPlayer() {
        if (stopped) {
            return;
        }
        if (forcePlayNext || ++ticks >= nextSongAtTicks) {
            forcePlayNext = false;
            resetCurrent();
            ticks = 0;
            if (!player.getBooleanSetting(Setting.LOOP_MUSIC)) {
                if (player.getBooleanSetting(Setting.AUTO_MUSIC)) {
                    playRandomTrack();
                } else {
                    stopped = true;
                }
            } else {
                final Music current = currentlyPlaying;
                currentlyPlaying = null;
                if (current != null) {
                    final OptionalInt slot = MUSIC_SLOT_NAME_ENUM.getKey(current.getName());
                    play(slot.orElseThrow(RuntimeException::new));
                }
            }
        }
    }

    public void playJingle(final int jingle) {
        this.ticks = 0;
        player.getPacketDispatcher().playJingle(jingle);
    }

    /**
     * Sends the hint for the music track at the given slot.
     */
    public void sendUnlockHint(final int slotId) {
        final var hint = MUSIC_SLOT_UNLOCK_ENUM.getValue(slotId).orElse("");
        player.sendMessage((!isUnlocked(slotId) ? "This track unlocks " : "This track was unlocked ") + hint.replace("unlocked ", ""));
    }

    /**
     * Resets the current music by stopping it client-sided.
     */
    private void resetCurrent() {
        // Only send packet is music volume is not 0
        if (player.getVarManager().getValue(SettingVariables.MUSIC_VOLUME_VARP_ID) != 0)
            player.getPacketDispatcher().sendMusic(-1);
    }

    /**
     * Whether the track at the given slot is unlocked or not.
     */
    private boolean isUnlocked(final int slot) {
        final OptionalInt optionalRandomSong = MUSIC_SLOT_INTERFACE_ENUM.getValue(slot);
        final String name = MUSIC_SLOT_NAME_ENUM.getValue(slot).orElse("");
        if (!optionalRandomSong.isPresent()) {
            return false;
        }
        final int randomSong = optionalRandomSong.getAsInt();
        if (randomSong == -1) {
            return true;
        }
        final int index = (randomSong >> 14 & 16383) - 1;
        if (index >= VARP_IDS.length) {
            return false;
        }
        final int value = unlockedTracks.get(VARP_IDS[index]) | MusicLoader.DEFAULT_VARP_VALUES[index];
        final int bitIndex = randomSong & 16383;
        return (value >> bitIndex & 1) == 1;
    }

    /**
     * Attempts to play the track at the requested slot. If it's not unlocked, or an error is thrown, returns as false and stops.
     */
    public boolean play(final int slot) {
        if (!isUnlocked(slot)) {
            return false;
        }
        final Optional<String> musicName = MUSIC_SLOT_NAME_ENUM.getValue(slot);
        final Music music = Music.map.get(musicName.orElseThrow(RuntimeException::new));
        if (music == null) {
            return false;
        }
        if (currentlyPlaying == music) {
            resetCurrent();
        }
        nextSongAtTicks = music.getDuration();
        ticks = 0;
        currentlyPlaying = music;
        stopped = false;
        player.getPacketDispatcher().sendMusic(music.getMusicId());
        if (GameInterface.MUSIC_TAB.getPlugin().isPresent())
            player.getPacketDispatcher().sendComponentText(GameInterface.MUSIC_TAB, GameInterface.MUSIC_TAB.getPlugin().get().getComponent("Song name"), music.getName());
        TreasureTrail.playSong(player, music.getName());
        return true;
    }

    public Int2IntOpenHashMap getUnlockedTracks() {
        return unlockedTracks;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void setForcePlayNext() {
        forcePlayNext = true;
    }

    public static void buildMusicEnums() {
        List<DBRowDefinition> rows = tableRows.get(44);
        if (rows == null || rows.isEmpty()) {
            System.err.println("No rows found for music table (id 44)!");
            return;
        }

        List<DBRowDefinition> validRowIndices = rows.stream()
                .filter(row -> {
                    Object unreleasedObj = row.getValueFromRow(8, 0, false);
                    boolean isUnreleased = false;
                    if (unreleasedObj instanceof Boolean) {
                        isUnreleased = (Boolean) unreleasedObj;
                    } else if (unreleasedObj instanceof Number) {
                        isUnreleased = (((Number) unreleasedObj).intValue() != 0);
                    }
                    return !isUnreleased;
                })
                .sorted(Comparator.comparingInt(DBRowDefinition::getId))
                .toList();

        Map<Integer, Object> nameValues = new TreeMap<>();
        Map<Integer, Object> interfaceValues = new TreeMap<>();
        Map<Integer, Object> unlockValues = new TreeMap<>();

        for (int filteredIndex = 0; filteredIndex < validRowIndices.size(); filteredIndex++) {
            DBRowDefinition row = validRowIndices.get(filteredIndex);
            String displayName = (String) row.getValueFromRow(1, 0);
            if (displayName == null || displayName.isEmpty()) {
                displayName = (String) row.getValueFromRow(0, 0, "");
            }

            nameValues.put(filteredIndex + 1, displayName);

            String unlockHint = (String) row.getValueFromRow(2);
            if (unlockHint != null) {
                unlockValues.put(filteredIndex + 1, unlockHint);
            }

            int indexOfVarp = (int) row.getValueFromRow(5, 0, 0) - 1;
            int bitOfVarp = (int) row.getValueFromRow(5, 1, -1);
            if (indexOfVarp == -1 || bitOfVarp == -1 || indexOfVarp >= VARP_IDS.length) {
                continue;
            }

            int interfaceValue = ((indexOfVarp + 1) << 14) | bitOfVarp;
            interfaceValues.put(filteredIndex + 1, interfaceValue);
        }

        EnumDefinitions nameEnumDef = EnumDefinitions.create(0, ScriptVarType.INTEGER, ScriptVarType.STRING);
        EnumDefinitions interfaceEnumDef = EnumDefinitions.create(1, ScriptVarType.INTEGER, ScriptVarType.INTEGER);
        EnumDefinitions unlockEnumDef = EnumDefinitions.create(2, ScriptVarType.INTEGER, ScriptVarType.STRING);

        nameEnumDef.setDefaultString("null");
        unlockEnumDef.setDefaultString("");
        interfaceEnumDef.setDefaultInt(-1);

        nameEnumDef.setValues(nameValues);
        interfaceEnumDef.setValues(interfaceValues);
        unlockEnumDef.setValues(unlockValues);

        StringEnum nameEnum = new StringEnum(
                nameEnumDef.getId(),
                nameEnumDef.getKeyType(),
                nameEnumDef.getValueType(),
                nameEnumDef.getDefaultString(),
                nameEnumDef.getValues()
        );
        StringEnum unlockEnum = new StringEnum(
                unlockEnumDef.getId(),
                unlockEnumDef.getKeyType(),
                unlockEnumDef.getValueType(),
                unlockEnumDef.getDefaultString(),
                unlockEnumDef.getValues()
        );
        IntEnum interfaceEnum = new IntEnum(
                interfaceEnumDef.getId(),
                interfaceEnumDef.getKeyType(),
                interfaceEnumDef.getValueType(),
                interfaceEnumDef.getDefaultInt(),
                interfaceEnumDef.getValues()
        );


        MusicHandler.MUSIC_SLOT_NAME_ENUM = nameEnum;
        MusicHandler.MUSIC_SLOT_UNLOCK_ENUM = unlockEnum;
        MusicHandler.MUSIC_SLOT_INTERFACE_ENUM = interfaceEnum;
    }
}
