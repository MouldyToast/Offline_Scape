package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region13112 : NPCSpawnsScript() {

    init {
        ENT(3299, 3586, 0, SOUTH, 11)
        ENT(3301, 3597, 0, SOUTH, 11)
        ENT(3305, 3614, 0, SOUTH, 11)
        ENT(3306, 3631, 0, SOUTH, 11)
    }
}
