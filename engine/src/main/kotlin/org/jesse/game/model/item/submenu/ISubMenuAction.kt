package org.jesse.game.model.item.submenu

import org.jesse.game.world.entity.player.Player

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