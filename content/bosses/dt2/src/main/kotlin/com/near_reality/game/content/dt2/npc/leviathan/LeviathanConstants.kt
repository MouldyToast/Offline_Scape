package com.near_reality.game.content.dt2.npc.leviathan

import com.zenyte.game.world.Projectile
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.Tinting
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NpcId

/**
 * @author Khaled Abdeljaber
 */
data class Tuple4<A, B, C, D>(
    val a: A,
    val b: B,
    val c: C,
    val d: D
)

data class Tuple5<A, B, C, D, E>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E
)

object LeviathanConstants {

    val RESET_TINTING = Tinting(-1, -1, -1, 0, 0, 0)

    val LEVIATHAN_RISE_FROM_WATER = Animation(10291)
    val LEVIATHAN_LAUNCH_ORB_START = Animation(10278)
    val LEVIATHAN_LAUNCH_ORB_END_FAST = Animation(10279)
    val LEVIATHAN_LAUNCH_ORB_END_SINGLE = Animation(10281)
    val LEVIATHAN_LAUNCH_ORB_END_STUNNED = Animation(10280)
    val LEVIATHAN_LAUNCH_SHADOW_BOULDER = Animation(10289)
    val LEVIATHAN_SHADOW_BOULDER_START = Animation(10288)
    val LEVIATHAN_SHADOW_BOULDER_END = Animation(10290)
    val LEVIATHAN_ROCKFALL = Animation(10282)
    val LEVIATHAN_BITE = Animation(10283)
    val LEVIATHAN_BITE_2 = Animation(10284)
    val LEVIATHAN_LIGHTNING_BARRAGE_START = Animation(10285)
    val LEVIATHAN_LIGHTNING_BARRAGE_CONTINUE = Animation(10286)
    val LEVIATHAN_LIGHTNING_BARRAGE_END = Animation(10287)
    val LEVIATHAN_DEATH = Animation(10293)

    val LEVIATHAN_TAIL_MOVEMENT_A = Animation(10304)
    val LEVIATHAN_TAIL_MOVEMENT_B = Animation(10305)
    val LEVIATHAN_TAIL_MOVEMENT_C = Animation(10306)

    val ABYSSAL_PATHFINDER_SPAWN = Animation(10296)
    val HUMAN_HIT_WITH_OBSTACLE_FOOT = Animation(1114)


    // GFX

    val LEVIATHAN_RANGED_ORB_START = Graphics(2484)
    val LEVIATHAN_MELEE_ORB_START = Graphics(2485)
    val LEVIATHAN_MAGIC_ORB_START = Graphics(2486)

    val LEVIATHAN_RANGED_ORB_CONTINUE = Graphics(2481)
    val LEVIATHAN_MELEE_ORB_CONTINUE = Graphics(2482)
    val LEVIATHAN_MAGIC_ORB_CONTINUE = Graphics(2483)

    val LEVIATHAN_RANGED_ORB = Graphics(2487)
    val LEVIATHAN_MELEE_ORB = Graphics(2488)
    val LEVIATHAN_MAGIC_ORB = Graphics(2489)

    val LEVIATHAN_RANGED_ORB_IMPACT = Graphics(2490)
    val LEVIATHAN_MELEE_ORB_IMPACT = Graphics(2491)
    val LEVIATHAN_MAGIC_ORB_IMPACT = Graphics(2492)

    val LEVIATHAN_BOULDER_SHADOW = Graphics(1446)
    val LEVIATHAN_BOULDER_PROJECTILE = Graphics(2472)
    val LEVIATHAN_BOULDER_IMPACT_STAY = Graphics(2473)
    val LEVIATHAN_BOULDER_IMPACT_BREAK = Graphics(2474)
    val LEVIATHAN_BOULDER_FALLS_THEN_BREAKS_A = Graphics(2475)
    val LEVIATHAN_BOULDER_FALLS_THEN_BREAKS_B = Graphics(2476)
    val LEVIATHAN_BOULDER_FALLS_THEN_STAYS_0 = Graphics(2477)
    val LEVIATHAN_BOULDER_FALLS_THEN_STAYS_1 = Graphics(2478)
    val LEVIATHAN_BOULDER_FALLS_THEN_STAYS_2 = Graphics(2479)
    val LEVIATHAN_BOULDER_FALLS_THEN_STAYS_3 = Graphics(2480)

