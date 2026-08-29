package com.near_reality.plugins.item.customs

import com.near_reality.game.content.middleman.middleManController
import com.near_reality.game.item.CustomItemId.*
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-10
 */
class DonatorPinItemAction: ItemPlugin() {

    override fun handle() {
        bind("Redeem") { player, item, _ ->
            if (player.bankPin.requiresVerification(player) { startDialogue(player, item) })
                return@bind
            startDialogue(player, item)
        }
//        bind("Trade") { player, item, _ ->
//            if (player.bankPin.requiresVerification(player) { startMiddleManRequest(player, item) })
//                return@bind
//            startMiddleManRequest(player, item)
//        }
    }

    private fun startMiddleManRequest(player: Player, bond: Item) {
        player.middleManController.openTradeRequestInterface(bond.id, 1, "")
    }

    private fun startDialogue(player: Player, bond: Item) {
        player.dialogueManager.start(DonatorPinRedeemDialogue(player, bond))
    }

    override fun getItems(): IntArray =
        intArrayOf(DONATOR_PIN_5, DONATOR_PIN_10, DONATOR_PIN_25, DONATOR_PIN_35, DONATOR_PIN_50, DONATOR_PIN_100)
}