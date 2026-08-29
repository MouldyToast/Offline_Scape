package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region12439 : NPCSpawnsScript() {

    init {
        ZOMBIE_39(3086, 9674, 0, SOUTH, 8)
        ZOMBIE_56(3088, 9672, 0, SOUTH, 7)
        ZOMBIE_58(3096, 9672, 0, SOUTH, 6)
        SKELETON_79(3101, 9671, 0, SOUTH, 8)
        SKELETON_77(3109, 9675, 0, SOUTH, 7)
        RUANTUN(3112, 9690, 0, WEST, 0)
        SKELETON(3115, 9674, 0, SOUTH, 7)
        SKELETON_72(3123, 9669, 0, SOUTH, 8)
    }
}
