package org.jesse.scripts.npc.drops.table.chance.immutable

import org.jesse.scripts.npc.drops.table.chance.RollNothingChance

class StaticRollNothingChance(override val rarity: Int = 128)
    : StaticRollChance, RollNothingChance
