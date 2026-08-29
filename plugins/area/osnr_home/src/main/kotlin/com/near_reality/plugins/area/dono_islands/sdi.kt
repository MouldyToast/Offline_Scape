package com.near_reality.plugins.area.dono_islands

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Sdi : NPCSpawnsScript() {

    init {
        TOOL_LEPRECHAUN(1640, 2632, 0, SOUTH, 1)
        MASTER_FARMER(1634, 2616, 0, SOUTH, 2)

        16058(1626, 2614, 0, EAST, 0)

        16031(1610, 2611, 0, SOUTH, 1)
    }
}
