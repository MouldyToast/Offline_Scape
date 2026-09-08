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
class WebWeaverBowCreationPlugin : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        if (player.skills.getLevel(SkillConstants.FLETCHING) < 85) {
            player.sendMessage("You need 85 Fletching in order to combine these items.")
            return
        }

        val craws = if (from.id == FANGS_OF_VENENATIS) to else from
        player.inventory.deleteItem(Item(FANGS_OF_VENENATIS))
        player.inventory.deleteItem(craws)

        val webweaver = Item(if (craws.id == CRAWS_BOW_U) WEBWEAVER_BOW_U_27652 else WEBWEAVER_BOW_27655)
        webweaver.charges = craws.charges
        player.inventory.addItem(webweaver)

        player.dialogue {
            item(webweaver, "You combine the Craw's bow and the Fangs of Venenatis together to form the Webweaver bow.")
        }
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(CRAWS_BOW_U, FANGS_OF_VENENATIS),
            ItemOnItemAction.ItemPair.of(CRAWS_BOW, FANGS_OF_VENENATIS)
        )
    }
}
