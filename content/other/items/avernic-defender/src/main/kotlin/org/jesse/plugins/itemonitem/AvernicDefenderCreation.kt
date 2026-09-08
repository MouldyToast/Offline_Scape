package org.jesse.plugins.itemonitem

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Handles the creation of an Avernic defender by using its hilt on a Dragon defender.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class AvernicDefenderCreation : ItemOnItemAction {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        player.dialogue {
            plain("WARNING: You will not be able to re-obtain the Avernic hilt after " +
                    "combining it with a dragon defender.")
            options("Proceed regardless?") {
                "Yes." {
                    val inventory = player.inventory
                    if (inventory.deleteItems(from, to).result == RequestResult.SUCCESS)
                        inventory.addItem(AVERNIC_DEFENDER, 1)
                }
                "No." {}
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(AVERNIC_DEFENDER_HILT, DRAGON_DEFENDER)
}
