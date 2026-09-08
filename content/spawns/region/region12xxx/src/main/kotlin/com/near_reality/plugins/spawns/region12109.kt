package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region12109 : NPCSpawnsScript() {

    init {
        EMERALD_BENEDICT(3042, 4972, 1, SOUTH, 5)
        BRIAN_ORICHARD(3049, 4977, 1, SOUTH, 5)
        GRACE(3050, 4962, 1, SOUTH, 3)
        MARK(3051, 4963, 1, SOUTH, 0)
    }
}
