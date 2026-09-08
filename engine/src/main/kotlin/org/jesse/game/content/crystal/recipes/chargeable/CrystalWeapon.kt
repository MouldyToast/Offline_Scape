package org.jesse.game.content.crystal.recipes.chargeable

import org.jesse.game.content.crystal.CrystalSeed
import org.jesse.game.content.crystal.recipes.CrystalChargeable
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.degradableitems.DegradeType

/**
 * Represents a type of crystal item that is intended for use in combat.
 *
 * @author Stan van der Bend
 */
sealed class CrystalWeapon(
    override val productItemId: Int,
    override val inactiveId: Int,
    override val startCharges: Int,
    override val maximumCharges: Int,
    override val crystalShardCost: Int,
    override val materials: List<Item>,
    override val requiredCrafting: Int,
    override val requiredSmithing: Int,
    override val craftingExperience: Int,
    override val smithingExperience: Int
) : CrystalChargeable() {

    companion object {
        val all by lazy { listOf( BowOfFaerdhinen, BladeOfSaeldor, Halberd, Shield, Bow) }
    }

    override val type: DegradeType = DegradeType.CUSTOM

    object BladeOfSaeldor : CrystalWeapon(
        productItemId = BLADE_OF_SAELDOR,
        inactiveId = BLADE_OF_SAELDOR_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 100,
        materials = CrystalSeed.ENHANCED_WEAPON * 1,
        requiredCrafting = 80,
        requiredSmithing = 80,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object BowOfFaerdhinen : CrystalWeapon(
        productItemId = BOW_OF_FAERDHINEN,
        inactiveId = BOW_OF_FAERDHINEN_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 100,
        materials = CrystalSeed.ENHANCED_WEAPON * 1,
        requiredCrafting = 80,
        requiredSmithing = 80,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object Bow : CrystalWeapon(
        productItemId = CRYSTAL_BOW,
        inactiveId = CRYSTAL_BOW_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 40,
        materials = CrystalSeed.WEAPON * 1,
        requiredCrafting = 78,
        requiredSmithing = 78,
        craftingExperience = 2000,
        smithingExperience = 2000
    )

    object Halberd : CrystalWeapon(
        productItemId = CRYSTAL_HALBERD,
        inactiveId = CRYSTAL_HALBERD_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 40,
        materials = CrystalSeed.WEAPON * 1,
        requiredCrafting = 78,
        requiredSmithing = 78,
        craftingExperience = 2000,
        smithingExperience = 2000
    )

    object Shield : CrystalWeapon(
        productItemId = CRYSTAL_SHIELD,
        inactiveId = CRYSTAL_SHIELD_INACTIVE,
        startCharges = 20_000,
        maximumCharges = 20_000,
        crystalShardCost = 40,
        materials = CrystalSeed.WEAPON * 1,
        requiredCrafting = 78,
        requiredSmithing = 78,
        craftingExperience = 2000,
        smithingExperience = 2000
    )
}
