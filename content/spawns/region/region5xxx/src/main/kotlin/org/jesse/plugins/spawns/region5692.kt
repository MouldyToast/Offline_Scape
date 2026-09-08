package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region5692 : NPCSpawnsScript() {

    init {
        MINE_SUPERVISOR(1426, 3848, 0, SOUTH, 2)
        MINE_SUPERVISOR_7076(1442, 3843, 0, SOUTH, 4)
        TOOTHY(1452, 3858, 0, SOUTH, 2)
    }
}
