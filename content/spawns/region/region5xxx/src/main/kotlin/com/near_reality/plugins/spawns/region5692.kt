package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region5692 : NPCSpawnsScript() {

    init {
        MINE_SUPERVISOR(1426, 3848, 0, SOUTH, 2)
        MINE_SUPERVISOR_7076(1442, 3843, 0, SOUTH, 4)
        TOOTHY(1452, 3858, 0, SOUTH, 2)
    }
}
