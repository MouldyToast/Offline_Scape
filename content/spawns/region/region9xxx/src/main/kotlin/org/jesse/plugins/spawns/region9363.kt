package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region9363 : NPCSpawnsScript() {

    init {
        SMOKE_DEVIL(2351, 9448, 0, SOUTH, 4)
        SMOKE_DEVIL(2354, 9455, 0, SOUTH, 4)
        SMOKE_DEVIL(2358, 9445, 0, SOUTH, 4)
        THERMONUCLEAR_SMOKE_DEVIL(2360, 9452, 0, SOUTH, 3)
        SMOKE_DEVIL(2367, 9455, 0, SOUTH, 4)
    }
}
