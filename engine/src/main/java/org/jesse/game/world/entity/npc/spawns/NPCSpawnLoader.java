package org.jesse.game.world.entity.npc.spawns;

import com.google.common.base.Stopwatch;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.impl.Crab;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorMonster;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.RegionArea;
import org.jesse.logger.NearRealityLogger;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Jire
 */
public final class NPCSpawnLoader {

    private static final Logger log = NearRealityLogger.getLogger(NPCSpawnLoader.class);

    public static final IntSet dropViewerNPCs = IntSets.synchronize(new IntOpenHashSet());
    public static final IntSet ignoredMonsters = IntSets.synchronize(new IntOpenHashSet());

    private static final Int2ObjectMap<Set<String>> npcAreaMap = new Int2ObjectOpenHashMap<>();

    private static final List<NPCSpawn> artificialSpawns = new ArrayList<>();

    private static final Int2IntMap npcTransformers = new Int2IntOpenHashMap();

    public static Set<String> getFoundLocations(final int npcId) {
        return npcAreaMap.get(npcId);
    }

    public static void populateAreaMap() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        final int definitionsSize = DEFINITIONS.size();

        final Int2ObjectMap<Set<String>> npcAreaMap = new Int2ObjectOpenHashMap<>(definitionsSize);
        final int spawnsSize = definitionsSize + artificialSpawns.size();
        final ObjectList<NPCSpawn> spawns = new ObjectArrayList<>(spawnsSize);
        spawns.addAll(artificialSpawns);
        spawns.addAll(DEFINITIONS);

        final Int2ObjectMap<RegionArea> areaMap = new Int2ObjectOpenHashMap<>(spawnsSize);
        final Int2ObjectMap<String> spatialGrid = new Int2ObjectOpenHashMap<>(spawnsSize);

        for (final NPCSpawn spawn : spawns) {
            final int x = spawn.getX(), y = spawn.getY(), z = spawn.getZ();
            final Location tile = new Location(x, y, z);
            final RegionArea area = GlobalAreaManager.getArea(tile);
            if (area != null) {
                final int tileHash = tile.getPositionHash();
                areaMap.put(tileHash, area);

                final String name = area.name();
                spatialGrid.put(tileHash, name);
            }
        }

        long foundAreaCount = 0;

        for (final NPCSpawn spawn : spawns) {
            final int x = spawn.getX(), y = spawn.getY(), z = spawn.getZ();
            final int tileHash = Location.getHash(x, y, z);

            final RegionArea directArea = areaMap.get(tileHash);
            String name = directArea == null ? "Undefined area" : directArea.name();

            if (directArea == null) {
                outer:
                for (int dx = -25; dx <= 25; dx++) {
                    final int xdx = x + dx;
                    final int baseHash = Location.getHash(xdx, y, z);
                    for (int dy = -25; dy <= 25; dy++) {
                        final int nearbyHash = baseHash + dy;
                        final String maybeName = spatialGrid.get(nearbyHash);
                        if (maybeName != null) {
                            name = maybeName;
                            foundAreaCount++;
                            break outer;
                        }
                    }
                }
            }

            final int id = spawn.getId();
            final int transformedId = npcTransformers.getOrDefault(id, id);

            Set<String> set = npcAreaMap.get(transformedId);
            if (set == null) {
                set = new ObjectArraySet<>();
                npcAreaMap.put(transformedId, set);
            }
            set.add(name);
        }

        NPCSpawnLoader.npcAreaMap.putAll(npcAreaMap);

