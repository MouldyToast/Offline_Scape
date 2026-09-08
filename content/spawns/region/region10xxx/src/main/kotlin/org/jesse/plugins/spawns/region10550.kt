package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region10550 : NPCSpawnsScript() {

    init {
        GUARD_DOG(2625, 3502, 0, SOUTH, 4)
        GUARD_DOG(2628, 3484, 0, SOUTH, 4)
        GUARD_DOG(2631, 3472, 0, SOUTH, 4)
        GUARD_DOG(2636, 3503, 0, SOUTH, 4)
        GUARD_DOG(2645, 3488, 0, SOUTH, 4)
        FORESTER_4076(2650, 3468, 0, SOUTH, 3)
        GUARD_DOG(2659, 3478, 0, SOUTH, 4)
        GUARD_DOG(2665, 3471, 0, SOUTH, 4)
        GUARD_DOG(2666, 3506, 0, SOUTH, 4)
        GUARD_DOG(2669, 3488, 0, SOUTH, 4)
        GUARD_DOG(2674, 3504, 0, SOUTH, 4)
        TRAMP_382(2683, 3484, 0, SOUTH, 3)
    }
}
