package com.near_reality.game.content.wilderness.revenant.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player

/**
 * @author Tommeh | 9 aug. 2018 | 15:15:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@Suppress("unused")
class BraceletOfEthereumChargePlugin : ItemOnItemAction {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        if (
            (from.id ==  BRACELET_OF_ETHEREUM || to.id >= BRACELET_OF_ETHEREUM_UNCHARGED) &&
            (from.id != REVENANT_ETHER) && (to.id != REVENANT_ETHER)) {
            return
        }
        val bracelet = if (from.id != REVENANT_ETHER) from else to
        val charges = bracelet.charges
        val ether = player.inventory.getAmountOf(REVENANT_ETHER)
        var toCharge = ether
        if (charges + ether > 16_000) {
            toCharge = 16_000 - bracelet.charges
            if (toCharge <= 0) {
                player.sendMessage("Your bracelet is already fully charged.")
                return
            }
            player.inventory.deleteItem(REVENANT_ETHER, toCharge)
            player.sendMessage("You charge the braclet with $toCharge charges.")
        } else {
            player.inventory.deleteItem(REVENANT_ETHER, ether)
            player.sendMessage("You charge the bracelet with $ether charges.")
        }
        bracelet.charges = bracelet.charges + toCharge
        if (bracelet.id == BRACELET_OF_ETHEREUM_UNCHARGED) {
            bracelet.id = BRACELET_OF_ETHEREUM
            player.inventory.refresh(fromSlot, toSlot)
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(REVENANT_ETHER, BRACELET_OF_ETHEREUM, BRACELET_OF_ETHEREUM_UNCHARGED)
}
