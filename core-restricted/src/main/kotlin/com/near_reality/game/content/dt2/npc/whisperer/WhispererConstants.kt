package com.near_reality.game.content.dt2.npc.whisperer

import com.near_reality.game.content.dt2.npc.*
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.THE_WHISPERER
import com.zenyte.game.world.entity.npc.NpcId.THE_WHISPERER_12206
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.DynamicArea


object WhispererConstants {

    const val WHISPERER_INSANE_AREA_TIMER = "whisperer_insane_area_timer"
    const val WHISPERER_INSANE_DAMAGE_TIMER = "whisperer_insane_damage_timer"
    const val WHISPERER_HEADBAR_TIMER = "whisperer_headbar_timer"
    const val WHISPERER_PILLAR_HEADBAR_TIMER = "whisperer_pillar_headbar_timer"

    const val WHISPERER_INSANITY_IF = 834

    // Animations (_anim)
    val WHISPERER_ATTACK_BASIC_LEFT_SLOW = Animation(10235)
    val WHISPERER_ATTACK_BASIC_RIGHT_SLOW = Animation(10236)
    val WHISPERER_ATTACK_BASIC_LEFT_FASTER = Animation(10237)
    val WHISPERER_ATTACK_BASIC_RIGHT_FASTER = Animation(10238)
    val WHISPERER_ATTACK_BASIC_LEFT_FASTEST = Animation(10239)
    val WHISPERER_ATTACK_BASIC_RIGHT_FASTEST = Animation(10240)
    val WHISPERER_ATTACK_BASIC_LEFT_GLOWING_SLOW = Animation(10241)
    val WHISPERER_ATTACK_BASIC_RIGHT_GLOWING_SLOW = Animation(10242)
    val WHISPERER_ATTACK_BASIC_LEFT_GLOWING_FASTER = Animation(10243)
    val WHISPERER_ATTACK_BASIC_RIGHT_GLOWING_FASTER = Animation(10244)
    val WHISPERER_ATTACK_BASIC_LEFT_GLOWING_FASTEST = Animation(10245)
    val WHISPERER_ATTACK_BASIC_RIGHT_GLOWING_FASTEST = Animation(10246)
    val WHISPERER_ATTACK_MELEE = Animation(10234)
    val WHISPERER_SWITCH_PHASE_START = Animation(10247)
    val WHISPERER_SWITCH_PHASE_END_PAUSE = Animation(10248)
    val WHISPERER_SWITCH_PHASE_END = Animation(10249)
    val WHISPERER_RISE_GLOWING_START = Animation(10250)
    val WHISPERER_RISE_GLOWING_PAUSE = Animation(10251)
    val WHISPERER_RISE_GLOWING_END = Animation(10252)
    val WHISPERER_RISE_GLOWING_END_2 = Animation(10253)
    val WHISPERER_NAMASTE = Animation(10254)
    val WHISPERER_NAMASTE_RETURN = Animation(10255)
    val WHISPERER_PRAY_RISE_START = Animation(10256)
    val WHISPERER_PRAY_RISE_CONTINUE = Animation(10257)
    val WHISPERER_PRAY_RISE_END = Animation(10258)
    val WHISPERER_SOUL_END = Animation(10259)
    val WHISPERER_DEATH = Animation(10260)
    val WHISPERER_DEATH_2 = Animation(10261)
    val WHISPERER_DEATH_3 = Animation(10262)
    val WHISPERER_PILLAR_SPAWN = Animation(10226)
    val WHISPERER_TENTACLE_SPAWN = Animation(10263)
    val WHISPERER_TENTACLE_DESPAWN = Animation(10266)
    val WHISPERER_SOUL_SPAWN = Animation(9047)
    val WHISPERER_SOUL_LOOP = Animation(5545)
    val WHISPERER_SOUL_DEATH = Animation(5542)
    val HUMAN_WHISPERER_PUSH_BACK_GET_UP = Animation(9799)
    val WHISPERER_ODD_FIGURE_SPAWN = Animation(10228)
    val WHISPERER_ODD_FIGURE_RISE = Animation(10227)


