package org.jesse.game.model.item.submenu.impl

import org.jesse.game.model.item.submenu.ISubMenuAction
import org.jesse.game.model.item.submenu.NecklaceTeleport
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */
class NecklaceOfPassageRubAction(
    private val wizardsTower: Int = 0,
    private val theOutpost: Int = 1,
    private val eaglesEyrie: Int = 2,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]!!
        val tallyTeleport = NecklaceTeleport(destination)
        tallyTeleport.teleport(player)
    }

    private val getSelectedTeleportLocation = mapOf(
        wizardsTower to Location(3114,3179, 0),
        theOutpost to Location(2430,3349, 0),
        eaglesEyrie to Location(3405,3158, 0)
    )
}