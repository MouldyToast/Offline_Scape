package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region5535 : NPCSpawnsScript() {

    init {
        SULPHUR_LIZARD(1346, 10182, 1, SOUTH, 9)
        DRAKE_8612(1347, 10229, 1, SOUTH, 6)
        DRAKE_8612(1349, 10236, 1, SOUTH, 6)
        DRAKE_8612(1356, 10239, 1, SOUTH, 6)
        HELLHOUND(1344, 10199, 2, SOUTH, 7)
        HELLHOUND(1345, 10204, 2, SOUTH, 7)
        HELLHOUND(1349, 10197, 2, SOUTH, 7)
        HELLHOUND(1351, 10194, 2, SOUTH, 7)
        HELLHOUND(1354, 10198, 2, SOUTH, 7)
    }
}
