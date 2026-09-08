package org.jesse.game.world.entity.player.container.impl

import org.jesse.game.world.entity.player.ironGroupTradeAddItemCheck
import org.jesse.game.item.Item
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.impl.Trade

/**
 * Check whether the [player] can add the [item] to this [Trade].
 *
 * @return `true` if the item can be added, `false` otherwise.
 */
fun Trade.canAddItem(player: Player, item: Item): Boolean
    = player.ironGroupTradeAddItemCheck?.invoke(this, item)?:true
