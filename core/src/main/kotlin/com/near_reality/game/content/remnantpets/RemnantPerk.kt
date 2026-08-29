package com.near_reality.game.content.remnantpets

import com.zenyte.game.world.entity.player.Player
import com.zenyte.utils.TimeUnit

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.25.2025
 */
sealed interface RemnantPerk {
    val scalar: Double
        get() = 100.0
    val primaryPredicate : (Player) -> Boolean
        get() = { true }
    val secondaryPredicate : (Player) -> Boolean
        get() = { true }
    val requiredCooldown: Int
        get() = 0
    val cooldownPredicate : (Player) -> Boolean
        get() = { player ->
            val lastUsage = player.lastTickPetUsage
            if(lastUsage == 0L)
                true
            else {
                val variance = System.currentTimeMillis() - lastUsage
                variance > TimeUnit.TICKS.toMillis(requiredCooldown.toLong())
            }
        }
    val description: String

    class DoubleClueScrollDrops(
        override val scalar: Double,
        override val description: String = "$scalar% chance to double clue scroll drops from all sources"
    ) : RemnantPerk

    class InstantClueScrollSolveChance(
        override val scalar: Double,
        override val description: String = "$scalar% chance to instantly solve a clue scroll (stacks with perk)"
    ) : RemnantPerk

    class DoubleSlayerPointChance(
        override val scalar: Double,
        override val description: String = "$scalar% chance to double slayer points"
    ) : RemnantPerk

    class SlayerTaskSpeedExtendChance(
        override val scalar: Double,
        override val description: String = "$scalar% chance to proc expeditious/slaughter effect on task. (toggleable)"
    ) : RemnantPerk

    class DoubleRemnantSkillingDropsGielinor(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = NonWildyOnly,
        override val description: String = "$scalar% chance to double remnant skilling parts found in Gielenor"
    ) : RemnantPerk

    class EnsouledHeadsToPrayerXP(
        override val scalar: Double,
        override val description: String = "$scalar% chance to instantly consume ensouled heads for prayer xp on drop"
    ) : RemnantPerk

    class NotConsumeSeedPackRoll(
        override val scalar: Double,
        override val description: String = "$scalar% chance to not consume a seed pack roll"
    ) : RemnantPerk

    class DoubleHerbAndSeedDrops(
        override val scalar: Double,
        override val description: String = "$scalar% chance to double herb and seed drops from PvM monsters"
    ) : RemnantPerk

    class IncreasedBloodMoney(
        override val scalar: Double,
        override val description: String = "$scalar% increased blood money from most sources"
    ) : RemnantPerk

    class DoubleRemnantSkillingDropsWildy(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = WildyOnly,
        override val description: String = "$scalar% chance to double remnant skilling parts found in Wilderness"
    ) : RemnantPerk

    class AdditionalWildyDropRoll(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = WildyOnly,
        override val description: String = "$scalar% chance to gain an additional roll against Wildy NPCs & Vaults"
    ) : RemnantPerk

    class GlobalDropRateBoost(
        override val scalar: Double,
        override val description: String = "$scalar% global drop rate increase"
    ) : RemnantPerk

    class GlobalIncreasedNotedSupplies(
        override val scalar: Double,
        override val description: String = "$scalar% increased quantity of dropped & noted supplies"
    ) : RemnantPerk

    class IncreasedDamageGiven(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = NonPvpOnly,
        override val secondaryPredicate: (Player) -> Boolean,
        override val description: String
    ) : RemnantPerk

    class ReducedDamageTaken(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = NonPvpOnly,
        override val secondaryPredicate: (Player) -> Boolean,
        override val description: String
    ) : RemnantPerk

    class DamageDealtReturnedAsHealth(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = NonPvpOnly,
        override val description: String = "$scalar% of damage dealt is returned as health on all hits (PvM only)"
    ) : RemnantPerk

    class RestoreHealth(
        override val scalar: Double,
        override val requiredCooldown: Int,
        override val primaryPredicate: (Player) -> Boolean = NonPvpOrWildy,
        override val description: String = "Every $requiredCooldown seconds, you can talk to your pet to restore full health. (non Wildy)"
    ) : RemnantPerk

    class BoostedHealthRecovery(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = NonPvpOrWildy,
        override val description: String = "Restoring health with food or potions is $scalar% more effective"
    ) : RemnantPerk

    class DamageDealtReturnedAsPrayer(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = NonPvpOnly,
        override val description: String = "$scalar% of damage dealt is returned as prayer points on all hits (PvM only)"
    ) : RemnantPerk

    class RestorePrayer(
        override val scalar: Double,
        override val requiredCooldown: Int,
        override val primaryPredicate: (Player) -> Boolean = NonPvpOrWildy,
        override val description: String = "Every $requiredCooldown seconds, you can talk to your pet to restore full prayer. (non Wildy)"
    ) : RemnantPerk

    class BoostedPrayerRecovery(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = NonPvpOrWildy,
        override val description: String = "Restoring prayer with potions is $scalar% more effective"
    ) : RemnantPerk

    class AdditionalDropChancePvm(
        override val scalar: Double,
        override val description: String = "$scalar% chance to roll an additional drop on most NPCs"
    ) : RemnantPerk

    class STCompleteResetScrollChance(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = { p -> p.resetScrollsToday < 3},
        override val description: String = "$scalar% chance to find a slayer task reset scroll when completing a task (max 3 / day)"
    ) : RemnantPerk

    class STCompleteChoiceScrollChance(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = { p -> p.choiceScrollsToday < 3},
        override val description: String = "$scalar% chance to find a slayer task choice scroll when completing a task (max 3 / day)"
    ) : RemnantPerk

    class STCompleteLarransBoosterChance(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = { p -> p.larransBoostersToday < 3},
        override val description: String = "$scalar% chance to find a Larran's booster when completing a task (max 3 / day)"
    ) : RemnantPerk

    class STCompletePetBoosterChance(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = { p -> p.petBoostersToday < 3},
        override val description: String = "$scalar% chance to find a Slayer booster when completing a task (max 3 / day)"
    ) : RemnantPerk

    class STCompleteSlayerBoosterChance(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = { p -> p.slayerBoostersToday < 3},
        override val description: String = "$scalar% chance to find a Pet booster when completing a task (max 3 / day)"
    ) : RemnantPerk

    class SpecialEnergyRestoreOnPowerfulKill(
        override val scalar: Double,
        override val description: String = "$scalar% of special attack is restored on killing enemy above Lv. 100"
    ) : RemnantPerk

    class RestoreAll(
        override val scalar: Double,
        override val primaryPredicate: (Player) -> Boolean = NonPvpOrWildy,
        override val requiredCooldown: Int,
        override val description: String = "Every $requiredCooldown seconds, you can talk to your pet to restore hp, prayer, and stats. (non Wildy)"
    ) : RemnantPerk

}