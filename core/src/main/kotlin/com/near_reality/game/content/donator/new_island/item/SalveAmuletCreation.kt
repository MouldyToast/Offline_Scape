package com.near_reality.game.content.donator.new_island.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-29
 */
class SalveAmuletCreation: ItemOnItemAction {

    val product = Item(SALVE_AMULET)

    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val shard = if (from.id == SALVE_SHARD) from else to
        val wool = if (to.id == BALL_OF_WOOL) to else from
        if (player.inventory.deleteItems(shard, wool).result == RequestResult.SUCCESS)
            player.inventory.addItem(product)
    }

    override fun getItems(): IntArray =
        intArrayOf(SALVE_SHARD, BALL_OF_WOOL)
}