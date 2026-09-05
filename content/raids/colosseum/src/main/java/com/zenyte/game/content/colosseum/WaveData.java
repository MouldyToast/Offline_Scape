package com.zenyte.game.content.colosseum;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NpcId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Wave composition data for the Fortis Colosseum.
 * <p>
 * Spawn composition algorithm ported from LlemonDuck/fortis-colosseum RuneLite plugin (BSD-2-Clause).
 * 12 fixed spawn points sourced from Supalosa/osrs-colosseum LoS tool (SPAWNS array in lineOfSight.ts),
 * converted to source-region 7216 world coordinates using mapping: worldX = losX + 1808, worldY = 3123 - losY.
 * Verified against 8 RSProx-observed spawn positions — every one maps to a fixed point exactly.
 */
public class WaveData {

    /** 12 fixed spawn points (SW corner of 3×3 block, source region 7216 world coords). */
    public static final int[][] SPAWN_POINTS = {
            {1811, 3104},  // #1  West-south      (LoS 3,19)
            {1817, 3106},  // #2  West-centre      (LoS 9,17)
            {1811, 3109},  // #3  West             (LoS 3,14)
            {1821, 3109},  // #4  Centre-west      (LoS 13,14)
            {1827, 3109},  // #5  Centre-east      (LoS 19,14)
            {1825, 3114},  // #6  North-centre     (LoS 17,9)
            {1821, 3103},  // #7  South-west       (LoS 13,20)
            {1827, 3103},  // #8  South-east       (LoS 19,20)
            {1824, 3099},  // #9  South            (LoS 16,24)
            {1832, 3107},  // #10 East             (LoS 24,16)
            {1836, 3109},  // #11 Far east         (LoS 28,14)
            {1836, 3104},  // #12 Far east-south   (LoS 28,19)
    };

    /**
     * Fremennik spawn zone bounds (RSProx-verified across 12 waves, 2 runs).
     * The trio spawns at a random base tile within this zone, then each NPC is
     * placed at a fixed offset from that base — see {@code ColosseumInstance#startWave()}.
     * <p>
     * Observed base positions: X 1821–1827, Y 3105–3111 (~7×7 zone around arena centre).
     */
    public static final int FREMENNIK_ZONE_MIN_X = 1821;
    public static final int FREMENNIK_ZONE_MAX_X = 1827;
    public static final int FREMENNIK_ZONE_MIN_Y = 3105;
    public static final int FREMENNIK_ZONE_MAX_Y = 3111;

    /** Reinforcement timer: 67 ticks = 40 seconds. */
    public static final int REINFORCEMENT_DELAY_TICKS = 67;

    /** Attack immunity after wave start: 3 ticks. */
    public static final int SPAWN_ATTACK_DELAY_TICKS = 3;

    /**
     * Get starting NPC IDs for a wave (not positions — positions assigned separately).
     * Algorithm from LlemonDuck/fortis-colosseum WaveSpawns.java.
     */
    public static List<Integer> getStartingNpcs(int wave, int modifierBitmask) {
        List<Integer> npcs = new ArrayList<>();

        // Fremennik trio — every wave 1-11
        int fremCount = hasModifier(modifierBitmask, 6) ? 4 : 3; // Quartet modifier
        npcs.add(NpcId.FREMENNIK_WARBAND_BERSERKER);
        npcs.add(NpcId.FREMENNIK_WARBAND_ARCHER);
        npcs.add(NpcId.FREMENNIK_WARBAND_SEER);
        if (fremCount >= 4) {
            npcs.add(NpcId.FREMENNIK_WARBAND_ARCHER); // extra archer
        }

        // Serpent shaman — waves 1-6 starting
        if (wave <= 6) {
            npcs.add(NpcId.SERPENT_SHAMAN);
        }

        // Javelin Colossus: wave 2=1, wave 3=2, wave 4=0, wave 5+=alternating
        int javelinCount = 0;
        if (wave == 2 || wave == 3) {
            javelinCount = wave - 1;
        } else if (wave >= 5) {
            javelinCount = 2 - (wave % 2);
        }
        for (int i = 0; i < javelinCount; i++) {
            npcs.add(NpcId.JAVELIN_COLOSSUS);
        }

        // Manticore — waves 4+ (1 on waves 4-8, 2 on waves 9+)
        if (wave >= 4) {
            int count = wave <= 8 ? 1 : 2;
            for (int i = 0; i < count; i++) {
                npcs.add(NpcId.MANTICORE);
            }
        }

        // Shockwave Colossus — waves 7, 8, 11 only
        if (wave == 7 || wave == 8 || wave == 11) {
            int count = hasModifier(modifierBitmask, 9) ? 2 : 1; // Dynamic Duo
            for (int i = 0; i < count; i++) {
                npcs.add(NpcId.SHOCKWAVE_COLOSSUS);
            }
        }

        return npcs;
    }

    /** Get reinforcement NPC IDs (arrive REINFORCEMENT_DELAY_TICKS after wave start). */
    public static List<Integer> getReinforcementNpcs(int wave, int modifierBitmask) {
        List<Integer> npcs = new ArrayList<>();

        // Jaguar warrior — waves 1-6 only
        if (wave <= 6) {
            npcs.add(NpcId.JAGUAR_WARRIOR);
        }

        // Serpent shaman reinforcement — waves 4-6 and 10-11
        if ((wave >= 4 && wave <= 6) || wave >= 10) {
            npcs.add(NpcId.SERPENT_SHAMAN);
        }

        // Minotaur — waves 7+
        if (wave >= 7) {
            npcs.add(hasModifier(modifierBitmask, 13)
                    ? NpcId.MINOTAUR_12813   // Red Flag → routefinding variant
                    : NpcId.MINOTAUR_12812);
        }

        return npcs;
    }

    /** Pick a random fixed spawn point, excluding used and excluded indices. */
    public static int[] getRandomSpawnPoint(Set<Integer> usedIndices, Set<Integer> excludedIndices) {
        List<Integer> available = new ArrayList<>();
        for (int i = 0; i < SPAWN_POINTS.length; i++) {
            if (!usedIndices.contains(i) && !excludedIndices.contains(i)) {
                available.add(i);
            }
        }
        if (available.isEmpty()) {
            // Fallback: ignore exclusion zone if all points blocked
            for (int i = 0; i < SPAWN_POINTS.length; i++) {
                if (!usedIndices.contains(i)) {
                    available.add(i);
                }
            }
        }
        if (available.isEmpty()) {
            // All 12 points used — shouldn't happen (max NPCs per wave < 12)
            return SPAWN_POINTS[Utils.random(SPAWN_POINTS.length - 1)];
        }
        int idx = available.get(Utils.random(available.size() - 1));
        usedIndices.add(idx);
        return SPAWN_POINTS[idx];
    }

    public static boolean isFremennik(int npcId) {
        return npcId == NpcId.FREMENNIK_WARBAND_BERSERKER
                || npcId == NpcId.FREMENNIK_WARBAND_ARCHER
                || npcId == NpcId.FREMENNIK_WARBAND_SEER;
    }

    private static boolean hasModifier(int bitmask, int modId) {
        return (bitmask & (1 << modId)) != 0;
    }
}
