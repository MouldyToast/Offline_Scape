package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region15000 : NPCSpawnsScript() {

    init {
        BAT(3727, 9755, 1, SOUTH, 23)
        9824(3727, 9759, 1, SOUTH, 5)
        BAT(3729, 9763, 1, SOUTH, 23)
        SISTER_SALOHCIN(3742, 9768, 1, SOUTH, 5)
    }
}