    // Graphics (_gfx)
    val WHISPERER_BASIC_RANGED_PROJECTILE = Graphics(2444)
    val WHISPERER_BASIC_MAGIC_PROJECTILE = Graphics(2445)
    val WHISPERER_BASIC_MAGIC_HIT = Graphics(2446)
    val WHISPERER_SMALL_POOL_BUBBLES = Graphics(2447)
    val WHISPERER_SMALL_POOL_SPLASH = Graphics(2448)
    val WHISPERER_MEDIUM_POOL_SPLASH = Graphics(2449)
    val WHISPERER_LARGE_POOL_SPLASH = Graphics(2450)
    val WHISPERER_MEDIUM_POOL_BUBBLES = Graphics(2451)
    val WHISPERER_MEDIUM_POOL_SPLASH_2 = Graphics(2452)
    val WHISPERER_LARGE_POOL_BUBBLES = Graphics(2453)
    val WHISPERER_WAVE_PUSH = Graphics(2454)
    val WHISPERER_SHADOW = Graphics(2455)
    val WHISPERER_SOUL_SIPHON_RETURN = Graphics(2457)
    val WHISPERER_SOUL_SIPHON_RETURN_SHADOW_REALM = Graphics(2458)
    val WHISPERER_SOUL_SIPHON = Graphics(668)
    val WHISPERER_SOUL_SIPHON_EFFECT_FULLY = Graphics(2470)
    val WHISPERER_SOUL_SIPHON_EFFECT = Graphics(2469)
    val WHISPERER_BIND_PROJECTILE = Graphics(2467)
    val WHISPERER_BIND_HIT = Graphics(2466)
    val WHISPERER_LEECH_POOL = Graphics(2459)
    val WHISPERER_LEECH_BLUE_BALL_DISARM = Graphics(2460)
    val WHISPERER_LEECH_BLUE_BALL_REMOVE = Graphics(2461)
    val WHISPERER_LEECH_BLUE_BALL_EXPLODE = Graphics(2462)
    val WHISPERER_LEECH_GREEN_BALL_DISARM = Graphics(2463)
    val WHISPERER_LEECH_BLUE_REALM_BALL_REMOVE = Graphics(2464)
    val WHISPERER_LEECH_GREEN_BALL_EXPLODE = Graphics(2465)
    val WHISPERER_LEECH_PROJECTILE = Graphics(2456)

    const val WHISPERER_HIT_SYNTH = 7487
    const val WHISPERER_WAVE_1_SYNTH = 7369
    const val WHISPERER_WAVE_2_SYNTH = 7367
    const val WHISPERER_WAVE_3_SYNTH = 7319
    const val WHISPERER_WAVE_4_SYNTH = 7364
    const val WHISPERER_WAVE_REACHED_SYNTH = 7321
    const val WHISPERER_SOUL_SIPHON_SYNTH = 7412
    const val WHISPERER_SOUL_SIPHON_END_SYNTH = 7297
    const val WHISPERER_SOUL_SIPHON_END_2_SYNTH = 7415
    const val WHISPERER_SOUL_SIPHON_RETURN_SYNTH = 7312
    const val WHISPERER_SOUL_SIPHON_EFFECT_SYNTH = 4064
    const val WHISPERER_STEP_OVER_LEECH_SYNTH = 7461
    const val WHISPERER_BIND_SYNTH = 7461
    const val WHISPERER_SCREECH_START_1_SYNTH = 7365
    const val WHISPERER_SCREECH_START_2_SYNTH = 7308
    const val WHISPERER_SCREECH_START_3_SYNTH = 7275
    const val WHISPERER_SCREECH_MIDDLE_1_SYNTH = 7437
    const val WHISPERER_SCREECH_MIDDLE_2_SYNTH = 7325
    const val WHISPERER_SCREECH_MIDDLE_3_SYNTH = 7293
    const val WHISPERER_SCREECH_MIDDLE_4_SYNTH = 7415
    const val WHISPERER_SCREECH_MIDDLE_5_SYNTH = 7459

    const val WHISPERER_SOUL_SIPHON_END_3_SYNTH = 6797
    const val WHISPERER_SOUL_SIPHON_END_4_SYNTH = 6672
    const val WHISPERER_SOUL_SIPHON_END_5_SYNTH = 6735

    val THE_WHISPERER_FIGHT_AREA = TheWhispererFightArea::class
    val LASSAR_UNDERGROUND_SHADOW_REALM_AREA = LassarUndergroundShadowRealmArea::class

