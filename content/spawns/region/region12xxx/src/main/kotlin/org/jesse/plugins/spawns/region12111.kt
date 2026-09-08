package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12111 : NPCSpawnsScript() {

    init {
        ROGUE_GUARD_3191(3017, 5060, 1, SOUTH, 5)
        ROGUE_GUARD(3018, 5072, 1, SOUTH, 5)
        SPIN_BLADES(3044, 5105, 1, SOUTH, 5)
        SPIN_BLADES_3196(3059, 5090, 1, SOUTH, 5)
    }
}
