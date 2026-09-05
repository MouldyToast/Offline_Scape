package com.near_reality.game.content.origins.nomad

import com.near_reality.game.item.CustomNpcId
import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Nomad : NPCSpawnsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.11.2025
         */

        CustomNpcId.NOMAD(3205, 3877, 0, walkRadius = 30)
    }
}