    val WHISPERER_LEECHES = listOf(
        listOf(
            // Route 1
            // Dead positions
            Triple(36, 33, 0) to false,
            Triple(35, 34, 0) to false,
            Triple(34, 35, 0) to false,
            Triple(33, 36, 0) to false,
            Triple(32, 37, 0) to false,
            Triple(35, 36, 0) to false,
            Triple(32, 29, 0) to false,
            Triple(31, 30, 0) to false,
            Triple(30, 31, 0) to false,
            Triple(29, 32, 0) to false,
            Triple(28, 33, 0) to false,
            Triple(29, 30, 0) to false,

            // Alive positions
            Triple(36, 31, 0) to true,
            Triple(34, 33, 0) to true,
            Triple(36, 35, 0) to true,
            Triple(34, 37, 0) to true,
            Triple(32, 35, 0) to true,
            Triple(30, 37, 0) to true,
            Triple(28, 35, 0) to true,
            Triple(30, 33, 0) to true,
            Triple(28, 31, 0) to true,
            Triple(30, 29, 0) to true,
            Triple(32, 31, 0) to true,
            Triple(34, 29, 0) to true,
        ), listOf(

            // Route 2
            // Dead positions
            Triple(35, 34, 0) to false,
            Triple(34, 35, 0) to false,
            Triple(33, 36, 0) to false,
            Triple(35, 36, 0) to false,
            Triple(31, 30, 0) to false,
            Triple(30, 31, 0) to false,
            Triple(29, 32, 0) to false,
            Triple(29, 30, 0) to false,

            // Alive positions
            Triple(36, 31, 0) to true,
            Triple(36, 33, 0) to true,
            Triple(34, 33, 0) to true,
            Triple(36, 35, 0) to true,
            Triple(34, 37, 0) to true,
            Triple(32, 37, 0) to true,
            Triple(32, 35, 0) to true,
            Triple(30, 37, 0) to true,
            Triple(28, 35, 0) to true,
            Triple(28, 33, 0) to true,
            Triple(30, 33, 0) to true,
            Triple(28, 31, 0) to true,
            Triple(30, 29, 0) to true,
            Triple(32, 29, 0) to true,
            Triple(32, 31, 0) to true,
            Triple(34, 29, 0) to true,
        ), listOf(

            // Route 3
            // Dead positions - North
            Triple(32, 37, 0) to false,
            Triple(31, 39, 0) to false,
            Triple(32, 39, 0) to false,
            Triple(33, 39, 0) to false,

            // Dead positions - East
            Triple(35, 33, 0) to false,
            Triple(36, 33, 0) to false,
            Triple(38, 33, 0) to false,

            // Dead positions - South
            Triple(31, 27, 0) to false,
            Triple(32, 27, 0) to false,
            Triple(33, 27, 0) to false,
            Triple(32, 29, 0) to false,

            // Dead positions - West
            Triple(26, 33, 0) to false,
            Triple(28, 33, 0) to false,
            Triple(29, 33, 0) to false,

            // Alive positions - North
            Triple(31, 37, 0) to true,
            Triple(33, 37, 0) to true,

            // Alive positions - East
            Triple(35, 31, 0) to true,
            Triple(37, 31, 0) to true,
            Triple(37, 33, 0) to true,
            Triple(35, 35, 0) to true,
            Triple(37, 35, 0) to true,

            // Alive positions - South
            Triple(31, 29, 0) to true,
            Triple(33, 29, 0) to true,

            // Alive positions - West
            Triple(29, 31, 0) to true,
            Triple(27, 31, 0) to true,
            Triple(27, 33, 0) to true,
            Triple(27, 35, 0) to true,
            Triple(29, 35, 0) to true,
        ), listOf(

            // Route 4
            // Dead positions - North
            Triple(32, 37, 0) to false,
            Triple(32, 39, 0) to false,

            // Dead positions - East
            Triple(37, 31, 0) to false,
            Triple(35, 33, 0) to false,
            Triple(36, 33, 0) to false,
            Triple(38, 33, 0) to false,
            Triple(37, 35, 0) to false,

            // Dead positions - South
            Triple(32, 27, 0) to false,
            Triple(32, 29, 0) to false,

            // Dead positions - West
            Triple(27, 31, 0) to false,
            Triple(26, 33, 0) to false,
            Triple(28, 33, 0) to false,
            Triple(29, 33, 0) to false,
            Triple(27, 35, 0) to false,

            // Alive positions - North
            Triple(31, 37, 0) to true,
            Triple(33, 37, 0) to true,
            Triple(31, 39, 0) to true,
            Triple(33, 39, 0) to true,

            // Alive positions - East
            Triple(35, 31, 0) to true,
            Triple(37, 33, 0) to true,
            Triple(35, 35, 0) to true,

            // Alive positions - South
            Triple(31, 27, 0) to true,
            Triple(31, 29, 0) to true,
            Triple(33, 27, 0) to true,
            Triple(33, 29, 0) to true,

            // Alive positions - West
            Triple(29, 31, 0) to true,
            Triple(27, 33, 0) to true,
            Triple(29, 35, 0) to true
        )
    )

    val SOUL_SPAWNS_STANDARD = listOf(
        coord(2650, 6375, 0),
        coord(2650, 6373, 0),
        coord(2648, 6373, 0),
        coord(2648, 6366, 0),
        coord(2650, 6364, 0),
        coord(2652, 6362, 0),
        coord(2662, 6362, 0),
        coord(2662, 6364, 0),
        coord(2664, 6364, 0),
        coord(2660, 6375, 0),
        coord(2662, 6373, 0),
        coord(2664, 6371, 0)
    )

    val SOUL_SPAWNS_AWAKENED = listOf(
        coord(2647, 6369, 0),
        coord(2648, 6366, 0),
        coord(2650, 6364, 0),
        coord(2652, 6362, 0),
        coord(2650, 6375, 0),
        coord(2650, 6373, 0),
        coord(2648, 6373, 0),
        coord(2655, 6376, 0),
        coord(2662, 6362, 0),
        coord(2662, 6364, 0),
        coord(2657, 6360, 0),
        coord(2660, 6375, 0),
        coord(2662, 6373, 0),
        coord(2664, 6364, 0),
        coord(2666, 6367, 0),
        coord(2664, 6371, 0)
    )

    val PILLARS_TO_MODEL = mapOf(
        coord(2646, 6365, 0) to 49312,
        coord(2648, 6361, 0) to 49310,
        coord(2651, 6363, 0) to 49312,
        coord(2654, 6364, 0) to 49312,
        coord(2657, 6364, 0) to 49313,
        coord(2660, 6363, 0) to 49313,
        coord(2663, 6361, 0) to 49313,
        coord(2665, 6365, 0) to 49312
    )

    val PILLARS_AWAKENED_HITPOINTS = intArrayOf(20, 20, 20, 20, 40, 60, 80, 100)
    val PILLARS_NON_AWAKENED_HITPOINTS = intArrayOf(20, 20, 20, 20, 20, 20, 40, 60)

