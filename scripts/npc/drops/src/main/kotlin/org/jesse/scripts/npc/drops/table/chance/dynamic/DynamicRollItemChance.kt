package org.jesse.scripts.npc.drops.table.chance.dynamic

import org.jesse.scripts.npc.drops.table.DropQuantity
import org.jesse.scripts.npc.drops.table.chance.RollItemChance
import org.jesse.game.world.entity.player.Player

class DynamicRollItemChance(
    id: Int,
    quantity: DropQuantity,
    override val rarityProvider: (Player) -> Int
) : RollItemChance(id, quantity), DynamicRollChance
