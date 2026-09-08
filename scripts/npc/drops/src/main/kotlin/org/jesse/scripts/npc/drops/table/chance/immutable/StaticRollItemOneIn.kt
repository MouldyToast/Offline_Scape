package org.jesse.scripts.npc.drops.table.chance.immutable

import org.jesse.scripts.npc.drops.table.DropQuantity
import org.jesse.scripts.npc.drops.table.chance.RollOneIn

class StaticRollItemOneIn(
    id: Int,
    quantity: DropQuantity,
    rarity: Int = 1
) : StaticRollItemChance(id, quantity, rarity), RollOneIn
