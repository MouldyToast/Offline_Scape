package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

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
