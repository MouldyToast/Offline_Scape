package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region7509 : NPCSpawnsScript() {

    init {
        PENANCE_RANGER(1874, 5485, 0, SOUTH, 5)
        PENANCE_FIGHTER(1880, 5486, 0, SOUTH, 5)
        MAJOR_HEAL(1882, 5466, 0, SOUTH, 5)
        MAJOR_ATTACK(1882, 5477, 0, SOUTH, 5)
        MAJOR_DEFEND(1891, 5466, 0, SOUTH, 5)
        MAJOR_COLLECT(1891, 5477, 0, SOUTH, 5)
        PENANCE_HEALER(1892, 5486, 0, SOUTH, 5)
        PENANCE_RUNNER(1898, 5485, 0, SOUTH, 5)
        EGG_LAUNCHER(1877, 5474, 0, SOUTH, 0)
        EGG_LAUNCHER(1896, 5474, 0, SOUTH, 0)
    }
}