        log.info("Populated area map for {} NPCs ({} mapped near) in {}ms.",
                NPCSpawnLoader.npcAreaMap.size(),
                foundAreaCount,
                stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    static {
        //Skotizo
        artificialSpawns.add(new NPCSpawn(7286, 1698, 9886, 0, Direction.SOUTH, 5));
        //Alchemical Hydra
        artificialSpawns.add(new NPCSpawn(8621, 1364, 10265, 0, Direction.SOUTH, 5));
        //Dusk (Grotesque Guardians)
        artificialSpawns.add(new NPCSpawn(7888, 3426, 3542, 2, Direction.SOUTH, 5));
        //Obor
        artificialSpawns.add(new NPCSpawn(7416, 3095, 9832, 0, Direction.SOUTH, 5));
        //Bryophyta
        artificialSpawns.add(new NPCSpawn(8195, 3174, 9900, 0, Direction.SOUTH, 5));
        //Zulrah
        artificialSpawns.add(new NPCSpawn(2042, 2267, 3072, 0, Direction.SOUTH, 5));
        //Vorkath
        artificialSpawns.add(new NPCSpawn(8061, 2269, 4062, 0, Direction.SOUTH, 5));
        //Cerberus
        artificialSpawns.add(new NPCSpawn(5862, 1239, 1250, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(5862, 1303, 1313, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(5862, 1367, 1249, 0, Direction.SOUTH, 5));
        //Corporeal beast
        artificialSpawns.add(new NPCSpawn(319, 2990, 4381, 2, Direction.SOUTH, 5));
        //Scavenger beasts
        artificialSpawns.add(new NPCSpawn(7548, 3279, 5225, 1, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(7549, 3279, 5225, 1, Direction.SOUTH, 5));
        //Mogre
        artificialSpawns.add(new NPCSpawn(2592, 2993, 3108, 0, Direction.SOUTH, 5));
        //The Mimic
        artificialSpawns.add(new NPCSpawn(8633, 2719, 4318, 1, Direction.SOUTH, 5));

        dropViewerNPCs.addAll(SuperiorMonster.superiorMonsters);
        //Armadylian guardian
        dropViewerNPCs.add(6587);
        //Bandosian guardian
        dropViewerNPCs.add(6587);
        //Brassican mage
        dropViewerNPCs.add(7310);


        //rdi barrows
        dropViewerNPCs.add(16052);
        dropViewerNPCs.add(16053);
        dropViewerNPCs.add(16054);
        dropViewerNPCs.add(16055);
        dropViewerNPCs.add(16056);
        dropViewerNPCs.add(16057);

        dropViewerNPCs.add(NpcId.ABYSSAL_SIRE_5908);
        //vanstrom Klause
        dropViewerNPCs.add(NpcId.VANSTROM_KLAUSE_9569);
        //Ancient wizard
        dropViewerNPCs.add(7307);
        //Krakens
        dropViewerNPCs.add(494);
        dropViewerNPCs.add(492);
        npcTransformers.put(493, 492);
        npcTransformers.put(496, 494);
        //Werewolves
        npcTransformers.put(2631, 2593);
        //All kinds of crabs.
        npcTransformers.putAll(Crab.rocks2AliveMap);
        //Wall beast
        dropViewerNPCs.add(476);
        npcTransformers.put(475, 476);
        //Zygomites
        dropViewerNPCs.add(536);
        dropViewerNPCs.add(1023);
        dropViewerNPCs.add(471);
        npcTransformers.put(536, 537);
        npcTransformers.put(1023, 1024);
        npcTransformers.put(471, 7797);

        //Tree spirits
        dropViewerNPCs.add(1163);
        dropViewerNPCs.add(1861);
        dropViewerNPCs.add(1862);
        dropViewerNPCs.add(1863);
        dropViewerNPCs.add(1864);
        dropViewerNPCs.add(1865);
        dropViewerNPCs.add(1866);
        dropViewerNPCs.add(6380);
        //Locost riders
        dropViewerNPCs.add(795);
        dropViewerNPCs.add(796);
        dropViewerNPCs.add(800);
        dropViewerNPCs.add(801);
        //Brutal green dragons
        dropViewerNPCs.add(2918);
        dropViewerNPCs.add(8081);
        dropViewerNPCs.add(8583);

        // callisto + artio
        dropViewerNPCs.add(NpcId.CALLISTO_6609);
        dropViewerNPCs.add(NpcId.ARTIO);
        //vetion + calvarion
        dropViewerNPCs.add(NpcId.VETION);
        dropViewerNPCs.add(NpcId.CALVARION);
        // ven + spindel
        dropViewerNPCs.add(NpcId.VENENATIS_6610);
        dropViewerNPCs.add(NpcId.SPINDEL);


        //Godwars sergeants
        artificialSpawns.add(new NPCSpawn(2216, 2868, 5362, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2217, 2872, 5354, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2218, 2871, 5359, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2206, 2901, 5264, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2207, 2897, 5263, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(2208, 2895, 5265, 0, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3130, 2929, 5327, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3131, 2921, 5327, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3132, 2923, 5324, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3163, 2834, 5297, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3164, 2827, 5299, 2, Direction.SOUTH, 5));
        artificialSpawns.add(new NPCSpawn(3165, 2829, 5300, 2, Direction.SOUTH, 5));
        dropViewerNPCs.addAll(npcTransformers.values());
        artificialSpawns.forEach(spawn -> dropViewerNPCs.add(spawn.getId()));

        dropViewerNPCs.add(NpcId.GANODERMIC_BEAST);
        dropViewerNPCs.add(NpcId.THE_NIGHTMARE_9430);
        dropViewerNPCs.add(NpcId.PHOSANIS_NIGHTMARE_11155);
        ignoredMonsters.add(8615);
        ignoredMonsters.add(NpcId.PHANTOM_MUSPAH_12079);
        ignoredMonsters.add(NpcId.PHANTOM_MUSPAH_12082);
        ignoredMonsters.add(NpcId.PHANTOM_MUSPAH_12078);

        dropViewerNPCs.add(12080);
    }

    /**
     * A mapping of the definitions to their item id.
     */
    public static final ObjectList<NPCSpawn> DEFINITIONS = new ObjectArrayList<>();

    public static void loadNPCSpawns() {
        WorldTasksManager.schedule(() -> {
            try {
                DEFINITIONS.forEach(v -> {
                    try {
                        final int id = v.getId();
                        dropViewerNPCs.add(id);

                        final Location tile = new Location(v.getX(), v.getY(), v.getZ());
                        World.getChunk(tile.getChunkHash());
                        World.spawnNPC(v, id, tile, v.getDirection(), v.getRadius());
                    } catch (final Throwable t) {
                        log.error("Failed to spawn NPC: " + v, t);
                    }
                });
            } catch (final Throwable e) {
                log.error("Failed to load NPC spawns...", e);
            }
        });
    }

    public static boolean addArtificialSpawn(NPCSpawn spawn) {
        return artificialSpawns.add(spawn);
    }

    public static boolean addSpawn(NPCSpawn spawn) {
        return DEFINITIONS.add(spawn);
    }

}
