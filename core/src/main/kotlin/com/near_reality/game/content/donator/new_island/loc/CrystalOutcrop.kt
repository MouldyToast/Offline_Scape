package com.near_reality.game.content.donator.new_island.loc

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-29
 */
class CrystalOutcrop: ObjectAction {

    private val requiredTool: Item = Item(ItemId.CHISEL)
    private val product: Item = Item(ItemId.SALVE_SHARD)

    override fun handleObjectAction(player: Player?, crystal: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return; crystal ?: return
        if (!player.inventory.containsItem(requiredTool)) {
            player.sendMessage("You need a chisel to harvest this crystal outcrop.")
            return
        }
        player.lock(2)
        player.faceObject(crystal)
        player.animation = Animation.GRAB
        player.inventory.addOrDrop(product)

    }

    override fun getObjects(): Array<Any> =
        arrayOf(
            ObjectId.CRYSTAL_OUTCROP,
            ObjectId.CRYSTAL_OUTCROP_4927,
            ObjectId.CRYSTAL_OUTCROP_4928,
            ObjectId.CRYSTAL_OUTCROP_36217,
            ObjectId.CRYSTAL_OUTCROP_36218,
            ObjectId.CRYSTAL_OUTCROP_36604
        )
}