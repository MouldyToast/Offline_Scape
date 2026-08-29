package com.near_reality.game.content.buffs

import com.zenyte.game.world.entity.player.Player

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.13.2025
 */
data class Buff(
    val id: String,                              // unique identifier
    val name: String,
    val category: BuffCategory,
    val subcategory: BuffSubcategory,
    val predicate: (Player) -> Boolean,          // can this buff apply to this player?
    val validInPvp: Boolean = true,              // false → never in PvP
    val exclusive: Boolean = false,              // “full” exclusive in its subcategory
    val exclusiveWith: Set<String> = emptySet(), // ids of buffs this one semi-excludes
    val modify: (Player, Double) -> Double               // transforms a base value
)