    val SCREECH_SPOTANIMS = mapOf(
        coord(2387, 6361, 0) to 50,
        coord(2388, 6361, 0) to 47,
        coord(2389, 6361, 0) to 44,
        coord(2390, 6361, 0) to 41,
        coord(2391, 6361, 0) to 38,
        coord(2387, 6362, 0) to 53,
        coord(2388, 6362, 0) to 50,
        coord(2389, 6362, 0) to 47,
        coord(2390, 6362, 0) to 44,
        coord(2391, 6362, 0) to 41,
        coord(2387, 6363, 0) to 56,
        coord(2388, 6363, 0) to 53,
        coord(2389, 6363, 0) to 50,
        coord(2390, 6363, 0) to 47,
        coord(2391, 6363, 0) to 44,
        coord(2388, 6364, 0) to 56,
        coord(2389, 6364, 0) to 53,
        coord(2390, 6364, 0) to 50,
        coord(2391, 6364, 0) to 47,
        coord(2389, 6365, 0) to 56,
        coord(2395, 6353, 0) to 26,
        coord(2396, 6353, 0) to 23,
        coord(2397, 6353, 0) to 20,
        coord(2398, 6353, 0) to 17,
        coord(2399, 6353, 0) to 14,
        coord(2394, 6354, 0) to 26,
        coord(2395, 6354, 0) to 23,
        coord(2396, 6354, 0) to 20,
        coord(2397, 6354, 0) to 17,
        coord(2398, 6354, 0) to 14,
        coord(2399, 6354, 0) to 11,
        coord(2393, 6355, 0) to 26,
        coord(2394, 6355, 0) to 23,
        coord(2395, 6355, 0) to 20,
        coord(2396, 6355, 0) to 17,
        coord(2397, 6355, 0) to 14,
        coord(2398, 6355, 0) to 11,
        coord(2399, 6355, 0) to 8,
        coord(2392, 6356, 0) to 26,
        coord(2393, 6356, 0) to 23,
        coord(2394, 6356, 0) to 20,
        coord(2395, 6356, 0) to 17,
        coord(2396, 6356, 0) to 14,
        coord(2397, 6356, 0) to 11,
        coord(2398, 6356, 0) to 8,
        coord(2399, 6356, 0) to 5,
        coord(2392, 6357, 0) to 23,
        coord(2393, 6357, 0) to 20,
        coord(2394, 6357, 0) to 17,
        coord(2395, 6357, 0) to 14,
        coord(2396, 6357, 0) to 11,
        coord(2397, 6357, 0) to 8,
        coord(2398, 6357, 0) to 5,
        coord(2399, 6357, 0) to 5,
        coord(2392, 6358, 0) to 26,
        coord(2393, 6358, 0) to 23,
        coord(2394, 6358, 0) to 20,
        coord(2395, 6358, 0) to 17,
        coord(2396, 6358, 0) to 14,
        coord(2397, 6358, 0) to 11,
        coord(2398, 6358, 0) to 8,
        coord(2399, 6358, 0) to 5,
        coord(2392, 6359, 0) to 29,
        coord(2393, 6359, 0) to 26,
        coord(2394, 6359, 0) to 23,
        coord(2395, 6359, 0) to 20,
        coord(2396, 6359, 0) to 17,
        coord(2397, 6359, 0) to 14,
        coord(2398, 6359, 0) to 11,
        coord(2399, 6359, 0) to 8,
        coord(2392, 6360, 0) to 32,
        coord(2393, 6360, 0) to 29,
        coord(2394, 6360, 0) to 26,
        coord(2395, 6360, 0) to 23,
        coord(2396, 6360, 0) to 20,
        coord(2397, 6360, 0) to 17,
        coord(2398, 6360, 0) to 14,
        coord(2399, 6360, 0) to 11,
        coord(2392, 6361, 0) to 35,
        coord(2393, 6361, 0) to 32,
        coord(2394, 6361, 0) to 29,
        coord(2395, 6361, 0) to 26,
        coord(2396, 6361, 0) to 23,
        coord(2397, 6361, 0) to 20,
        coord(2398, 6361, 0) to 17,
        coord(2399, 6361, 0) to 14,
        coord(2392, 6362, 0) to 38,
        coord(2393, 6362, 0) to 35,
        coord(2394, 6362, 0) to 32,
        coord(2395, 6362, 0) to 29,
        coord(2396, 6362, 0) to 26,
        coord(2397, 6362, 0) to 23,
        coord(2398, 6362, 0) to 20,
        coord(2399, 6362, 0) to 17,
        coord(2392, 6363, 0) to 41,
        coord(2393, 6363, 0) to 38,
        coord(2394, 6363, 0) to 35,
        coord(2395, 6363, 0) to 32,
        coord(2396, 6363, 0) to 29,
        coord(2397, 6363, 0) to 26,
        coord(2398, 6363, 0) to 23,
        coord(2399, 6363, 0) to 20,
        coord(2392, 6364, 0) to 44,
        coord(2393, 6364, 0) to 41,
        coord(2394, 6364, 0) to 38,
        coord(2395, 6364, 0) to 35,
        coord(2396, 6364, 0) to 32,
        coord(2397, 6364, 0) to 29,
        coord(2398, 6364, 0) to 26,
        coord(2399, 6364, 0) to 23,
        coord(2392, 6365, 0) to 47,
        coord(2393, 6365, 0) to 44,
        coord(2394, 6365, 0) to 41,
        coord(2395, 6365, 0) to 38,
        coord(2396, 6365, 0) to 35,
        coord(2397, 6365, 0) to 32,
        coord(2398, 6365, 0) to 29,
        coord(2399, 6365, 0) to 26,
        coord(2392, 6366, 0) to 50,
        coord(2393, 6366, 0) to 47,
        coord(2394, 6366, 0) to 44,
        coord(2395, 6366, 0) to 41,
        coord(2396, 6366, 0) to 38,
        coord(2397, 6366, 0) to 35,
        coord(2398, 6366, 0) to 32,
        coord(2399, 6366, 0) to 29,
        coord(2392, 6367, 0) to 53,
        coord(2393, 6367, 0) to 50,
        coord(2394, 6367, 0) to 47,
        coord(2395, 6367, 0) to 44,
        coord(2396, 6367, 0) to 41,
        coord(2397, 6367, 0) to 38,
        coord(2398, 6367, 0) to 35,
        coord(2399, 6367, 0) to 32,
        coord(2392, 6368, 0) to 56,
        coord(2393, 6368, 0) to 53,
        coord(2394, 6368, 0) to 50,
        coord(2395, 6368, 0) to 47,
        coord(2396, 6368, 0) to 44,
        coord(2397, 6368, 0) to 41,
        coord(2398, 6368, 0) to 38,
        coord(2399, 6368, 0) to 35,
        coord(2393, 6369, 0) to 56,
        coord(2394, 6369, 0) to 53,
        coord(2395, 6369, 0) to 50,
        coord(2396, 6369, 0) to 47,
        coord(2397, 6369, 0) to 44,
        coord(2398, 6369, 0) to 41,
        coord(2399, 6369, 0) to 38,
        coord(2394, 6370, 0) to 56,
        coord(2395, 6370, 0) to 53,
        coord(2396, 6370, 0) to 50,
        coord(2397, 6370, 0) to 47,
        coord(2398, 6370, 0) to 44,
        coord(2399, 6370, 0) to 41,
        coord(2395, 6371, 0) to 56,
        coord(2396, 6371, 0) to 53,
        coord(2397, 6371, 0) to 50,
        coord(2398, 6371, 0) to 47,
        coord(2399, 6371, 0) to 44,
        coord(2396, 6372, 0) to 56,
        coord(2397, 6372, 0) to 53,
        coord(2398, 6372, 0) to 50,
        coord(2399, 6372, 0) to 47,
        coord(2397, 6373, 0) to 56,
        coord(2398, 6373, 0) to 53,
        coord(2399, 6373, 0) to 50,
        coord(2398, 6374, 0) to 56,
        coord(2399, 6374, 0) to 53,
        coord(2399, 6375, 0) to 56,
        coord(2400, 6353, 0) to 11,
        coord(2401, 6353, 0) to 14,
        coord(2402, 6353, 0) to 17,
        coord(2400, 6354, 0) to 8,
        coord(2401, 6354, 0) to 11,
        coord(2402, 6354, 0) to 14,
        coord(2403, 6354, 0) to 17,
        coord(2404, 6354, 0) to 20,
        coord(2405, 6354, 0) to 23,
        coord(2406, 6354, 0) to 26,
        coord(2400, 6355, 0) to 5,
        coord(2401, 6355, 0) to 8,
        coord(2402, 6355, 0) to 11,
        coord(2403, 6355, 0) to 14,
        coord(2404, 6355, 0) to 17,
        coord(2405, 6355, 0) to 20,
        coord(2406, 6355, 0) to 23,
        coord(2407, 6355, 0) to 26,
        coord(2400, 6356, 0) to 5,
        coord(2401, 6356, 0) to 5,
        coord(2402, 6356, 0) to 8,
        coord(2403, 6356, 0) to 11,
        coord(2404, 6356, 0) to 14,
        coord(2405, 6356, 0) to 17,
        coord(2406, 6356, 0) to 20,
        coord(2407, 6356, 0) to 23,
        coord(2400, 6357, 0) to 5,
        coord(2401, 6357, 0) to 5,
        coord(2402, 6357, 0) to 5,
        coord(2403, 6357, 0) to 8,
        coord(2404, 6357, 0) to 11,
        coord(2405, 6357, 0) to 14,
        coord(2406, 6357, 0) to 17,
        coord(2407, 6357, 0) to 20,
        coord(2400, 6358, 0) to 5,
        coord(2401, 6358, 0) to 5,
        coord(2402, 6358, 0) to 8,
        coord(2403, 6358, 0) to 11,
        coord(2404, 6358, 0) to 14,
        coord(2405, 6358, 0) to 17,
        coord(2406, 6358, 0) to 20,
        coord(2407, 6358, 0) to 23,
        coord(2400, 6359, 0) to 5,
        coord(2401, 6359, 0) to 8,
        coord(2402, 6359, 0) to 11,
        coord(2403, 6359, 0) to 14,
        coord(2404, 6359, 0) to 17,
        coord(2405, 6359, 0) to 20,
        coord(2406, 6359, 0) to 23,
        coord(2407, 6359, 0) to 26,
        coord(2400, 6360, 0) to 8,
        coord(2401, 6360, 0) to 11,
        coord(2402, 6360, 0) to 14,
        coord(2403, 6360, 0) to 17,
        coord(2404, 6360, 0) to 20,
        coord(2405, 6360, 0) to 23,
        coord(2406, 6360, 0) to 26,
        coord(2407, 6360, 0) to 29,
        coord(2400, 6361, 0) to 11,
        coord(2401, 6361, 0) to 14,
        coord(2402, 6361, 0) to 17,
        coord(2403, 6361, 0) to 20,
        coord(2404, 6361, 0) to 23,
        coord(2405, 6361, 0) to 26,
        coord(2406, 6361, 0) to 29,
        coord(2407, 6361, 0) to 32,
        coord(2400, 6362, 0) to 14,
        coord(2401, 6362, 0) to 17,
        coord(2402, 6362, 0) to 20,
        coord(2403, 6362, 0) to 23,
        coord(2404, 6362, 0) to 26,
        coord(2405, 6362, 0) to 29,
        coord(2406, 6362, 0) to 32,
        coord(2407, 6362, 0) to 35,
        coord(2400, 6363, 0) to 17,
        coord(2401, 6363, 0) to 20,
        coord(2402, 6363, 0) to 23,
        coord(2403, 6363, 0) to 26,
        coord(2404, 6363, 0) to 29,
        coord(2405, 6363, 0) to 32,
        coord(2406, 6363, 0) to 35,
        coord(2407, 6363, 0) to 38,
        coord(2400, 6364, 0) to 20,
        coord(2401, 6364, 0) to 23,
        coord(2402, 6364, 0) to 26,
        coord(2403, 6364, 0) to 29,
        coord(2404, 6364, 0) to 32,
        coord(2405, 6364, 0) to 35,
        coord(2406, 6364, 0) to 38,
        coord(2407, 6364, 0) to 41,
        coord(2400, 6365, 0) to 23,
        coord(2401, 6365, 0) to 26,
        coord(2402, 6365, 0) to 29,
        coord(2403, 6365, 0) to 32,
        coord(2404, 6365, 0) to 35,
        coord(2405, 6365, 0) to 38,
        coord(2406, 6365, 0) to 41,
        coord(2407, 6365, 0) to 44,
        coord(2400, 6366, 0) to 26,
        coord(2401, 6366, 0) to 29,
        coord(2402, 6366, 0) to 32,
        coord(2403, 6366, 0) to 35,
        coord(2404, 6366, 0) to 38,
        coord(2405, 6366, 0) to 41,
        coord(2406, 6366, 0) to 44,
        coord(2407, 6366, 0) to 47,
        coord(2400, 6367, 0) to 29,
        coord(2401, 6367, 0) to 32,
        coord(2402, 6367, 0) to 35,
        coord(2403, 6367, 0) to 38,
        coord(2404, 6367, 0) to 41,
        coord(2405, 6367, 0) to 44,
        coord(2406, 6367, 0) to 47,
        coord(2407, 6367, 0) to 50,
        coord(2400, 6368, 0) to 32,
        coord(2401, 6368, 0) to 35,
        coord(2402, 6368, 0) to 38,
        coord(2403, 6368, 0) to 41,
        coord(2404, 6368, 0) to 44,
        coord(2405, 6368, 0) to 47,
        coord(2406, 6368, 0) to 50,
        coord(2407, 6368, 0) to 53,
        coord(2400, 6369, 0) to 35,
        coord(2401, 6369, 0) to 38,
        coord(2402, 6369, 0) to 41,
        coord(2403, 6369, 0) to 44,
        coord(2404, 6369, 0) to 47,
        coord(2405, 6369, 0) to 50,
        coord(2406, 6369, 0) to 53,
        coord(2407, 6369, 0) to 56,
        coord(2400, 6370, 0) to 38,
        coord(2401, 6370, 0) to 41,
        coord(2402, 6370, 0) to 44,
        coord(2403, 6370, 0) to 47,
        coord(2404, 6370, 0) to 50,
        coord(2405, 6370, 0) to 53,
        coord(2406, 6370, 0) to 56,
        coord(2400, 6371, 0) to 41,
        coord(2401, 6371, 0) to 44,
        coord(2402, 6371, 0) to 47,
        coord(2403, 6371, 0) to 50,
        coord(2404, 6371, 0) to 53,
        coord(2405, 6371, 0) to 56,
        coord(2400, 6372, 0) to 44,
        coord(2401, 6372, 0) to 47,
        coord(2402, 6372, 0) to 50,
        coord(2403, 6372, 0) to 53,
        coord(2404, 6372, 0) to 56,
        coord(2400, 6373, 0) to 47,
        coord(2401, 6373, 0) to 50,
        coord(2402, 6373, 0) to 53,
        coord(2403, 6373, 0) to 56,
        coord(2400, 6374, 0) to 50,
        coord(2401, 6374, 0) to 53,
        coord(2402, 6374, 0) to 56,
        coord(2400, 6375, 0) to 53,
        coord(2401, 6375, 0) to 56,
        coord(2400, 6376, 0) to 56,
        coord(2408, 6356, 0) to 26,
        coord(2409, 6356, 0) to 29,
        coord(2408, 6357, 0) to 23,
        coord(2409, 6357, 0) to 26,
        coord(2408, 6358, 0) to 26,
        coord(2409, 6358, 0) to 29,
        coord(2408, 6359, 0) to 29,
        coord(2409, 6359, 0) to 32,
        coord(2408, 6360, 0) to 32,
        coord(2409, 6360, 0) to 35,
        coord(2408, 6361, 0) to 35,
        coord(2409, 6361, 0) to 38,
        coord(2410, 6361, 0) to 41,
        coord(2411, 6361, 0) to 44,
        coord(2408, 6362, 0) to 38,
        coord(2409, 6362, 0) to 41,
        coord(2410, 6362, 0) to 44,
        coord(2411, 6362, 0) to 47,
        coord(2408, 6363, 0) to 41,
        coord(2409, 6363, 0) to 44,
        coord(2410, 6363, 0) to 47,
        coord(2411, 6363, 0) to 50,
        coord(2408, 6364, 0) to 44,
        coord(2409, 6364, 0) to 47,
        coord(2410, 6364, 0) to 50,
        coord(2411, 6364, 0) to 53,
        coord(2408, 6365, 0) to 47,
        coord(2409, 6365, 0) to 50,
        coord(2410, 6365, 0) to 53,
        coord(2411, 6365, 0) to 56,
        coord(2408, 6366, 0) to 50,
        coord(2409, 6366, 0) to 53,
        coord(2410, 6366, 0) to 56,
        coord(2408, 6367, 0) to 53,
        coord(2409, 6367, 0) to 56,
        coord(2408, 6368, 0) to 56
    )


