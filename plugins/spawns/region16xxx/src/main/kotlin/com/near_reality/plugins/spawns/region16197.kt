package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region16197 : NPCSpawnsScript() {

    init {
        TORRMENTED_DEMON(4040, 4420, 0, SOUTH, 5)

        TORRMENTED_DEMON(4038, 4449, 0, SOUTH, 5)

        TORRMENTED_DEMON(4070, 4427, 0, SOUTH, 5)
        TORRMENTED_DEMON(4079, 4431, 0, SOUTH, 5)
    }
}
