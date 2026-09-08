package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region13550 : NPCSpawnsScript() {

    init {
        LAVA_DRAGON(3372, 15264, 0, SOUTH, 3)
        LAVA_DRAGON(3362, 15257, 0, SOUTH, 3)
        REVENANT_ORK(3362, 15267, 0, SOUTH, 3)
        REVENANT_DEMON(3371, 15270, 0, SOUTH, 3)
        REVENANT_CYCLOPS(3358, 15276, 0, SOUTH, 3)
    }
}
