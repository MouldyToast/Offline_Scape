package org.jesse.game.content.araxxor.rewards

import org.jesse.game.item.Item


/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-29
 */
interface AraxxorReward {
    fun rollForItem(): Item?
}