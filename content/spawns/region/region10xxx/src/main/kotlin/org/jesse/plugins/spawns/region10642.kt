package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region10642 : NPCSpawnsScript() {

    init {
        BUGS(2640, 9391, 0, SOUTH, 3)
        FYCIE(2650, 9393, 0, SOUTH, 5)
    }
}
