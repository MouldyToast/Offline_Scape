package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region13614 : NPCSpawnsScript() {

    init {
        JACKAL(3400, 2997, 0, SOUTH, 11)
        JACKAL(3402, 2999, 0, SOUTH, 11)
        SNAKE_3544(3403, 2963, 0, SOUTH, 3)
        JACKAL(3403, 2997, 0, SOUTH, 11)
        SNAKE_3544(3429, 2976, 0, SOUTH, 3)
        JACKAL(3445, 2993, 0, SOUTH, 11)
        JACKAL(3446, 2992, 0, SOUTH, 11)
        JACKAL(3448, 2991, 0, SOUTH, 11)
        JACKAL(3448, 2994, 0, SOUTH, 11)
    }
}
