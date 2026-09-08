package org.jesse.game.content.elven

import org.jesse.game.content.crystal.recipes.chargeable.CrystalDegradeable
import org.jesse.game.content.crystal.CrystalRecipe
import org.jesse.game.item.Item
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import mgi.types.config.items.ItemDefinitions


fun CrystalRecipe.name(): String =
    ItemDefinitions.nameOf(productItemId)

fun CrystalRecipe.produce() =
    Item(productItemId).apply {
        if (this@produce is CrystalDegradeable)
            this.charges = this@produce.maximumCharges
    }

fun Player.canMake(recipe: CrystalRecipe) =
    skills.getLevel(SkillConstants.CRAFTING) >= recipe.requiredCrafting &&
            skills.getLevel(SkillConstants.SMITHING) >= recipe.requiredSmithing
