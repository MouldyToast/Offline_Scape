package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region7508 : NPCSpawnsScript() {

    init {
        MAJOR_COLLECT(1883, 5401, 0, SOUTH, 5)
        MAJOR_HEAL(1886, 5401, 0, SOUTH, 5)
        MAJOR_ATTACK(1889, 5401, 0, SOUTH, 5)
        MAJOR_DEFEND(1892, 5401, 0, SOUTH, 5)
        EGG_LAUNCHER(1877, 5410, 0, SOUTH, 0)
    }
}
