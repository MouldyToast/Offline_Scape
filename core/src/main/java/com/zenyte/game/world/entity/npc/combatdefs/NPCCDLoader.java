package com.zenyte.game.world.entity.npc.combatdefs;

import cloud.rsps.util.JacksonObjectMapper;
import cloud.rsps.util.io.ByteBufferBackedInputStream;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.base.Stopwatch;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.dailychallenge.ChallengeWrapper;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.DailyChallenge;
import com.zenyte.logger.NearRealityLogger;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.AnimationDefinitions;
import org.slf4j.Logger;

import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author Kris | 18/11/2018 02:58
 * @author Jire
 */
public final class NPCCDLoader {

    private static final Path DIRECTORY_PATH = Path.of("cache", "data", "npcs", "combat");

    private static final int EXPECTED_SIZE = 4096;

    private static final Logger log = NearRealityLogger.getLogger(NPCCDLoader.class);

    public static final Int2ObjectMap<NPCCombatDefinitions> definitions =
            new Int2ObjectOpenHashMap<>(EXPECTED_SIZE);

    public static void parse() {
        parse(ForkJoinPool.commonPool());
    }

    public static void parse(final ExecutorService executorService) {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            ChallengeWrapper.init();

            final ObjectMapper mapper = JacksonObjectMapper.getObjectMapper();
            mapper.registerModule(
                    new SimpleModule().addKeyDeserializer(DailyChallenge.class, new KeyDeserializer() {
                        @Override
                        public Object deserializeKey(String key, DeserializationContext ctxt) {
                            return ChallengeWrapper.get(key);
                        }
                    })
            );

            final ObjectReader objectReader = mapper.readerFor(NPCCombatDefinitions.class);

            final List<Callable<Void>> callables = new ObjectArrayList<>(EXPECTED_SIZE);
            try (final Stream<Path> stream = Files.list(DIRECTORY_PATH)) {
                stream.forEach(file -> callables.add(() -> {
                    try (final FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
                        final MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                        final InputStream inputStream = new ByteBufferBackedInputStream(buf);
                        final NPCCombatDefinitions def = objectReader.readValue(inputStream);
                        insert(def.getId(), def);
                    } catch (final Throwable t) {
                        log.error("Failed to parse NPC combat definitions for file: " + file, t);
                        System.exit(1);
                        return null;
                    }

                    return null;
                }));
            }

            executorService.invokeAll(callables);
        } catch (final Exception e) {
            log.error("", e);
        }

        final int parsedSize = definitions.size();
        final long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        log.info("Parsed {} NPC combat definitions in {}ms.", parsedSize, elapsed);
    }

    public static NPCCombatDefinitions get(final int id) {
        return definitions.get(id);
    }

    public static final Int2ObjectMap<AttackDefinitions> skeletonToAttackDefs =
            new Int2ObjectOpenHashMap<>(EXPECTED_SIZE);
    public static final Int2ObjectMap<BlockDefinitions> skeletonToBlockDefs =
            new Int2ObjectOpenHashMap<>(EXPECTED_SIZE);
    public static final Int2ObjectMap<SpawnDefinitions> skeletonToSpawnDefs =
            new Int2ObjectOpenHashMap<>(EXPECTED_SIZE);

    public static synchronized void insert(int id, NPCCombatDefinitions def) {
        definitions.put(id, def);

        int skeleton = -1;
        AttackDefinitions attackDef = def.getAttackDefinitions();
        if (attackDef != null) {
            Animation animation = attackDef.getAnimation();
            if (animation != null && animation != Animation.STOP) {
                skeleton = AnimationDefinitions.getSkeletonId(animation.getId());
                if (skeleton >= 0) skeletonToAttackDefs.putIfAbsent(skeleton, AttackDefinitions.construct(attackDef));
            }
        }

        BlockDefinitions blockDef = def.getBlockDefinitions();
        if (blockDef != null) {
            Animation animation = blockDef.getAnimation();
            if (animation != null && animation != Animation.STOP) {
                if (skeleton < 0) skeleton = AnimationDefinitions.getSkeletonId(animation.getId());
                if (skeleton >= 0) skeletonToBlockDefs.putIfAbsent(skeleton, BlockDefinitions.construct(blockDef));
            }
        }

        SpawnDefinitions spawnDef = def.getSpawnDefinitions();
        if (spawnDef != null) {
            Animation deathAnimation = spawnDef.getDeathAnimation();
            if (deathAnimation != null && deathAnimation != Animation.STOP) {
                if (skeleton < 0) skeleton = AnimationDefinitions.getSkeletonId(deathAnimation.getId());
                if (skeleton >= 0) {
                    skeletonToSpawnDefs.putIfAbsent(skeleton, SpawnDefinitions.construct(spawnDef));
                    return;
                }
            }

            Animation spawnAnimation = spawnDef.getSpawnAnimation();
            if (spawnAnimation != null && spawnAnimation != Animation.STOP) {
                if (skeleton < 0) skeleton = AnimationDefinitions.getSkeletonId(spawnAnimation.getId());
                if (skeleton >= 0) {
                    skeletonToSpawnDefs.putIfAbsent(skeleton, SpawnDefinitions.construct(spawnDef));
                }
            }
        }
    }

}
