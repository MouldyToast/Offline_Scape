package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region16197 : NPCSpawnsScript() {

    init {
        TORRMENTED_DEMON(4040, 4420, 0, SOUTH, 5)

        TORRMENTED_DEMON(4038, 4449, 0, SOUTH, 5)

        TORRMENTED_DEMON(4070, 4427, 0, SOUTH, 5)
        TORRMENTED_DEMON(4079, 4431, 0, SOUTH, 5)
    }
}
