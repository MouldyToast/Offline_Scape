package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12959 : NPCSpawnsScript() {

    init {
        /*REVENANT_KNIGHT*/REVENANT_ORK(3209, 10216, 0, SOUTH, 6)
        /*REVENANT_KNIGHT*/REVENANT_HELLHOUND(3227, 10220, 0, SOUTH, 6)
        REVENANT_IMP(3214, 10195, 0, SOUTH, 3)
        REVENANT_IMP(3218, 10188, 0, SOUTH, 3)
        REVENANT_DRAGON(3237, 10201, 0, SOUTH, 2)
        REVENANT_DRAGON(3230, 10200, 0, SOUTH, 2)
        REVENANT_IMP(3219, 10198, 0, SOUTH, 3)
    }
}
