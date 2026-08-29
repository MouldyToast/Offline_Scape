package com.near_reality.game.content.slayer

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 */
data class SlayerMonsterInfo(
    var ids : MutableSet<Int> = mutableSetOf(),
    var names : MutableSet<String> = mutableSetOf(),
)
