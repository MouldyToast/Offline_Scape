package com.near_reality.game.model.item.submenu.impl.slayer_ring

import com.near_reality.game.model.item.submenu.ISubMenuAction
import com.near_reality.game.model.item.submenu.NecklaceTeleport
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-10
 */
class SlayerRingTeleportAction(
    private val stronghold: Int = 0,
    private val slayerTower: Int = 1,
    private val fremennikDungeon: Int = 2,
    private val tarnsLair: Int = 3,
    private val darkBeasts: Int = 4,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]!!
        val tallyTeleport = NecklaceTeleport(destination)
        tallyTeleport.teleport(player)
    }

    private val getSelectedTeleportLocation = mapOf(
        stronghold to Location(2431,3422, 0),
        slayerTower to Location(3421,3537, 0),
        fremennikDungeon to Location(2802,9999, 0),
        tarnsLair to Location(3185,4601, 0),
        darkBeasts to Location(2028,4636, 0)
    )
}