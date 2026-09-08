package org.jesse.game.content.gauntlet.item.actions

import org.jesse.game.content.gauntlet.GauntletStage
import org.jesse.game.content.gauntlet.gauntlet
import org.jesse.game.world.entity.Location
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class TeleportCrystalItemaction : ItemActionScript() {

    init {
        items(TELEPORT_CRYSTAL, CORRUPTED_TELEPORT_CRYSTAL)

        "Activate" {
            val gauntlet = player.gauntlet

            when {
                gauntlet == null ->
                    player.sendMessage("This item cannot be used outside of the Gauntlet.")
                gauntlet.stage == GauntletStage.BOSS ->
                    player.sendMessage("That won't help you now. At this point in the Gauntlet, you win or die.")
                else -> {
                    player.inventory.deleteItem(item.id, 1)
                    val map = gauntlet.map
                    val x = map.getBaseXForNode(map.startingRoomX)
                    val y = map.getBaseYForNode(map.startingRoomY)
                    val teleport = GauntletCrystalTeleport(Location(x + 4, y + 4, 1))
                    teleport.teleport(player)
                }
            }
        }
    }
}
