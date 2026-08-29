package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region8027 : NPCSpawnsScript() {

    init {
        BUTTERFLY_238(1988, 5870, 0, SOUTH, 0)
        SQUIRREL(1992, 5854, 0, SOUTH, 8)
        BUTTERFLY_238(2017, 5884, 0, SOUTH, 0)
        BUTTERFLY_238(2025, 5852, 0, SOUTH, 0)
        SQUIRREL_1418(2035, 5877, 0, SOUTH, 10)
        MOSS_GIANT_2091(2035, 5887, 0, SOUTH, 4)
        BIRD_10541(2042, 5874, 0, SOUTH, 5)
    }
}
