package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region13552 : NPCSpawnsScript() {

    init {
        REVENANT_DRAGON(3370, 15391, 0, SOUTH, 5)
        REVENANT_DARK_BEAST(3360, 15404, 0, SOUTH, 5)
        REVENANT_KNIGHT(3358, 15391, 0, SOUTH, 5)
    }
}
