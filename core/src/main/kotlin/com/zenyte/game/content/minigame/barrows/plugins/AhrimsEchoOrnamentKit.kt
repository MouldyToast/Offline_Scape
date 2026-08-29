package com.zenyte.game.content.minigame.barrows.plugins

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player

class AhrimsEchoOrnamentKit : ItemOnItemAction {

    enum class OrnamentData(val ornament: Int, val base: Int, val product: Int) {
        ECHO_AHRIM_STAFF(ItemId.ECHO_AHRIMS_ORNAMENT_KIT, ItemId.AHRIMS_STAFF, ItemId.ECHO_AHRIMS_STAFF),
        ECHO_AHRIM_HOOD(ItemId.ECHO_AHRIMS_ORNAMENT_KIT, ItemId.AHRIMS_HOOD, ItemId.ECHO_AHRIMS_HOOD),
        ECHO_AHRIM_ROBETOP(ItemId.ECHO_AHRIMS_ORNAMENT_KIT, ItemId.AHRIMS_ROBETOP, ItemId.ECHO_AHRIMS_ROBETOP),
        ECHO_AHRIM_ROBESKIRT(ItemId.ECHO_AHRIMS_ORNAMENT_KIT, ItemId.AHRIMS_ROBESKIRT, ItemId.ECHO_AHRIMS_ROBESKIRT),
        ;

        companion object {
            val VALUES: Array<OrnamentData> = entries.toTypedArray()
        }
    }

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        var found: OrnamentData? = null
        for (value in OrnamentData.VALUES) {
            if (value.ornament == from.id && value.base == to.id || value.ornament == to.id && value.base == from.id) {
                found = value
                break
            }
        }

        if (found == null) {
            return
        }

        val inventory = player.inventory
        inventory.deleteItem(fromSlot, from)
        inventory.deleteItem(toSlot, to)
        inventory.addItem(Item(found.product))
        player.sendMessage("The ornament kit attaches itself to the item.")
    }

    override fun getItems(): IntArray? {
        return null
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair(ItemId.ECHO_AHRIMS_ORNAMENT_KIT, ItemId.AHRIMS_STAFF),
            ItemOnItemAction.ItemPair(ItemId.ECHO_AHRIMS_ORNAMENT_KIT, ItemId.AHRIMS_HOOD),
            ItemOnItemAction.ItemPair(ItemId.ECHO_AHRIMS_ORNAMENT_KIT, ItemId.AHRIMS_ROBETOP),
            ItemOnItemAction.ItemPair(ItemId.ECHO_AHRIMS_ORNAMENT_KIT, ItemId.AHRIMS_ROBESKIRT),
        )
    }

}
