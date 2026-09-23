package org.jesse.game.content.wilderness.revenant.item

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.model.item.PairedItemOnItemPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.entity.player.dialogue.dialogue

/**
 * @author Andys1814
 */
@Suppress("unused")
class UrsineChainMaceCreationPlugin : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        if (player.skills.getLevel(SkillConstants.SMITHING) < 85) {
            player.sendMessage("You need 85 Smithing in order to combine these items.")
            return
        }

        val viggoras = if (from.id == CLAWS_OF_CALLISTO) to else from
        player.inventory.deleteItem(Item(CLAWS_OF_CALLISTO))
        player.inventory.deleteItem(viggoras)

        val ursine =
            Item(if (viggoras.id == VIGGORAS_CHAINMACE_U) URSINE_CHAINMACE_U else URSINE_CHAINMACE)
        ursine.charges = viggoras.charges
        player.inventory.addItem(ursine)

        player.dialogue {
            item(ursine, "You combine the Viggora's chainmace and the Claws of Callisto together to form the Ursine chainmace.")
        }
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(VIGGORAS_CHAINMACE_U, CLAWS_OF_CALLISTO),
            ItemOnItemAction.ItemPair.of(VIGGORAS_CHAINMACE, CLAWS_OF_CALLISTO)
        )
    }
}
