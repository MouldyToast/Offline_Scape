package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region9012 : NPCSpawnsScript() {

    init {
        RABBIT_3420(2298, 3383, 0, SOUTH, 4)
        9012(2299, 3328, 0, SOUTH, 0)
        9242(2299, 3329, 0, SOUTH, 5)
        RABBIT_3420(2300, 3380, 0, SOUTH, 4)
        RABBIT_3420(2301, 3382, 0, SOUTH, 4)
        LENNY(2302, 3380, 0, SOUTH, 5)
        RABBIT_3420(2302, 3383, 0, SOUTH, 4)
        RABBIT_3420(2303, 3381, 0, SOUTH, 4)
        9233(2299, 3328, 0, SOUTH, 5)
        9233(2299, 3328, 0, SOUTH, 5)
    }
}