    val LEVIATHAN_GROUND_DUST_A = Graphics(2495)
    val LEVIATHAN_GROUND_DUST_B = Graphics(2496)
    val LEVIATHAN_GROUND_DUST_C = Graphics(2497)
    val LEVIATHAN_GROUND_DUST_D = Graphics(2498)
    val LEVIATHAN_GROUND_DUST_E = Graphics(2499)
    val LEVIATHAN_GROUND_DUST_F = Graphics(2500)
    val LEVIATHAN_GROUND_DUST_G = Graphics(2501)
    val LEVIATHAN_GROUND_DUST_H = Graphics(2502)

    val LEVIATHAN_LIGHTNING_BARRAGE_START_GFX = Graphics(2493)
    val LEVIATHAN_LIGHTNING_BARRAGE_SMOKE = Graphics(2494)
    val LEVIATHAN_GROUND_ELECTRICITY = Graphics(2503)
    val LEVIATHAN_LIGHTNING_STRIKE = Graphics(2504)
    val LEVIATHAN_SMOKE_BARRAGE = Graphics(2505)
    val LEVIATHAN_LIGHTNING_PROJECTILE = Graphics(2506)
    val LEVIATHAN_TORNADO_HIT = Graphics(2507)
    val LEVIATHAN_ABYSSAL_PATHFINDER = Graphics(2298)


    // Sounds
    val LEVIATHAN_RISE_FROM_WATER_SOUND = SoundEffect(7037, 10, 0)
    val LEVIATHAN_RANGED_VOLLEY_SOUND = SoundEffect(7023, 10, 0)
    val LEVIATHAN_MELEE_VOLLEY_SOUND = SoundEffect(7040, 10, 0)
    val LEVIATHAN_MAGIC_VOLLEY_SOUND = SoundEffect(7030, 10, 0)
    val LEVIATHAN_BOULDER_FALLING_START_SOUND = SoundEffect(7043, 10, 0)
    val LEVIATHAN_ELECTRICITY_START_SOUND = SoundEffect(7486, 10, 0)
    val LEVIATHAN_ELECTRICITY_SHOT_SOUND = SoundEffect(7050, 10, 0)
    val LEVIATHAN_LIGHTNING_STRIKE_SOUND = SoundEffect(7030, 10, 0)

    val LEVIATHAN_SHADOW_BOULDER_START_SOUND = SoundEffect(7032, 10, 0)
    val LEVIATHAN_SHADOW_BOULDER_LAUNCH_SOUND = SoundEffect(7028, 10, 0)
    val LEVIATHAN_SHADOW_BOULDER_LAND_SOUND = SoundEffect(7035, 10, 0)

    val LEVIATHAN_TORNADO_HIDDEN_SOUND = SoundEffect(7021, 10, 0)

    val LEVIATHAN_HIT_SOUND = SoundEffect(7031, 10, 0)
    val LEVIATHAN_DEATH_SOUND = SoundEffect(7034, 10, 0)

    // Based off of
    val VOLLEY_SPEEDS = listOf<Tuple5<Int, Int, Int, Int, Boolean>>(
        Tuple5(3, 30, 90, 6, false), // Two tick attack speed, Four tick reaction time
        Tuple5(2, 30, 60, 8, false), // Two tick attack speed, Three tick reaction time
        Tuple5(1, 30, 30, 8, true), // One tick attack speed, Two tick reaction time
        Tuple5(1, 30, 30, 12, false), // One tick attack speed, Two tick reaction time - First anim change
        Tuple5(1, 30, 30, 10, true), // One tick attack speed, One tick reaction time
        Tuple5(1, 0, 15, 10, true), // One tick attack speed, Zero Tick reaction time
        Tuple5(1, 0, 15, 12, true), // One tick attack speed, Zero Tick reaction time
    )

