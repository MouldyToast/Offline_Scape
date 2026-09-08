package org.jesse.scripts.npc.drops.table.chance.immutable

import org.jesse.scripts.npc.drops.table.DropQuantity
import org.jesse.scripts.npc.drops.table.chance.RollAlways

class StaticRollItemAlways(id: Int, quantity: DropQuantity)
    : StaticRollItemChance(id, quantity, 0), RollAlways
