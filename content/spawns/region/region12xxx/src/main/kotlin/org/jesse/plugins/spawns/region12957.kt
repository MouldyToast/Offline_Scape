package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12957 : NPCSpawnsScript() {

    init {
        REVENANT_ORK(3209, 10097, 0, SOUTH, 2)
        REVENANT_ORK(3220, 10095, 0, SOUTH, 2)
        REVENANT_GOBLIN(3225, 10070, 0, SOUTH, 2)
        REVENANT_GOBLIN(3224, 10074, 0, SOUTH, 2)
        REVENANT_HOBGOBLIN(3244, 10101, 0, SOUTH, 2)
        REVENANT_HOBGOBLIN(3240, 10092, 0, SOUTH, 2)
        REVENANT_IMP(3200, 10070, 0, SOUTH, 2)
        REVENANT_GOBLIN(3220, 10064, 0, SOUTH, 2)
        REVENANT_HOBGOBLIN(3243, 10083, 0, SOUTH, 2)
    }
}
