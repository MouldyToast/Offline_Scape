package org.jesse.scripts.npc.drops.table

data class DropQuantityModifier(
    val maxRoll: Int,
    val amountForChance: (Int) -> Int
)
