package org.jesse.scripts.npc.drops.table.chance.dynamic

import org.jesse.scripts.npc.drops.table.chance.RollChance
import org.jesse.game.world.entity.player.Player

interface DynamicRollChance : RollChance {

    val rarityProvider: (Player) -> Int
}
