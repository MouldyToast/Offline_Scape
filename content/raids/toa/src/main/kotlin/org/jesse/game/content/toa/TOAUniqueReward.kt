package org.jesse.game.content.toa

import org.jesse.utils.ChanceItem
import org.jesse.utils.roll
import org.jesse.game.item.ids.*

enum class TOAUniqueReward(
    val item: Int,
    val chance: Double,
    val animation: Int,
    val level: Int,
) {
    LIGHTBEARER(org.jesse.game.item.ids.LIGHTBEARER, 1.0 / 3.429, 9508, 50),
    MASORI_MASK(org.jesse.game.item.ids.MASORI_MASK, 1.0 / 12.0, 9509, 150),
    MASORI_BODY(org.jesse.game.item.ids.MASORI_BODY, 1.0 / 12.0, 9510, 150),
    MASORI_CHAPS(org.jesse.game.item.ids.MASORI_CHAPS, 1.0 / 12.0, 9511, 150),
    OSMUMTENS_FANG(org.jesse.game.item.ids.OSMUMTENS_FANG, 1.0 / 3.429, 9512, 50),
    ELIDINIS_WARD(org.jesse.game.item.ids.ELIDINIS_WARD, 1.0 / 8.0, 9513, 150),
    TUMEKENS_SHADOW(TUMEKENS_SHADOW_UNCHARGED, 1.0 / 24.0, 9514, 150);

    fun hasLevel(raidLevel: Int): Boolean = raidLevel >= level

    companion object {
        private val rewards = entries.map { ChanceItem(it, it.chance) }
        fun random(): TOAUniqueReward? = rewards.roll()
    }
}