    enum class VolleyType(
        val start: Graphics,
        val follow: Graphics,
        val projectile: Graphics,
        val impact: Graphics,
        val sound: SoundEffect,
        val hitType: HitType
    ) {
        Melee(
            LEVIATHAN_MELEE_ORB_START,
            LEVIATHAN_MELEE_ORB_CONTINUE,
            LEVIATHAN_MELEE_ORB,
            LEVIATHAN_MELEE_ORB_IMPACT,
            LEVIATHAN_MELEE_VOLLEY_SOUND,
            HitType.MELEE
        ),
        Ranged(
            LEVIATHAN_RANGED_ORB_START,
            LEVIATHAN_RANGED_ORB_CONTINUE,
            LEVIATHAN_RANGED_ORB,
            LEVIATHAN_RANGED_ORB_IMPACT,
            LEVIATHAN_RANGED_VOLLEY_SOUND,
            HitType.RANGED
        ),
        Magic(
            LEVIATHAN_MAGIC_ORB_START,
            LEVIATHAN_MAGIC_ORB_CONTINUE,
            LEVIATHAN_MAGIC_ORB,
            LEVIATHAN_MAGIC_ORB_IMPACT,
            LEVIATHAN_MAGIC_VOLLEY_SOUND,
            HitType.MAGIC
        );

        companion object {
            val ALL = listOf(Melee, Ranged, Magic)
            val DISTANCED = listOf(Ranged, Magic)
        }
    }

    val LIGHTNING_PROJECTILE = Projectile(
        LEVIATHAN_LIGHTNING_PROJECTILE.getId(),
        200,
        0,
        40,
        3,
        110,
        200,
        0
    )

    val BOULDER_PROJECTILE = Projectile(
        LEVIATHAN_BOULDER_PROJECTILE.getId(),
        150,
        25,
        0,
        40,
        60,
        80,
        0
    )

    val TAIL_DATA: List<Tuple4<Int, Location, Location, Animation>> = listOf(
        Tuple4(NpcId.TAIL, Location(2053, 6355, 0), Location(2067, 6372, 0), LEVIATHAN_TAIL_MOVEMENT_A),
        Tuple4(NpcId.TAIL_12217, Location(2079, 6346, 0), Location(2081, 6372, 0), LEVIATHAN_TAIL_MOVEMENT_B),
        Tuple4(NpcId.TAIL, Location(2075, 6393, 0), Location(2073, 6372, 0), LEVIATHAN_TAIL_MOVEMENT_A),
        Tuple4(NpcId.TAIL, Location(2095, 6357, 0), Location(2081, 6372, 0), LEVIATHAN_TAIL_MOVEMENT_A),
        Tuple4(NpcId.TAIL, Location(2096, 6389, 0), Location(2081, 6372, 0), LEVIATHAN_TAIL_MOVEMENT_C)
    )

    val STRIKE_BORDER_AREAS = listOf(
        LeviathanLightStrikeBordersA::class,
        LeviathanLightStrikeBordersB::class,
        LeviathanLightStrikeBordersC::class,
        LeviathanLightStrikeBordersD::class
    )

