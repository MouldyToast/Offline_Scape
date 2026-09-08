package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region10322 : NPCSpawnsScript() {

    init {
        PRIVATE_PALDON(2579, 5290, 0, SOUTH, 3)
        PRIVATE_PIERREB(2583, 5280, 0, SOUTH, 4)
        PRIVATE_PALDO(2589, 5264, 0, SOUTH, 5)
        SERGEANT_SAMBUR(2602, 5280, 0, SOUTH, 5)
        PRIVATE_PENDRON(2604, 5290, 0, SOUTH, 3)
    }
}
