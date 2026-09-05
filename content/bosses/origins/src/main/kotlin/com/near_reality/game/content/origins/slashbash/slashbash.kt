package com.near_reality.game.content.origins.slashbash

import com.near_reality.game.item.CustomNpcId
import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Slashbash : NPCSpawnsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.11.2025
         */

        CustomNpcId.SLASH_BASH(2438, 4395, 0, walkRadius = 5)
        CustomNpcId.SLASH_BASH(2439, 4385, 0, walkRadius = 5)
    }
}
