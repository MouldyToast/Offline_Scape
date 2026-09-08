package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region5948 : NPCSpawnsScript() {

    init {
        OPERATOR_7073(1476, 3864, 0, EAST, 0)
        OPERATOR_7073(1479, 3874, 0, EAST, 0)
        OPERATOR(1497, 3864, 0, EAST, 0)
        OPERATOR_7073(1498, 3871, 0, EAST, 0)
        BLASTED_ORE(1479, 3873, 0, SOUTH, 5)
        BLASTED_ORE(1498, 3870, 0, SOUTH, 5)
        BLASTED_ORE(1479, 3873, 0, SOUTH, 5)
        BLASTED_ORE(1498, 3870, 0, SOUTH, 5)
        BLASTED_ORE(1477, 3864, 0, SOUTH, 5)
        BLASTED_ORE(1479, 3873, 0, SOUTH, 5)
        BLASTED_ORE(1498, 3870, 0, SOUTH, 5)
    }
}
