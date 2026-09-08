package com.near_reality.game.content.crystal.recipes.chargeable

import com.near_reality.game.content.crystal.CrystalRecipe
import com.near_reality.game.content.crystal.CrystalSeed
import com.near_reality.game.content.crystal.recipes.CrystalChargeable
import com.zenyte.game.item.ids.*
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.degradableitems.DegradeType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot

/**
 * Represents a type of armour that can be created following the implemented [recipe][CrystalRecipe].
 *
 * @author Stan van der Bend
 */
sealed class CrystalArmour(
    val damageBonus: Double,
    val accuracyBonus: Double,
    override val productItemId: Int,
    override val inactiveId: Int,
    override val crystalShardCost: Int,
    override val materials: List<Item>,
    override val requiredCrafting: Int,
    override val requiredSmithing: Int,
    override val craftingExperience: Int,
    override val smithingExperience: Int,
) : CrystalChargeable() {

    companion object {

        private val bowItems = intArrayOf(
            CRYSTAL_BOW_BASIC,
            CRYSTAL_BOW_ATTUNED,
            CRYSTAL_BOW_PERFECTED,

            NEW_CRYSTAL_BOW_I,
            CRYSTAL_BOW_FULL_I,
            CRYSTAL_BOW_910_I,
            CRYSTAL_BOW_810_I,
            CRYSTAL_BOW_710_I,
            CRYSTAL_BOW_610_I,
            CRYSTAL_BOW_510_I,
            CRYSTAL_BOW_410_I,
            CRYSTAL_BOW_310_I,
            CRYSTAL_BOW_210_I,
            CRYSTAL_BOW_110_I,

            NEW_CRYSTAL_BOW,
            CRYSTAL_BOW_FULL,
            CRYSTAL_BOW_910,
            CRYSTAL_BOW_810,
            CRYSTAL_BOW_710,
            CRYSTAL_BOW_610,
            CRYSTAL_BOW_510,
            CRYSTAL_BOW_410,
            CRYSTAL_BOW_310,
            CRYSTAL_BOW_210,
            CRYSTAL_BOW_110,

            BOW_OF_FAERDHINEN_C,
            BOW_OF_FAERDHINEN_C_25884,
            BOW_OF_FAERDHINEN_C_25886,
            BOW_OF_FAERDHINEN_C_25888,
            BOW_OF_FAERDHINEN_C_25890,
            BOW_OF_FAERDHINEN_C_25892,
            BOW_OF_FAERDHINEN_C_25894,
            BOW_OF_FAERDHINEN_C_25896,
        )

        val all by lazy {
            listOf(
                Helm,
                Body,
                Legs,

                AmloddHelm,
                AmloddBody,
                AmloddLegs,

                CadarnHelm,
                CadarnBody,
                CadarnLegs,

                CrwysHelm,
                CrwysBody,
                CrwysLegs,

                HefinHelm,
                HefinBody,
                HefinLegs,

                IorwerthHelm,
                IorwerthBody,
                IorwerthLegs,

                IthellHelm,
                IthellBody,
                IthellLegs,

                TrahaearnHelm,
                TrahaearnBody,
                TrahaearnLegs
            )
        }

        private fun appliesTo(player: Player) =
            CrystalWeapon.Bow.hasEquippedActive(player) ||
                    CrystalWeapon.BowOfFaerdhinen.hasEquippedActive(player) ||
                    player.equipment.containsAnyOf(EquipmentSlot.WEAPON.slot, bowItems)

        fun getTotalAccuracyBonus(player: Player) = if (appliesTo(player))
            all.filter { it.hasEquippedActive(player) }.sumOf { it.accuracyBonus }
        else
            0.0

        fun getTotalDamageBonus(player: Player) = if (appliesTo(player))
            all.filter { it.hasEquippedActive(player) }.sumOf { it.damageBonus }
        else
            0.0
    }

    override val startCharges = 20_000

    override val maximumCharges = 20_000

    override val type: DegradeType = DegradeType.CUSTOM

    object Helm : CrystalArmour(
        productItemId = CRYSTAL_HELM,
        inactiveId = CRYSTAL_HELM_INACTIVE,
        damageBonus = 0.025,
        accuracyBonus = 0.05,
        crystalShardCost = 50,
        materials = CrystalSeed.ARMOUR * 1,
        requiredCrafting = 70,
        requiredSmithing = 70,
        craftingExperience = 2500,
        smithingExperience = 2500
    )

    object Legs : CrystalArmour(
        productItemId = CRYSTAL_LEGS,
        inactiveId = CRYSTAL_LEGS_INACTIVE,
        damageBonus = 0.05,
        accuracyBonus = 0.10,
        crystalShardCost = 100,
        materials = CrystalSeed.ARMOUR * 2,
        requiredCrafting = 72,
        requiredSmithing = 72,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object Body : CrystalArmour(
        productItemId = CRYSTAL_BODY,
        inactiveId = CRYSTAL_BODY_INACTIVE,
        damageBonus = 0.075,
        accuracyBonus = 0.15,
        crystalShardCost = 150,
        materials = CrystalSeed.ARMOUR * 3,
        requiredCrafting = 74,
        requiredSmithing = 74,
        craftingExperience = 7500,
        smithingExperience = 7500
    )

    object AmloddHelm : CrystalArmour(
        productItemId = CRYSTAL_HELM_27777,
        inactiveId = CRYSTAL_HELM_INACTIVE_27779,
        damageBonus = 0.025,
        accuracyBonus = 0.05,
        crystalShardCost = 50,
        materials = CrystalSeed.ARMOUR * 1,
        requiredCrafting = 70,
        requiredSmithing = 70,
        craftingExperience = 2500,
        smithingExperience = 2500
    )

    object AmloddLegs : CrystalArmour(
        productItemId = CRYSTAL_LEGS_27773,
        inactiveId = CRYSTAL_LEGS_INACTIVE_27775,
        damageBonus = 0.05,
        accuracyBonus = 0.10,
        crystalShardCost = 100,
        materials = CrystalSeed.ARMOUR * 2,
        requiredCrafting = 72,
        requiredSmithing = 72,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object AmloddBody : CrystalArmour(
        productItemId = CRYSTAL_BODY_27769,
        inactiveId = CRYSTAL_BODY_INACTIVE_27771,
        damageBonus = 0.075,
        accuracyBonus = 0.15,
        crystalShardCost = 150,
        materials = CrystalSeed.ARMOUR * 3,
        requiredCrafting = 74,
        requiredSmithing = 74,
        craftingExperience = 7500,
        smithingExperience = 7500
    )

    object CadarnHelm : CrystalArmour(
        productItemId = CRYSTAL_HELM_27753,
        inactiveId = CRYSTAL_HELM_INACTIVE_27755,
        damageBonus = 0.025,
        accuracyBonus = 0.05,
        crystalShardCost = 50,
        materials = CrystalSeed.ARMOUR * 1,
        requiredCrafting = 70,
        requiredSmithing = 70,
        craftingExperience = 2500,
        smithingExperience = 2500
    )

    object CadarnBody : CrystalArmour(
        productItemId = CRYSTAL_BODY_27757,
        inactiveId = CRYSTAL_BODY_INACTIVE_27759,
        damageBonus = 0.075,
        accuracyBonus = 0.15,
        crystalShardCost = 150,
        materials = CrystalSeed.ARMOUR * 3,
        requiredCrafting = 74,
        requiredSmithing = 74,
        craftingExperience = 7500,
        smithingExperience = 7500
    )

    object CadarnLegs : CrystalArmour(
        productItemId = CRYSTAL_LEGS_27749,
        inactiveId = CRYSTAL_LEGS_INACTIVE_27751,
        damageBonus = 0.05,
        accuracyBonus = 0.10,
        crystalShardCost = 100,
        materials = CrystalSeed.ARMOUR * 2,
        requiredCrafting = 72,
        requiredSmithing = 72,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object CrwysHelm : CrystalArmour(
        productItemId = CRYSTAL_HELM_27765,
        inactiveId = CRYSTAL_HELM_INACTIVE_27767,
        damageBonus = 0.025,
        accuracyBonus = 0.05,
        crystalShardCost = 50,
        materials = CrystalSeed.ARMOUR * 1,
        requiredCrafting = 70,
        requiredSmithing = 70,
        craftingExperience = 2500,
        smithingExperience = 2500
    )

    object CrwysBody : CrystalArmour(
        productItemId = CRYSTAL_BODY_27709,
        inactiveId = CRYSTAL_BODY_INACTIVE_27711,
        damageBonus = 0.075,
        accuracyBonus = 0.15,
        crystalShardCost = 150,
        materials = CrystalSeed.ARMOUR * 3,
        requiredCrafting = 74,
        requiredSmithing = 74,
        craftingExperience = 7500,
        smithingExperience = 7500
    )

    object CrwysLegs : CrystalArmour(
        productItemId = CRYSTAL_LEGS_27761,
        inactiveId = CRYSTAL_LEGS_INACTIVE_27763,
        damageBonus = 0.05,
        accuracyBonus = 0.10,
        crystalShardCost = 100,
        materials = CrystalSeed.ARMOUR * 2,
        requiredCrafting = 72,
        requiredSmithing = 72,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object HefinHelm : CrystalArmour(
        productItemId = CRYSTAL_HELM_27705,
        inactiveId = CRYSTAL_HELM_INACTIVE_27707,
        damageBonus = 0.025,
        accuracyBonus = 0.05,
        crystalShardCost = 50,
        materials = CrystalSeed.ARMOUR * 1,
        requiredCrafting = 70,
        requiredSmithing = 70,
        craftingExperience = 2500,
        smithingExperience = 2500
    )

    object HefinBody : CrystalArmour(
        productItemId = CRYSTAL_BODY_27697,
        inactiveId = CRYSTAL_BODY_INACTIVE_27699,
        damageBonus = 0.075,
        accuracyBonus = 0.15,
        crystalShardCost = 150,
        materials = CrystalSeed.ARMOUR * 3,
        requiredCrafting = 74,
        requiredSmithing = 74,
        craftingExperience = 7500,
        smithingExperience = 7500
    )

    object HefinLegs : CrystalArmour(
        productItemId = CRYSTAL_LEGS_27701,
        inactiveId = CRYSTAL_LEGS_INACTIVE_27703,
        damageBonus = 0.05,
        accuracyBonus = 0.10,
        crystalShardCost = 100,
        materials = CrystalSeed.ARMOUR * 2,
        requiredCrafting = 72,
        requiredSmithing = 72,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object IorwerthHelm : CrystalArmour(
        productItemId = CRYSTAL_HELM_27729,
        inactiveId = CRYSTAL_HELM_INACTIVE_27731,
        damageBonus = 0.025,
        accuracyBonus = 0.05,
        crystalShardCost = 50,
        materials = CrystalSeed.ARMOUR * 1,
        requiredCrafting = 70,
        requiredSmithing = 70,
        craftingExperience = 2500,
        smithingExperience = 2500
    )

    object IorwerthBody : CrystalArmour(
        productItemId = CRYSTAL_BODY_27733,
        inactiveId = CRYSTAL_BODY_INACTIVE_27735,
        damageBonus = 0.075,
        accuracyBonus = 0.15,
        crystalShardCost = 150,
        materials = CrystalSeed.ARMOUR * 3,
        requiredCrafting = 74,
        requiredSmithing = 74,
        craftingExperience = 7500,
        smithingExperience = 7500
    )

    object IorwerthLegs : CrystalArmour(
        productItemId = CRYSTAL_LEGS_27725,
        inactiveId = CRYSTAL_LEGS_INACTIVE_27727,
        damageBonus = 0.05,
        accuracyBonus = 0.10,
        crystalShardCost = 100,
        materials = CrystalSeed.ARMOUR * 2,
        requiredCrafting = 72,
        requiredSmithing = 72,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object IthellHelm : CrystalArmour(
        productItemId = CRYSTAL_HELM_27717,
        inactiveId = CRYSTAL_HELM_INACTIVE_27719,
        damageBonus = 0.025,
        accuracyBonus = 0.05,
        crystalShardCost = 50,
        materials = CrystalSeed.ARMOUR * 1,
        requiredCrafting = 70,
        requiredSmithing = 70,
        craftingExperience = 2500,
        smithingExperience = 2500
    )

    object IthellBody : CrystalArmour(
        productItemId = CRYSTAL_BODY_27721,
        inactiveId = CRYSTAL_BODY_INACTIVE_27723,
        damageBonus = 0.075,
        accuracyBonus = 0.15,
        crystalShardCost = 150,
        materials = CrystalSeed.ARMOUR * 3,
        requiredCrafting = 74,
        requiredSmithing = 74,
        craftingExperience = 7500,
        smithingExperience = 7500
    )

    object IthellLegs : CrystalArmour(
        productItemId = CRYSTAL_LEGS_27713,
        inactiveId = CRYSTAL_LEGS_INACTIVE_27715,
        damageBonus = 0.05,
        accuracyBonus = 0.10,
        crystalShardCost = 100,
        materials = CrystalSeed.ARMOUR * 2,
        requiredCrafting = 72,
        requiredSmithing = 72,
        craftingExperience = 5000,
        smithingExperience = 5000
    )

    object TrahaearnHelm : CrystalArmour(
        productItemId = CRYSTAL_HELM_27741,
        inactiveId = CRYSTAL_HELM_INACTIVE_27743,
        damageBonus = 0.025,
        accuracyBonus = 0.05,
        crystalShardCost = 50,
        materials = CrystalSeed.ARMOUR * 1,
        requiredCrafting = 70,
        requiredSmithing = 70,
        craftingExperience = 2500,
        smithingExperience = 2500
    )

    object TrahaearnBody : CrystalArmour(
        productItemId = CRYSTAL_BODY_27745,
        inactiveId = CRYSTAL_BODY_INACTIVE_27747,
        damageBonus = 0.075,
        accuracyBonus = 0.15,
        crystalShardCost = 150,
        materials = CrystalSeed.ARMOUR * 3,
        requiredCrafting = 74,
        requiredSmithing = 74,
        craftingExperience = 7500,
        smithingExperience = 7500
    )

    object TrahaearnLegs : CrystalArmour(
        productItemId = CRYSTAL_LEGS_27737,
        inactiveId = CRYSTAL_LEGS_INACTIVE_27739,
        damageBonus = 0.05,
        accuracyBonus = 0.10,
        crystalShardCost = 100,
        materials = CrystalSeed.ARMOUR * 2,
        requiredCrafting = 72,
        requiredSmithing = 72,
        craftingExperience = 5000,
        smithingExperience = 5000
    )
}
