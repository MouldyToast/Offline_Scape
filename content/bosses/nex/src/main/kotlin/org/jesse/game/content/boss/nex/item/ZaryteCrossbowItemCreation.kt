package org.jesse.game.content.boss.nex.item

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Represents an [ItemOnItemAction] that is used to create a Zaryte Crossbow.
 */
@Suppress("unused")
class ZaryteCrossbowItemCreation : ItemOnItemAction {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {

        val inventory = player.inventory

        val shards = inventory.getAny(NIHIL_SHARD)
        if ((shards?.amount ?: 0) < 250) {
            player.dialogue { item(Item(NIHIL_SHARD), FAILED_MESSAGE) }
            return
        }

        player.options("Create a Zaryte crossbow?") {
            "Yes" {
                val ingredients = arrayOf(
                    Item(NIHIL_SHARD, 250),
                    Item(NIHIL_HORN),
                    Item(ARMADYL_CROSSBOW)
                )
                if (inventory.deleteItems(*ingredients).result == RequestResult.SUCCESS) {
                    val zaryteCrossbow = Item(ZARYTE_CROSSBOW, 1)
                    inventory.addItem(zaryteCrossbow)
                    player.dialogue { item(zaryteCrossbow, SUCCESS_MESSAGE) }
                }
            }
            "No" {}
        }
    }

    override fun getItems(): IntArray = intArrayOf(ARMADYL_CROSSBOW, NIHIL_HORN)

    private companion object {
        const val FAILED_MESSAGE = "To create a Zaryte crossbow, " +
                "you need a Nihil horn, an Armadyl crossbow and 250 Nihil shards."
        const val SUCCESS_MESSAGE = "You successfully combine a Nihil horn, " +
                "an Armadyl crossbow and 250 Nihil shards to create a Zaryte crossbow."
    }
}