    val SMOKE_BLAST_DATA = listOf(
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2073, 6363, 0), 45),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2074, 6363, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2075, 6363, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2076, 6363, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2077, 6363, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2078, 6363, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2079, 6363, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2072, 6364, 0), 45),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2073, 6364, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2074, 6364, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2075, 6364, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2076, 6364, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2077, 6364, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2078, 6364, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2079, 6364, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2072, 6365, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2073, 6365, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2074, 6365, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2075, 6365, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2076, 6365, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2077, 6365, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2078, 6365, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2079, 6365, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2072, 6366, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2073, 6366, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2074, 6366, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2075, 6366, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2076, 6366, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2077, 6366, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2078, 6366, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2079, 6366, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2072, 6367, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2073, 6367, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2074, 6367, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2075, 6367, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2076, 6367, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2077, 6367, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2078, 6367, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2079, 6367, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2072, 6368, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2073, 6368, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2074, 6368, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2075, 6368, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2076, 6368, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2077, 6368, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2078, 6368, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2079, 6368, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2072, 6369, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2073, 6369, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2074, 6369, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2075, 6369, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2076, 6369, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2077, 6369, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2078, 6369, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2072, 6370, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2073, 6370, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2074, 6370, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2075, 6370, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2076, 6370, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2077, 6370, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2072, 6371, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2073, 6371, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2074, 6371, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2075, 6371, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2076, 6371, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2077, 6371, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2072, 6372, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2073, 6372, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2074, 6372, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2075, 6372, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2076, 6372, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2077, 6372, 0), 6),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2072, 6373, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2073, 6373, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2074, 6373, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2075, 6373, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2076, 6373, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2077, 6373, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2072, 6374, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2073, 6374, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2074, 6374, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2075, 6374, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2076, 6374, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2077, 6374, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2072, 6375, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2073, 6375, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2074, 6375, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2075, 6375, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2076, 6375, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2077, 6375, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2078, 6375, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2072, 6376, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2073, 6376, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2074, 6376, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2075, 6376, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2076, 6376, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2077, 6376, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2078, 6376, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2079, 6376, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2072, 6377, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2073, 6377, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2074, 6377, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2075, 6377, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2076, 6377, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2077, 6377, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2078, 6377, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2079, 6377, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2072, 6378, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2073, 6378, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2074, 6378, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2075, 6378, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2076, 6378, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2077, 6378, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2078, 6378, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2079, 6378, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2072, 6379, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2073, 6379, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2074, 6379, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2075, 6379, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2076, 6379, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2077, 6379, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2078, 6379, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2079, 6379, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2072, 6380, 0), 45),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2073, 6380, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2074, 6380, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2075, 6380, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2076, 6380, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2077, 6380, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2078, 6380, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2079, 6380, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2072, 6381, 0), 48),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2073, 6381, 0), 45),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2074, 6381, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2075, 6381, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2076, 6381, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2077, 6381, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2078, 6381, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2079, 6381, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2080, 6363, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2081, 6363, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2087, 6363, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2080, 6364, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2081, 6364, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2082, 6364, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2083, 6364, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2084, 6364, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2085, 6364, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2086, 6364, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2087, 6364, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2080, 6365, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2081, 6365, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2082, 6365, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2083, 6365, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2084, 6365, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2085, 6365, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2086, 6365, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2087, 6365, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2080, 6366, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2081, 6366, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2082, 6366, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2083, 6366, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2084, 6366, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2085, 6366, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2086, 6366, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2087, 6366, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2080, 6367, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2081, 6367, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2082, 6367, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2083, 6367, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2084, 6367, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2085, 6367, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2086, 6367, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2087, 6367, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2080, 6368, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2081, 6368, 0), 6),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2082, 6368, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2083, 6368, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2084, 6368, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2085, 6368, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2086, 6368, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2087, 6368, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6368, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6369, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6370, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6371, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6372, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6373, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6374, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2089, 6375, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2089, 6376, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2089, 6377, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6378, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6379, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6380, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6381, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6383, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6384, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6385, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6386, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2084, 6369, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2085, 6369, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2086, 6369, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2087, 6369, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2085, 6370, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2086, 6370, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2087, 6370, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2085, 6371, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2086, 6371, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2087, 6371, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2085, 6372, 0), 6),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2086, 6372, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2087, 6372, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2085, 6373, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2086, 6373, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2087, 6373, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2085, 6374, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2086, 6374, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2087, 6374, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2084, 6375, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2085, 6375, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2086, 6375, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2087, 6375, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2080, 6376, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2081, 6376, 0), 6),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2082, 6376, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2083, 6376, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2084, 6376, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2085, 6376, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2086, 6376, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2087, 6376, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2080, 6377, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2081, 6377, 0), 9),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2082, 6377, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2083, 6377, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2084, 6377, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2085, 6377, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2086, 6377, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2087, 6377, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2080, 6378, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2081, 6378, 0), 12),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2082, 6378, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2083, 6378, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2084, 6378, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2085, 6378, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2086, 6378, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2087, 6378, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2080, 6379, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2081, 6379, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2084, 6379, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2085, 6379, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2086, 6379, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2087, 6379, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2080, 6380, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2081, 6380, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2084, 6380, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2085, 6380, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2086, 6380, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2087, 6380, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2080, 6381, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2081, 6381, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2084, 6381, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2085, 6381, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2088, 6363, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6363, 0), 45),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2088, 6364, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6364, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2090, 6364, 0), 45),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2088, 6365, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6365, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2090, 6365, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2088, 6366, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6366, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2090, 6366, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2088, 6367, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2089, 6367, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2090, 6367, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6368, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6369, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6370, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6371, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6372, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6373, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6374, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6375, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6376, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6377, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6378, 0), 15),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6379, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6380, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6381, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6382, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6383, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6384, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6385, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6386, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6376, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6377, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2090, 6378, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6377, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6378, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6379, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6380, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6381, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6382, 0), 39),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6383, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6384, 0), 42),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6385, 0), 45),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2088, 6386, 0), 45),


        /** Synthetic data **/


        Triple(LEVIATHAN_GROUND_DUST_A, Location(2088, 6368, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2088, 6369, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2088, 6370, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2088, 6371, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2088, 6372, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2088, 6373, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2088, 6374, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2088, 6375, 0), 27),


        Triple(LEVIATHAN_GROUND_DUST_A, Location(2091, 6376, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2091, 6375, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2091, 6374, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2091, 6373, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2091, 6372, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2091, 6371, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2091, 6370, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_A, Location(2091, 6369, 0), 33),

        Triple(LEVIATHAN_GROUND_DUST_B, Location(2092, 6369, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2092, 6370, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2092, 6371, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2092, 6372, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2092, 6373, 0), 36),

        Triple(LEVIATHAN_GROUND_DUST_B, Location(2074, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2075, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2076, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2077, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2078, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2082, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2083, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2084, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2085, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2086, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2087, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2082, 6381, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2083, 6381, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2084, 6381, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2085, 6381, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2086, 6381, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2087, 6381, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2082, 6379, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2083, 6379, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2082, 6380, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_B, Location(2083, 6380, 0), 33),


        Triple(LEVIATHAN_GROUND_DUST_H, Location(2087, 6382, 0), 41),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2086, 6381, 0), 44),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2087, 6381, 0), 39),

        Triple(LEVIATHAN_GROUND_DUST_H, Location(2086, 6382, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2085, 6382, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2084, 6382, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2083, 6382, 0), 30),


        Triple(LEVIATHAN_GROUND_DUST_G, Location(2084, 6383, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2085, 6383, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2086, 6383, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_H, Location(2087, 6383, 0), 39),


        Triple(LEVIATHAN_GROUND_DUST_G, Location(2082, 6379, 0), 18),
        Triple(LEVIATHAN_GROUND_DUST_G, Location(2083, 6379, 0), 21),

        Triple(LEVIATHAN_GROUND_DUST_C, Location(2082, 6363, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2080, 6362, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2079, 6362, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2078, 6362, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_C, Location(2077, 6362, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_D, Location(2076, 6362, 0), 36),

        Triple(LEVIATHAN_GROUND_DUST_D, Location(2071, 6367, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2071, 6368, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2071, 6369, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2071, 6370, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2071, 6371, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2071, 6372, 0), 21),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2071, 6373, 0), 24),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2071, 6374, 0), 27),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2071, 6375, 0), 30),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2071, 6376, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_F, Location(2071, 6377, 0), 36),

        Triple(LEVIATHAN_GROUND_DUST_D, Location(2070, 6370, 0), 36),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2070, 6371, 0), 33),
        Triple(LEVIATHAN_GROUND_DUST_E, Location(2070, 6372, 0), 30)

    ).associateBy { it.second }

    val FALLING_BOULDER_DELAYS = listOf(20, 50, 80)

}