    val WHISPERER_INSANITY_VARPBIT = 15064 // Default 100
    val WHISPERER_BLACKSTONE_READY_VARBIT = 15067 // Default 0
}


fun DynamicArea.addTentacles(fixed: Boolean, offset: Boolean) {
    val instance = this
    val delta = if (offset) -256 else 0
    MapObject(
        ObjectId.TENTACLE, instance[2652 + delta, 6384, 0], 10, 0
    ).spawn()

    MapObject(
        ObjectId.TENTACLE, instance[2653 + delta, 6384, 0], 10, if (fixed) 0 else 1
    ).spawn()

    MapObject(
        ObjectId.TENTACLE, instance[2654 + delta, 6384, 0], 10, 0
    ).spawn()

    MapObject(
        ObjectId.TENTACLE, instance[2655 + delta, 6384, 0], 10, if (fixed) 0 else 2
    ).spawn()

    MapObject(
        ObjectId.TENTACLE, instance[2656 + delta, 6384, 0], 10, if (fixed) 0 else 3
    ).spawn()

    MapObject(
        ObjectId.TENTACLE, instance[2657 + delta, 6384, 0], 10, if (fixed) 0 else 1
    ).spawn()

    MapObject(
        ObjectId.TENTACLE, instance[2658 + delta, 6384, 0], 10, if (fixed) 0 else 2
    ).spawn()

    MapObject(
        ObjectId.TENTACLE, instance[2659 + delta, 6384, 0], 10, 0
    ).spawn()

    MapObject(
        ObjectId.TENTACLE, instance[2660 + delta, 6384, 0], 10, if (fixed) 0 else 3
    ).spawn()
}

