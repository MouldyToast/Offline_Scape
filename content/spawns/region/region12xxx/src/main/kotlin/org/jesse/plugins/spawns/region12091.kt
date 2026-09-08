package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12091 : NPCSpawnsScript() {

    init {
        KING_SCORPION(3070, 3829, 0, SOUTH, 8)
        KING_SCORPION(3070, 3836, 0, SOUTH, 8)
        FISHING_SPOT_6784(3070, 3839, 0, SOUTH, 5)
    }
}
