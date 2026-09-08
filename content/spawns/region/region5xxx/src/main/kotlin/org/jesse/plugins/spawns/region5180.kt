package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region5180 : NPCSpawnsScript() {

    init {
        GIANT_BAT(1288, 3846, 0, SOUTH, 11)
        GIANT_BAT(1290, 3840, 0, SOUTH, 11)
        GIANT_BAT(1325, 3843, 0, SOUTH, 11)
        GIANT_BAT(1336, 3842, 0, SOUTH, 11)
    }
}