enum class SoulColour(val amount: Int, val say: String, val colours: List<Int>) {
    YELLOW(2, "Vita!", listOf()),
    CYAN(3, "Sanitas!", listOf(-31934, -31945, -31955, -31984)),
    GREEN(4, "Morss!", listOf(26434, 26423, 26413, 26386)),
    BLUE(3, "Oratio!", listOf(-25790, -25801, -24787, -24816))
}

enum class SpecialPhase {
    STANDARD,
    SHADOW_LEECHES,
    SOUL_SIPHON,
    SCREECH,
    BINDING,
    ENRAGED,

    ;

    val isSpecial get() = this != STANDARD && this != ENRAGED
}

enum class SpecialAttackRotations(vararg val rotations: SpecialPhase) {

    FIRST(SpecialPhase.SHADOW_LEECHES, SpecialPhase.SCREECH, SpecialPhase.SOUL_SIPHON),
    SECOND(SpecialPhase.SCREECH, SpecialPhase.SOUL_SIPHON, SpecialPhase.SHADOW_LEECHES)
}

enum class BasicAttackSpeed {
    SLOW, FASTER, FASTEST
}

enum class BasicAttackSide {
    LEFT, RIGHT, RANDOM
}

fun AttackType.sequence(count: Int): Array<AttackType> {
    return Array(count) { this }
}

