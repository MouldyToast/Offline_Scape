package com.near_reality.game.content.araxxor.items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.item.ItemId.AMULET_OF_RANCOUR
import com.zenyte.game.item.ItemId.AMULET_OF_RANCOUR_S
import com.zenyte.game.item.ItemId.AMULET_OF_TORTURE
import com.zenyte.game.item.ItemId.ARAXYTE_FANG

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class RancourAmulet : ItemOnItemAction, ItemPlugin() {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        val fang = player?.inventory?.getItemById(ARAXYTE_FANG)
        val amulet = player?.inventory?.getItemById(AMULET_OF_TORTURE)
        if (fang == null || amulet == null) {
            player?.sendMessage("You are missing pieces to this amulet.")
            return
        }
        player.dialogue {
            plain("Do you wish to use the araxyte fang on your amulet of torture? This process is non-reversible and will consume both items.")
            options("Do you wish to create a amulet of rancour?",
                Dialogue.DialogueOption("Yes.") {
                    if (player.inventory?.deleteItems(fang, amulet)?.result == RequestResult.SUCCESS) {
                        player.inventory?.addItem(Item(AMULET_OF_RANCOUR))
                        player.dialogue { item(AMULET_OF_RANCOUR, "You successfully create a amulet of rancour.") }
                    }
                },
                Dialogue.DialogueOption("No.") { player.interfaceHandler.closeInterfaces() }
            )
        }
    }

    override fun handle() {
        bind("Revert") { player, item, _ -> player.promptItemRevert(item) }
    }

    private fun Player.promptItemRevert(item: Item) {
        dialogue {
            item(Item(AMULET_OF_RANCOUR_S), "Do you wish to revert your amulet of rancour?")
            options("Do you wish to revert your amulet of rancour?",
                Dialogue.DialogueOption("Yes.") {
                    if (replaceItems(item, item.revert()!!))
                        dialogue { item(item.revert(), "You successfully revert your amulet of rancour.") }
                },
                Dialogue.DialogueOption("No.") { interfaceHandler.closeInterfaces() }
            )
        }
    }

    private fun Player.replaceItems(original: Item, replacement: Item): Boolean {
        return inventory.deleteItem(original).result == RequestResult.SUCCESS &&
                inventory.addItem(replacement).result == RequestResult.SUCCESS
    }

    private fun Item.revert(): Item? {
        return if (id == AMULET_OF_RANCOUR_S) Item(AMULET_OF_RANCOUR)
        else null
    }

    override fun getItems(): IntArray =
        intArrayOf(AMULET_OF_RANCOUR_S, AMULET_OF_TORTURE, ARAXYTE_FANG)
}