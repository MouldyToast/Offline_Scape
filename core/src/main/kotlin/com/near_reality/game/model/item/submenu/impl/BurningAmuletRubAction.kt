package com.near_reality.game.model.item.submenu.impl

import com.near_reality.game.model.item.submenu.ISubMenuAction
import com.near_reality.game.model.item.submenu.NecklaceTeleport
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */
class BurningAmuletRubAction(
    private val chaosTemple: Int = 0,
    private val banditCamp: Int = 1,
    private val lavaMaze: Int = 2,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]!!
        val tallyTeleport = NecklaceTeleport(destination)
        tallyTeleport.teleport(player)
    }

    private val getSelectedTeleportLocation = mapOf(
        chaosTemple to Location(3234,3634, 0),
        banditCamp to Location(3038,3651, 0),
        lavaMaze to Location(3028,3842, 0)
    )
}