package org.jesse.game.content.elven.item

import org.jesse.game.content.crystal.CRYSTAL_SHARD
import org.jesse.game.content.crystal.CRYSTAL_SHARD_CHARGES_RATIO
import org.jesse.game.content.crystal.recipes.CrystalChargeable
import org.jesse.game.content.elven.produce
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction.ItemPair
import org.jesse.game.model.item.PairedItemOnItemPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Handles the charging of crystal items.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class ChargeCrystalItem : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val (crystalItem, slot) = if (from.id == CRYSTAL_SHARD) to to toSlot else from to fromSlot

        val crystalWearable = CrystalChargeable.all.find {
            it.productItemId == crystalItem.id || it.inactiveId == crystalItem.id
        }!!
        val inactive = crystalItem.id == crystalWearable.inactiveId

        if(inactive) {
            player.inventory.set(slot, crystalWearable.produce())
            return
        }
    }

    override fun getMatchingPairs() = CrystalChargeable.all
        .flatMap {
            listOf(
                ItemPair.of(it.productItemId, CRYSTAL_SHARD),
                ItemPair.of(it.inactiveId, CRYSTAL_SHARD)
            )
        }
        .toTypedArray()
}
