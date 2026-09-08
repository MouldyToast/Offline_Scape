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
class DigsitePendantRubAction(
    private val digsite: Int = 0,
    private val fossilIsland: Int = 1,
    private val lithkrenDungeon: Int = 2,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]!!
        val tallyTeleport = NecklaceTeleport(destination)
        tallyTeleport.teleport(player)
    }

    private val getSelectedTeleportLocation = mapOf(
        digsite to Location(3341,3445, 0),
        fossilIsland to Location(3761,3869, 1),
        lithkrenDungeon to Location(3549,10456, 0)
    )
}