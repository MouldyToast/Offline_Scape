package org.jesse.game.content.tormented_demon.items

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.model.item.PairedItemOnItemPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import java.util.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-17
 */
class BurningClaws : PairedItemOnItemPlugin {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        if (player == null) return
        if (Objects.equals(fromSlot, toSlot)) return
        val clawA = player.inventory.getItem(toSlot)
        val clawB = player.inventory.getItem(fromSlot)
        if (player.inventory.deleteItems(clawA, clawB).result == RequestResult.SUCCESS) {
            player.inventory.addItem(Item(BURNING_CLAWS, 1))
            player.dialogue {
                item(BURNING_CLAWS, "You bring the two claws together and combine them.")
                player("That was easy.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(BURNING_CLAW)

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> =
        arrayOf(ItemOnItemAction.ItemPair(BURNING_CLAW, BURNING_CLAW))
}