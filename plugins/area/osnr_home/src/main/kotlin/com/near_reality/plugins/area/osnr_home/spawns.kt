package com.near_reality.plugins.area.osnr_home

import com.near_reality.game.content.slayer.SlayerMaster
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.plugins.renewednpc.ZenyteGuide
import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Spawns : NPCSpawnsScript() {

    val loc: Location = ZenyteGuide.HOME_ZENYTE_GUIDE

    init {
        NEARREALITY_GUIDE(loc.x, loc.y, 0, SOUTH, 2)

        BANKER_2117(3089, 3489, 0, SOUTH, 0)
        BANKER_2117(3090, 3489, 0, SOUTH, 0)
        BANKER_2118(3089, 3492, 0, NORTH, 0)
        BANKER_2118(3090, 3492, 0, NORTH, 0)
        GRAND_EXCHANGE_CLERK(3088, 3491, 0, WEST, 0)
        GRAND_EXCHANGE_CLERK_2150(3088, 3490, 0, WEST, 0)
        GRAND_EXCHANGE_CLERK_2149(3091, 3491, 0, EAST, 0)
        GRAND_EXCHANGE_CLERK_2151(3091, 3490, 0, EAST, 0)

        BANKER_2117(3089, 3452, 0, SOUTH, 0)
        BANKER_2117(3090, 3452, 0, SOUTH, 0)
        BANKER_2118(3089, 3455, 0, NORTH, 0)
        BANKER_2118(3090, 3455, 0, NORTH, 0)
        GRAND_EXCHANGE_CLERK(3088, 3454, 0, WEST, 0)
        GRAND_EXCHANGE_CLERK_2150(3088, 3453, 0, WEST, 0)
        GRAND_EXCHANGE_CLERK_2149(3091, 3454, 0, EAST, 0)
        GRAND_EXCHANGE_CLERK_2151(3091, 3453, 0, EAST, 0)
        WAYDAR(3089, 3459, 0, NORTH, 3)

        VANNAKA(3074, 3494, 0, SOUTH)
        MAZCHNA(3075, 3494, 0, SOUTH)
        16064(3076, 3494, 0, SOUTH) // Sumona
        TURAEL(3077, 3494, 0, SOUTH)
        CHAELDAR(3077, 3487, 0, NORTH)
        KONAR_QUO_MATEN(3074, 3487, 0, NORTH)
        SlayerMaster.NIEVE.npcId(3076, 3487, 0, NORTH)
        DURADEL(3075, 3487, 0, NORTH)

        WATSON(3094, 3509, 0, WEST, 0)

        SKULLY(3086, 3484, 0, NORTH, 0)

        16065(3092, 3491, 0, SOUTH, 15) // HANS

        OZIACH(3077, 3515, 0, SOUTH, 1)

        MAKEOVER_MAGE(3087, 3497, 0, SOUTH, 0)

        WISE_OLD_MAN(3092, 3514, 0, SOUTH, 0)

        MAC(3093, 3497, 0, SOUTH, 0)

        TOOL_LEPRECHAUN(3080, 3478, 0, SOUTH, 1)
        MASTER_FARMER(3077, 3475, 0, SOUTH, 2)

        KING_THOROS(3133, 3482, 0, WEST, 0)

        KRYSTILIA(3117, 3515, 0, NORTH, 1)

        LESSER_FANATIC(3112, 3517, 0, SOUTH, 1)

        RICHARD_2200(3117, 3518, 0, NORTH, 0)

        IRON_MAN_TUTOR(3086, 3497, 0, SOUTH, 0)

        16060(3087, 3507, 0, NORTH, 0) // Teleport Manager

        EMBLEM_TRADER_12113(3105, 3515, 0, SOUTH, 2)

        BETA_JACMOB(3108, 3468, 0, NORTH, 2)

        LUNA(3115, 3518, 0, SOUTH, 3)

        NULODION(3107, 3494, 0, SOUTH, 1)

        IMP_5007(3073, 3493, 0, SOUTH, 50)
        IMP_5007(3078, 3461, 0, SOUTH, 50)
        IMP_5007(3104, 3486, 0, SOUTH, 50)

        PEKSA(3078, 3420, 0, SOUTH, 2)

        PROBITA(3092, 3507, 0, NORTH, 0)

        JOSSIK(3087, 3514, 0, SOUTH, 0)

        GHOMMAL(3092, 3497, 0, SOUTH, 0)

        SIGMUND_THE_MERCHANT(3088, 3511, 0, SOUTH, 2)
        16009(3091, 3511, 0, SOUTH, 2) // Frank
        16034(3094, 3512, 0, WEST, 0) // Herblore Secondaries

        996(2717, 5310, 0, SOUTH, 3)

        ICEFIEND(3007, 3487, 0, NORTH)
        ICEFIEND(3009, 3492, 0, NORTH)
        ICEFIEND(3007, 3484, 0, NORTH)
        ICEFIEND(3010, 3480, 0, NORTH)
        ICEFIEND(3011, 3485, 0, NORTH)
        ICEFIEND(3011, 3491, 0, NORTH)

        COMBAT_DUMMY_16019(3108, 3491, 0, SOUTH)
        UNDEAD_COMBAT_DUMMY_16020(3110, 3491, 0, SOUTH)
        COMBAT_DUMMY_16019(3112, 3491, 0, SOUTH)

        COMBAT_DUMMY_16019(3114, 3489, 0, WEST)
        UNDEAD_COMBAT_DUMMY_16020(3114, 3487, 0, WEST)
        COMBAT_DUMMY_16019(3114, 3485, 0, WEST)

        DUSURI(3106, 3502, 0, NORTH, 2)

        MADAM_SIKARO(3152, 3642, 0, SOUTH, 0)

        PONG(2593, 3271, 0, EAST, 0)
        DRIFTER(1652, 11681, 0, EAST, 3)
    }
}
