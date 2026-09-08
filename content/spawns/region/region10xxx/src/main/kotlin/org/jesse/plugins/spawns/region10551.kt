package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region10551 : NPCSpawnsScript() {

    init {
        TOOL_LEPRECHAUN(2663, 3521, 0, SOUTH, 0)
        RHONEN(2666, 3530, 0, SOUTH, 2)
    }
}
