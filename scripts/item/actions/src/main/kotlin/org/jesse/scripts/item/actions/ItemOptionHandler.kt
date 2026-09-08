package org.jesse.scripts.item.actions

import org.jesse.game.item.Item
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.Container

/**
 * @author Jire
 */
data class ItemOptionHandler(
    val player: Player,
    val item: Item,
    val container: Container,
    val slotID: Int
)