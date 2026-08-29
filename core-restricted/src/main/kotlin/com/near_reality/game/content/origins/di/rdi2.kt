package com.near_reality.game.content.origins.di

import com.near_reality.game.item.CustomNpcId
import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Rdi2 : NPCSpawnsScript() {

    init {
        CustomNpcId.NOMAD(2909, 5428, 0, walkRadius = 5)
        CustomNpcId.NOMAD(2922, 5420, 0, walkRadius = 5)

        NEZIKCHENED_6379(2923, 5410, 0, walkRadius = 0)
        NEZIKCHENED_6379(2923, 5406, 0, walkRadius = 0)
        NEZIKCHENED_6379(2923, 5402, 0, walkRadius = 0)
        NEZIKCHENED_6379(2923, 5398, 0, walkRadius = 0)
        NEZIKCHENED_6379(2928, 5410, 0, walkRadius = 0)
        NEZIKCHENED_6379(2928, 5406, 0, walkRadius = 0)
        NEZIKCHENED_6379(2928, 5402, 0, walkRadius = 0)
        NEZIKCHENED_6379(2928, 5398, 0, walkRadius = 0)
        NEZIKCHENED_6379(2933, 5410, 0, walkRadius = 0)
        NEZIKCHENED_6379(2933, 5406, 0, walkRadius = 0)
        NEZIKCHENED_6379(2933, 5402, 0, walkRadius = 0)
        NEZIKCHENED_6379(2933, 5398, 0, walkRadius = 0)

        BALANCE_ELEMENTAL_MELEE(2891, 5418, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_RANGED(2899, 5418, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_MAGIC(2895, 5415, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_RANGED(2891, 5412, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_MELEE(2899, 5412, 0, walkRadius = 3)
    }
}
