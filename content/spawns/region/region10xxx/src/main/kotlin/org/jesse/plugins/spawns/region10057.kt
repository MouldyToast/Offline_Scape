package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region10057 : NPCSpawnsScript() {

    init {
        CHAMBER_GUARDIAN(2508, 4696, 0, SOUTH, 1)
        GUNDAI(2534, 4714, 0, SOUTH, 2)
        LUNDAIL(2534, 4719, 0, SOUTH, 0)
        KOLODION(2541, 4715, 0, SOUTH, 2)
    }
}