object ProjConstants {
    val WHISPERER_BASIC_MAGIC = Projectile(
        WhispererConstants.WHISPERER_BASIC_MAGIC_PROJECTILE.id,
        87,   // startHeight
        25,   // endHeight
        30,   // delay (slow)
        5,    // angle (corrected from 60; see explanation)
        51,   // duration
        0,    // distanceOffset
        0     // multiplier
    )

    val WHISPERER_BASIC_MAGIC_FASTEST = Projectile(
        WhispererConstants.WHISPERER_BASIC_MAGIC_PROJECTILE.id,
        87,
        25,
        0,    // delay (fast)
        5,    // angle
        51,   // duration
        0,
        0
    )

    val WHISPERER_BASIC_RANGED = Projectile(
        WhispererConstants.WHISPERER_BASIC_RANGED_PROJECTILE.id,
        87,
        25,
        30,   // delay (slow)
        5,    // angle
        51,   // duration
        0,
        0
    )

    val WHISPERER_BASIC_RANGED_FASTEST = Projectile(
        WhispererConstants.WHISPERER_BASIC_RANGED_PROJECTILE.id,
        87,
        25,
        0,    // delay (fast)
        5,    // angle
        51,   // duration
        0,
        0
    )

    val WHISPERER_BIND = Projectile(
        WhispererConstants.WHISPERER_BIND_PROJECTILE.id,
        145,  // startHeight
        0,    // endHeight
        80,   // delay
        5,    // angle
        51,   // duration
        16,   // distanceOffset
        0     // multiplier
    )

