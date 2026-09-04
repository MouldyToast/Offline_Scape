package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region5442 : NPCSpawnsScript() {

    init {
        WARPED_TORTOISE(1352, 4235, 0, SOUTH, 8)
        WARPED_TORTOISE(1360, 4235, 1, SOUTH, 8)
    }
}
