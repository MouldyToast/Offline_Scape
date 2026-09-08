package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12860 : NPCSpawnsScript() {

    init {
        LAVA_DRAGON(3212, 3846, 0, SOUTH, 4)
        WILLIAM(3218, 3879, 0, SOUTH, 2)
    }
}
