package org.jesse.game.content.boss.nex.`object`.actions

import org.jesse.game.content.boss.nex.item.BandosOnAncientForge
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.scripts.`object`.actions.ObjectActionScript
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.*

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
