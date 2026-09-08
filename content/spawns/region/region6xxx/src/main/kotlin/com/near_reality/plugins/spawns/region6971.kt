package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region6971 : NPCSpawnsScript() {

    init {
        FISHING_SPOT_7470(1747, 3802, 0, SOUTH, 0)
        FISHING_SPOT_7470(1748, 3802, 0, SOUTH, 0)
        SWARM(1761, 3790, 0, SOUTH, 6)
        FISHING_SPOT_7469(1761, 3796, 0, SOUTH, 0)
        FISHING_SPOT_7469(1764, 3796, 0, SOUTH, 0)
        FISHING_SPOT_7469(1765, 3796, 0, SOUTH, 0)
        SWARM(1766, 3777, 0, SOUTH, 6)
        GUARD_11094(1782, 3778, 0, SOUTH, 5)
        GUARD_11102(1781, 3777, 1, SOUTH, 5)
        HEAD_GUARD_11097(1786, 3780, 1, SOUTH, 5)
    }
}
