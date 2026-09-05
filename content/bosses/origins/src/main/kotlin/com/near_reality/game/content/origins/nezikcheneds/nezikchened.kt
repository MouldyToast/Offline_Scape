package com.near_reality.game.content.origins.nezikcheneds

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Nezikchened : NPCSpawnsScript() {

    init {
        NEZIKCHENED_6379(2981, 3915, 0, walkRadius = 5)
        NEZIKCHENED_6379(2986, 3910, 0, walkRadius = 5)
        NEZIKCHENED_6379(2981, 3907, 0, walkRadius = 5)
        NEZIKCHENED_6379(2971, 3918, 0, walkRadius = 5)


        NEZIKCHENED_6379(2985, 3798, 0, walkRadius = 6)
        NEZIKCHENED_6379(2990, 3804, 0, walkRadius = 6)
        NEZIKCHENED_6379(2995, 3798, 0, walkRadius = 6)
        NEZIKCHENED_6379(2995, 3791, 0, walkRadius = 6)
    }
}
