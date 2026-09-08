package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12186 : NPCSpawnsScript() {

    init {
        HELLRAT_BEHEMOTH(3069, 9895, 0, SOUTH, 0)
        HELLRAT_BEHEMOTH(3070, 9881, 0, SOUTH, 0)
    }
}
