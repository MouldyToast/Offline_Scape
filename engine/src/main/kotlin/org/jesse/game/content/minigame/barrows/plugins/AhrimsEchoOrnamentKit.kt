package org.jesse.game.content.minigame.barrows.plugins

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.world.entity.player.Player

class AhrimsEchoOrnamentKit : ItemOnItemAction {

    enum class OrnamentData(val ornament: Int, val base: Int, val product: Int) {
        ECHO_AHRIM_STAFF(ECHO_AHRIMS_ORNAMENT_KIT, AHRIMS_STAFF, ECHO_AHRIMS_STAFF),
        ECHO_AHRIM_HOOD(ECHO_AHRIMS_ORNAMENT_KIT, AHRIMS_HOOD, ECHO_AHRIMS_HOOD),
        ECHO_AHRIM_ROBETOP(ECHO_AHRIMS_ORNAMENT_KIT, AHRIMS_ROBETOP, ECHO_AHRIMS_ROBETOP),
        ECHO_AHRIM_ROBESKIRT(ECHO_AHRIMS_ORNAMENT_KIT, AHRIMS_ROBESKIRT, ECHO_AHRIMS_ROBESKIRT),
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
            ItemOnItemAction.ItemPair(ECHO_AHRIMS_ORNAMENT_KIT, AHRIMS_STAFF),
            ItemOnItemAction.ItemPair(ECHO_AHRIMS_ORNAMENT_KIT, AHRIMS_HOOD),
            ItemOnItemAction.ItemPair(ECHO_AHRIMS_ORNAMENT_KIT, AHRIMS_ROBETOP),
            ItemOnItemAction.ItemPair(ECHO_AHRIMS_ORNAMENT_KIT, AHRIMS_ROBESKIRT),
        )
    }

}
