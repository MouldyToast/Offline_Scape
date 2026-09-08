package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region13116 : NPCSpawnsScript() {

    init {
        GREATER_DEMON_2028(3282, 3880, 0, SOUTH, 4)
        GREATER_DEMON_2029(3287, 3894, 0, SOUTH, 7)
        GREATER_DEMON_2027(3296, 3872, 0, SOUTH, 2)
        GREATER_DEMON_2026(3304, 3886, 0, SOUTH, 4)
        LESSER_DEMON(3311, 3845, 0, SOUTH, 4)
        LESSER_DEMON_2018(3324, 3856, 0, SOUTH, 8)
    }
}
