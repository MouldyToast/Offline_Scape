package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region16196 : NPCSpawnsScript() {

    init {
        TORRMENTED_DEMON(4038, 4380, 0, SOUTH, 5)
        TORRMENTED_DEMON(4040, 4385, 0, SOUTH, 5)
        TORRMENTED_DEMON(4039, 4391, 0, SOUTH, 5)

        TORRMENTED_DEMON(4088, 4368, 0, SOUTH, 5)
        TORRMENTED_DEMON(4083, 4374, 0, SOUTH, 5)
        TORRMENTED_DEMON(4088, 4380, 0, SOUTH, 5)
    }
}
