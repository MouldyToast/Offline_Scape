package com.near_reality.game.content.toa

import com.near_reality.utils.ChanceItem
import com.near_reality.utils.roll
import com.zenyte.game.item.ids.*

enum class TOAUniqueReward(
    val item: Int,
    val chance: Double,
    val animation: Int,
    val level: Int,
) {
    LIGHTBEARER(LIGHTBEARER, 1.0 / 3.429, 9508, 50),
    MASORI_MASK(MASORI_MASK, 1.0 / 12.0, 9509, 150),
    MASORI_BODY(MASORI_BODY, 1.0 / 12.0, 9510, 150),
    MASORI_CHAPS(MASORI_CHAPS, 1.0 / 12.0, 9511, 150),
    OSMUMTENS_FANG(OSMUMTENS_FANG, 1.0 / 3.429, 9512, 50),
    ELIDINIS_WARD(ELIDINIS_WARD, 1.0 / 8.0, 9513, 150),
    TUMEKENS_SHADOW(TUMEKENS_SHADOW_UNCHARGED, 1.0 / 24.0, 9514, 150);

    fun hasLevel(raidLevel: Int): Boolean = raidLevel >= level

    companion object {
        private val rewards = entries.map { ChanceItem(it, it.chance) }
        fun random(): TOAUniqueReward? = rewards.roll()
    }
}