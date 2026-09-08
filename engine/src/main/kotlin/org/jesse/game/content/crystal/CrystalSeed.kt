package org.jesse.game.content.crystal

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*

/**
 * Represents a type of crystal seed that is in most cases a required material for crafting a crystal item.
 *
 * @see CrystalRecipe the recipe for which a seed is (in most cases) required.
 *
 * @author Stan van der Bend
 */
enum class CrystalSeed(val itemId: Int, val shardReturnRate: Int) {
    WEAPON(CRYSTAL_SEED, 10),
    TOOL(CRYSTAL_TOOL_SEED, 100),
    ENHANCED_TELEPORT(ENHANCED_CRYSTAL_TELEPORT_SEED, 150),
    ARMOUR(CRYSTAL_ARMOUR_SEED, 250),
    ENHANCED_WEAPON(ENHANCED_CRYSTAL_WEAPON_SEED, 1500);

    operator fun times(amount: Int) = listOf(Item(itemId, amount))
}
