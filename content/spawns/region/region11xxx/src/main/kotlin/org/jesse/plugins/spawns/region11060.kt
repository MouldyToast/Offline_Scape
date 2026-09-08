package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region11060 : NPCSpawnsScript() {

    init {
        FRINCOS(2808, 3342, 0, SOUTH, 2)
        AUGUSTE(2808, 3355, 0, SOUTH, 3)
        BLACK_BEAR(2809, 3376, 0, SOUTH, 8)
        TOOL_LEPRECHAUN(2812, 3332, 0, SOUTH, 0)
        FRANCIS(2814, 3337, 0, SOUTH, 4)
    }
}
