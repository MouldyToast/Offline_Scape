package org.jesse.game.model.item.submenu.impl.ring_of_wealth

import org.jesse.game.model.item.submenu.ISubMenuAction
import org.jesse.game.model.item.submenu.NecklaceTeleport
import org.jesse.game.content.grandexchange.GrandExchange
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */
class RingOfWealthRubAction(
    private val miscellania: Int = 0,
    private val grandExchange: Int = 1,
    private val falador: Int = 2,
    private val dondakan: Int = 3,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]!!
        val tallyTeleport = NecklaceTeleport(destination)
        tallyTeleport.teleport(player)
    }

    private val getSelectedTeleportLocation = mapOf(
        miscellania to Location(2534,3862, 0),
        grandExchange to Location(3163,3478, 0),
        falador to Location(2995,3375, 0),
        dondakan to Location(2824,10168, 0)
    )
}