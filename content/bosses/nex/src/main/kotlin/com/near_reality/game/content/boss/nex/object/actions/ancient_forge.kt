package com.near_reality.game.content.boss.nex.`object`.actions

import com.near_reality.game.content.boss.nex.item.BandosOnAncientForge
import com.zenyte.game.item.ids.*
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.ObjectId.*
import com.zenyte.game.world.`object`.*

class AncientForgeObjectaction : ObjectActionScript() {

    init {
        ANCIENT_FORGE_42966 {
            val possible = listOfNotNull(
                player.inventory.getAny(BANDOS_CHESTPLATE),
                player.inventory.getAny(BANDOS_TASSETS),
            )
            if (possible.isEmpty()) {
                player.dialogue { plain("You don't have any bandos items to melt.") }
                return@ANCIENT_FORGE_42966
            }
            BandosOnAncientForge().handleItemOnObjectAction(player, possible.first(), -1, obj)
        }
    }
}
