package com.near_reality.game.model.item.submenu

import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-10
 */
@FunctionalInterface
interface ISubMenuAction {
    fun onAction(player: Player, selectedItemIndex: Int)
}