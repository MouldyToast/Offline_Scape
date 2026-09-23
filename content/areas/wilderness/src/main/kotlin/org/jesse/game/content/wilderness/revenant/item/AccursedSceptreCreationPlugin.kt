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
class AccursedSceptreCreationPlugin : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        if (player.skills.getLevel(SkillConstants.CRAFTING) < 85) {
            player.sendMessage("You need 85 Crafting in order to combine these items.")
            return
        }

        val thammarons = if (from.id == SKULL_OF_VETION) to else from
        player.inventory.deleteItem(Item(SKULL_OF_VETION))
        player.inventory.deleteItem(thammarons)

        val accursed = Item(
            when (thammarons.id) {
                THAMMARONS_SCEPTRE_U -> ACCURSED_SCEPTRE_U
                THAMMARONS_SCEPTRE_AU -> ACCURSED_SCEPTRE_AU
                THAMMARONS_SCEPTRE -> ACCURSED_SCEPTRE
                THAMMARONS_SCEPTRE_A -> ACCURSED_SCEPTRE_A
                else -> {
                    player.sendDeveloperMessage("Invalid item id: ${thammarons.id}")
                    return
                }
            }
        )
        accursed.charges = thammarons.charges
        player.inventory.addItem(accursed)

        player.dialogue {
            item(
                accursed,
                "You combine the Thammaron's sceptre and the Skull of Vet'ion together to form the Accursed Sceptre."
            )
        }
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(THAMMARONS_SCEPTRE_U, SKULL_OF_VETION),
            ItemOnItemAction.ItemPair.of(THAMMARONS_SCEPTRE_AU, SKULL_OF_VETION),
            ItemOnItemAction.ItemPair.of(THAMMARONS_SCEPTRE, SKULL_OF_VETION),
            ItemOnItemAction.ItemPair.of(THAMMARONS_SCEPTRE_A, SKULL_OF_VETION)
        )
    }
}
