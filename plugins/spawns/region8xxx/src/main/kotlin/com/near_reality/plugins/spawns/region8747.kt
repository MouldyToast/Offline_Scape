package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region8747 : NPCSpawnsScript() {

    init {
        BUTTERFLY_238(2180, 2798, 0, SOUTH, 0)
        SQUIRREL(2184, 2782, 0, SOUTH, 8)
        BUTTERFLY_238(2209, 2812, 0, SOUTH, 0)
        BUTTERFLY_238(2217, 2780, 0, SOUTH, 0)
        SQUIRREL_1418(2227, 2805, 0, SOUTH, 10)
        MOSS_GIANT_2091(2227, 2815, 0, SOUTH, 4)
        BIRD_10541(2234, 2802, 0, SOUTH, 5)
    }
}
