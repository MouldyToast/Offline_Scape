package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12702 : NPCSpawnsScript() {

    init {
        REVENANT_DEMON(3162, 10114, 0, SOUTH, 2)
        REVENANT_DEMON(3152, 10113, 0, SOUTH, 2)
        REVENANT_PYREFIEND(3170, 10155, 0, SOUTH, 2)
        REVENANT_PYREFIEND(3181, 10152, 0, SOUTH, 2)
        REVENANT_PYREFIEND(3167, 10165, 0, SOUTH, 2)
    }
}
