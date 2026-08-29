package com.near_reality.plugins.area.osnr_home.obj

import com.near_reality.game.item.CustomObjectId.WELL_OF_SACRIFICE_BLUE
import com.near_reality.game.item.CustomObjectId.WELL_OF_SACRIFICE_GREEN
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

class RemnantForge : ObjectAction {
    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        if (option == "Breakdown items") {
            // If the account has a PIN & Required unlocking; STOP HERE
            if (player!!.bankPin.requiresVerification(player) { GameInterface.REMNANT_EXCHANGE.open(player) }) return
            GameInterface.REMNANT_EXCHANGE.open(player)
        }
        if (option == "Buy perks") {
            // If the account has a PIN & Required unlocking; STOP HERE
            if (player!!.bankPin.requiresVerification(player) { GameInterface.PVPW_PERKS.open(player) }) return
            GameInterface.PVPW_PERKS.open(player)
        }
        if (option == "Item values") {
            GameInterface.PVPW_PRICES.open(player)
        }
        if (option == "Open store") {
            // If the account has a PIN & Required unlocking; STOP HERE
            if (player!!.bankPin.requiresVerification(player) { player.openShop("Remnant Shop") }) return
            player.openShop("Remnant Shop")
        }
    }

    override fun getObjects(): Array<Int> {
        return arrayOf(WELL_OF_SACRIFICE_BLUE, WELL_OF_SACRIFICE_GREEN)
    }
}