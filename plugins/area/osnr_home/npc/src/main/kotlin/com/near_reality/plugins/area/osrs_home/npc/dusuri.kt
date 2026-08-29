package com.near_reality.plugins.area.osrs_home.npc

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.npc.actions.*

class DusuriNpcaction : NPCActionScript() {

    init {
        npcs(DUSURI)

        "Talk-to" {
            player.openShop("Dusuri's Star Store")
        }
    }
}
