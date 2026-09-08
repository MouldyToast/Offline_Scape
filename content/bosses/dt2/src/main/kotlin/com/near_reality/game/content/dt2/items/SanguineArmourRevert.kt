package com.near_reality.game.content.dt2.items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.item.ItemId.SANGUINE_TORVA_FULL_HELM
import com.zenyte.game.item.ItemId.SANGUINE_TORVA_PLATEBODY
import com.zenyte.game.item.ItemId.SANGUINE_TORVA_PLATELEGS
import com.zenyte.game.item.ItemId.TORVA_FULLHELM
import com.zenyte.game.item.ItemId.TORVA_PLATEBODY
import com.zenyte.game.item.ItemId.TORVA_PLATELEGS

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-11
 */
class SanguineArmourRevert : ItemPlugin() {

    private fun Item.reverted() : Item? {
        return when(this.id) {
            SANGUINE_TORVA_FULL_HELM -> return Item(TORVA_FULLHELM)
            SANGUINE_TORVA_PLATEBODY -> return Item(TORVA_PLATEBODY)
            SANGUINE_TORVA_PLATELEGS -> return Item(TORVA_PLATELEGS)
            else -> null
        }
    }

    override fun handle() {
        bind("Revert") { player: Player, item: Item, _: Int ->
            player.dialogue {
                options("Are you sure you want to revert your ${item.name}?<br>The Blood runes will NOT be returned.") {
                    "Yes." {
                        if (player.inventory.deleteItem(item).result == RequestResult.SUCCESS) {
                            player.inventory.addItem(item.reverted()!!)
                            item(item.reverted()!!, "You successfully revert your ${item.name}.")
                        }
                    }
                    "No." {}
                }
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(SANGUINE_TORVA_FULL_HELM, SANGUINE_TORVA_PLATEBODY, SANGUINE_TORVA_PLATELEGS)
}