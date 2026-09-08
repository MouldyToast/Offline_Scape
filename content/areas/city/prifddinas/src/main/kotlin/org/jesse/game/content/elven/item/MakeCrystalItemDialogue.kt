package org.jesse.game.content.elven.item

import org.jesse.game.content.crystal.recipes.CrystalCorrupted
import org.jesse.game.content.crystal.CrystalRecipe
import org.jesse.game.content.crystal.recipes.EnhancedCrystalKeyRecipe
import org.jesse.game.item.Item
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.plugins.dialogue.MakeType
import org.jesse.plugins.dialogue.SkillDialogue
import mgi.utilities.StringFormatUtil

/**
 * Represents a [SkillDialogue] to handle the creation interface,
 * lists all possible recipes the player can make.
 *
 * @author Stan van der Bend
 */
class MakeCrystalItemDialogue(
    player: Player,
    private val recipes: List<CrystalRecipe>,
) : SkillDialogue(player, "What would you like to make?", *recipes.map { Item(it.productItemId) }.toTypedArray()) {

    override fun type() = MakeType.MAKE

    override fun getMaximumAmount() = 1

    override fun run(slotId: Int, amount: Int) {

        val recipe = recipes[slotId]

        if (recipe is CrystalCorrupted) {
            player.dialogue {
                val baseWeapon = recipe.findCrystalWeaponForRecipe(player) ?: return@dialogue
                val prefix = if (baseWeapon.charges > 0) "charged" else ""
                val name = "$prefix ${baseWeapon.name}"
                val shardCost =
                    Colour.RED.wrap(StringFormatUtil.formatNumberUS(recipe.getShardCost(player)) + " Crystal Shards")
                doubleItem(
                    recipe.weaponToCorrupt.productItemId, recipe.productItemId,
                    "Corrupting your $name will cost $shardCost, any crystal shards currently stored in your bow have been used to reduce the cost."
                )
                options("Corrupt your $name?") {
                    "Yes" { player.actionManager.action = MakeCrystalItemAction(recipe, amount) }
                    "No" {}
                }
            }
        } else if (recipe is EnhancedCrystalKeyRecipe) {
            player.actionManager.action = MakeCrystalKeyAction(recipe, amount)
        } else {
            player.actionManager.action = MakeCrystalItemAction(recipe, amount)
        }
    }
}
