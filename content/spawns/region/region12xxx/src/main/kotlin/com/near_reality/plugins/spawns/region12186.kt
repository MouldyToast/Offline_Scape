package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region12186 : NPCSpawnsScript() {

    init {
        HELLRAT_BEHEMOTH(3069, 9895, 0, SOUTH, 0)
        HELLRAT_BEHEMOTH(3070, 9881, 0, SOUTH, 0)
    }
}