    val WHISPERER_LEECH = Projectile(
        WhispererConstants.WHISPERER_LEECH_PROJECTILE.id,
        0,
        0,
        30,   // delay
        5,    // angle
        51,   // duration
        0,
        0     // multiplier (changed from 0)
    )

    val WHISPERER_SOUL_SIPHON = Projectile(
        WhispererConstants.WHISPERER_SOUL_SIPHON.id,
        125,
        0,
        0,    // delay
        5,    // angle
        51,   // duration
        127,  // distanceOffset
        0     // multiplier (changed from 128)
    )

    val WHISPERER_SOUL_SIPHON_RETURN = Projectile(
        WhispererConstants.WHISPERER_SOUL_SIPHON_RETURN.id,
        100,
        0,
        0,    // delay
        5,    // angle
        51,   // duration
        127,  // distanceOffset
        0     // multiplier (changed from 60)
    )
}

object AttrConstants {

    val NPC_SPECIAL_PHASE = "npc_special_phase"
    val NPC_SPECIAL_PHASE_NEXT = "npc_special_phase_next"
    val NPC_SPECIAL_COUNT = "npc_special_count"
    val NPC_IS_WAITING = "npc_is_waiting"
    val LOWEST_HITPOINTS = "lowest_hitpoints"
    val WHISPERER_REALM_TIME_ACTIVE = "whisperer_realm_time_active"

    val WHISPERER_TIMER_CURRENT = "whisperer_timer_current"
    val WHISPERER_TIMER_MAX = "whisperer_timer_max"
    val WHISPERER_SANITY_COUNTER = "whisperer_sanity_counter"


}


var NPC.soulColour by attrEnumNullable<SoulColour>(AttrConstants.NPC_SPECIAL_PHASE)

var NPC.phase by attrEnumNullable<SpecialPhase>(AttrConstants.NPC_SPECIAL_PHASE)
var NPC.rotation by attrEnumNullable<SpecialAttackRotations>(AttrConstants.NPC_SPECIAL_PHASE_NEXT)
var NPC.specials by attrOrElse(AttrConstants.NPC_SPECIAL_COUNT, 0)

val NPC.awakened get() = id == THE_WHISPERER_12206
var NPC.waiting by attrOrElse(AttrConstants.NPC_IS_WAITING, true)

val Entity.whisperer: NPC?
    get() = location.findNpc(radius = 30) { id == THE_WHISPERER || id == THE_WHISPERER_12206 }
val Player.awakenedEncounter: Boolean
    get() = whisperer?.awakened == true

var NPC.lowestHitpoints by attrOrElse(AttrConstants.LOWEST_HITPOINTS, 10000)
val NPC.lowestHitpointsAsPercentage: Int
    get() = (lowestHitpoints.toDouble() / maxHitpoints * 100).toInt()

val NPC.isEnraged get() = lowestHitpointsAsPercentage <= 0 && phase == SpecialPhase.ENRAGED

var Player.sanity
    get() = varManager.getBitValue(WhispererConstants.WHISPERER_INSANITY_VARPBIT)
    set(value) {
        varManager.sendBit(WhispererConstants.WHISPERER_INSANITY_VARPBIT, value)
    }
var Player.whispererBlackstoneReady
    get() = varManager.getBitValue(WhispererConstants.WHISPERER_BLACKSTONE_READY_VARBIT) == 1
    set(value) {
        varManager.sendBit(WhispererConstants.WHISPERER_BLACKSTONE_READY_VARBIT, if (value) 1 else 0)
    }

var Player.shadowRealmTicks by attrOrElse(AttrConstants.WHISPERER_REALM_TIME_ACTIVE, 0)

var NPC.whispererTimerCurrent by attrOrElse(AttrConstants.WHISPERER_TIMER_CURRENT, 0)
var NPC.whispererTimerMax by attrOrElse(AttrConstants.WHISPERER_TIMER_MAX, 0)
var NPC.attackCounterStandard by attrOrElse("attack_counter_standard", 0)
var NPC.allObjects by attrNullable<MutableList<WorldObject>>("dt2_all_objects")