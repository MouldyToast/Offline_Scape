package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region15162 : NPCSpawnsScript() {

    init {
        FOSSIL_ROCK(3800, 3752, 0, SOUTH, 2)
        CRAB_1553(3801, 3754, 0, SOUTH, 4)
        FOSSIL_ROCK(3802, 3756, 0, SOUTH, 2)
        FOSSIL_ROCK(3806, 3755, 0, SOUTH, 2)
        CRAB_1553(3817, 3755, 0, SOUTH, 4)
        FOSSIL_ROCK(3817, 3760, 0, SOUTH, 2)
    }
}
