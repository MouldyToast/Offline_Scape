package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12090 : NPCSpawnsScript() {

    init {
        CHAOS_DWARF(3019, 3760, 0, SOUTH, 14)
        CHAOS_DWARF(3019, 3766, 0, SOUTH, 14)
        CHAOS_DWARF(3021, 3755, 0, SOUTH, 14)
        CHAOS_DWARF(3025, 3763, 0, SOUTH, 14)
    }
}
