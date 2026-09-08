package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region12091 : NPCSpawnsScript() {

    init {
        KING_SCORPION(3070, 3829, 0, SOUTH, 8)
        KING_SCORPION(3070, 3836, 0, SOUTH, 8)
        FISHING_SPOT_6784(3070, 3839, 0, SOUTH, 5)
    }
}
