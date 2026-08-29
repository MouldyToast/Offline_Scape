package com.near_reality.game.content.remnantpets

import com.near_reality.game.item.CustomItemId.*
import com.near_reality.game.item.CustomNpcId.*
import com.near_reality.game.content.remnantpets.RemnantPerk.*

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.25.2025
 */
sealed interface RemnantPet  {
    val npcId: Int
    val itemId: Int
    val inheritance: Set<RemnantPet>
    val perks: Set<RemnantPerk>

    data object NonRemnantPet : RemnantPet {
        override val npcId = -1
        override val itemId = -1
        override val inheritance = emptySet<RemnantPet>()
        override val perks = emptySet<RemnantPerk>()
    }

    data object PostiePete : RemnantPet {
        override val npcId = REMN_POSTIE_PETE
        override val itemId = PET_POSTIE_PETE
        override val inheritance = setOf(this)
        override val perks = setOf(
            DoubleClueScrollDrops(25.0),
            InstantClueScrollSolveChance(10.0)
        )
    }

    data object DarkPostiePete : RemnantPet {
        override val npcId = REMN_DARK_POSTIE_PETE
        override val itemId = PET_DARK_POSTIE_PETE
        override val inheritance = setOf(this, PostiePete)
        override val perks = setOf(
            DoubleClueScrollDrops(50.0),
            InstantClueScrollSolveChance(20.0)
        )
    }

    data object Imp : RemnantPet {
        override val npcId = REMN_IMP
        override val itemId = PET_IMP
        override val inheritance = setOf(this)
        override val perks = setOf(
            DoubleSlayerPointChance(50.0),
            SlayerTaskSpeedExtendChance(25.0)
        )
    }

    data object DarkImp : RemnantPet {
        override val npcId = REMN_DARK_IMP
        override val itemId = PET_DARK_IMP
        override val inheritance = setOf(this, Imp)
        override val perks = setOf(
            DoubleSlayerPointChance(100.0),
            SlayerTaskSpeedExtendChance(50.0)
        )
    }

    data object Toucan : RemnantPet {
        override val npcId = REMN_TOUCAN
        override val itemId = PET_TOUCAN
        override val inheritance = setOf(this)
        override val perks = setOf(
            DoubleRemnantSkillingDropsGielinor(25.0),
            EnsouledHeadsToPrayerXP(50.0),
            NotConsumeSeedPackRoll(25.0),
            DoubleHerbAndSeedDrops(25.0)
        )
    }

    data object DarkToucan : RemnantPet {
        override val npcId = REMN_DARK_TOUCAN
        override val itemId = PET_DARK_TOUCAN
        override val inheritance = setOf(this, Toucan)
        override val perks = setOf(
            DoubleRemnantSkillingDropsGielinor(50.0),
            EnsouledHeadsToPrayerXP(100.0),
            NotConsumeSeedPackRoll(50.0),
            DoubleHerbAndSeedDrops(50.0)
        )
    }

    data object KingPenguin : RemnantPet {
        override val npcId = REMN_KING_PENGUIN
        override val itemId = PET_KING_PENGUIN
        override val inheritance = setOf(this)
        override val perks = setOf(
            IncreasedBloodMoney(25.0),
            DoubleRemnantSkillingDropsWildy(50.0),
            AdditionalWildyDropRoll(10.0)
        )
    }

    data object DarkKingPenguin : RemnantPet {
        override val npcId = REMN_DARK_KING_PENGUIN
        override val itemId = PET_DARK_KING_PENGUIN
        override val inheritance = setOf(this, KingPenguin)
        override val perks = setOf(
            IncreasedBloodMoney(50.0),
            DoubleRemnantSkillingDropsWildy(100.0),
            AdditionalWildyDropRoll(20.0)
        )
    }

    data object Kklik : RemnantPet {
        override val npcId = REMN_KKLIK
        override val itemId = PET_KKLIK
        override val inheritance = setOf(this)
        override val perks = setOf(
            GlobalDropRateBoost(5.0),
            GlobalIncreasedNotedSupplies(5.0)
        )
    }

    data object DarkKklik : RemnantPet {
        override val npcId = REMN_DARK_KKLIK
        override val itemId = PET_DARK_KKLIK
        override val inheritance = setOf(this, Kklik)
        override val perks = setOf(
            GlobalDropRateBoost(10.0),
            GlobalIncreasedNotedSupplies(10.0)
        )
    }

    data object ShadowWarrior : RemnantPet {
        override val npcId = REMN_SHADOW_WARRIOR
        override val itemId = PET_SHADOW_WARRIOR
        override val inheritance = setOf(this)
        override val perks = setOf(
            IncreasedDamageGiven(7.5, secondaryPredicate = { p -> p.inMeleeCombat() }, description = "7.5% damage given increase in melee combat styles (PvM)"),
            ReducedDamageTaken(7.5, secondaryPredicate = { p -> p.inMeleeCombat() }, description = "7.5% damage taken reduction when using a melee combat style (PvM)")
        )
    }

    data object DarkShadowWarrior : RemnantPet {
        override val npcId = REMN_DARK_SHADOW_WARRIOR
        override val itemId = PET_DARK_SHADOW_WARRIOR
        override val inheritance = setOf(this, ShadowWarrior)
        override val perks = setOf(
            IncreasedDamageGiven(15.0, secondaryPredicate = { p -> p.inMeleeCombat() }, description = "15% damage given increase in melee combat styles (PvM)"),
            ReducedDamageTaken(15.0, secondaryPredicate = { p -> p.inMeleeCombat() }, description = "15% damage taken reduction when using a melee combat style (PvM)")
        )
    }

    data object ShadowArcher: RemnantPet {
        override val npcId = REMN_SHADOW_ARCHER
        override val itemId = PET_SHADOW_ARCHER
        override val inheritance = setOf(this)
        override val perks = setOf(
            IncreasedDamageGiven(7.5, secondaryPredicate = { p -> p.inRangedCombat() }, description = "7.5% damage given increase in ranged combat (PvM)"),
            ReducedDamageTaken(7.5, secondaryPredicate = { p -> p.inRangedCombat() }, description = "7.5% damage taken reduction when using a ranged combat style (PvM)")
        )
    }

    data object DarkShadowArcher: RemnantPet {
        override val npcId = REMN_DARK_SHADOW_ARCHER
        override val itemId = PET_DARK_SHADOW_ARCHER
        override val inheritance = setOf(this, ShadowArcher)
        override val perks = setOf(
            IncreasedDamageGiven(15.0, secondaryPredicate = { p -> p.inRangedCombat() }, description = "15% damage given increase in ranged combat (PvM)"),
            ReducedDamageTaken(15.0, secondaryPredicate = { p -> p.inRangedCombat() }, description = "15% damage taken reduction when using a ranged combat style (PvM)")
        )
    }

    data object ShadowWizard: RemnantPet {
        override val npcId = REMN_SHADOW_WIZARD
        override val itemId = PET_SHADOW_WIZARD
        override val inheritance = setOf(this)
        override val perks = setOf(
            IncreasedDamageGiven(7.5, secondaryPredicate = { p -> p.inMagicCombat() }, description = "7.5% damage given increase in magic combat (PvM)"),
            ReducedDamageTaken(7.5, secondaryPredicate = { p -> p.inMagicCombat() }, description = "7.5% damage taken reduction when using a magic combat style (PvM)")
        )
    }

    data object DarkShadowWizard: RemnantPet {
        override val npcId = REMN_DARK_SHADOW_WIZARD
        override val itemId = PET_DARK_SHADOW_WIZARD
        override val inheritance = setOf(this, ShadowWizard)
        override val perks = setOf(
            IncreasedDamageGiven(15.0, secondaryPredicate = { p -> p.inMagicCombat() }, description = "15% damage given increase in magic combat (PvM)"),
            ReducedDamageTaken(15.0, secondaryPredicate = { p -> p.inMagicCombat() }, description = "15% damage taken reduction when using a magic combat style (PvM)")
        )
    }

    data object HealerDeathSpawn: RemnantPet {
        override val npcId = REMN_HEALER_DEATH_SPAWN
        override val itemId = PET_HEALER_DEATH_SPAWN
        override val inheritance = setOf(this)
        override val perks = setOf(
            DamageDealtReturnedAsHealth(5.0),
            RestoreHealth(100.0, 180),
            BoostedHealthRecovery(20.0)
        )
    }

    data object DarkHealerDeathSpawn : RemnantPet {
        override val npcId = REMN_DARK_HEALER_DEATH_SPAWN
        override val itemId = PET_DARK_HEALER_DEATH_SPAWN
        override val inheritance = setOf(this, HealerDeathSpawn)
        override val perks = setOf(
            DamageDealtReturnedAsHealth(10.0),
            RestoreHealth(100.0, 90),
            BoostedHealthRecovery(40.0)
        )
    }

    data object HolyDeathSpawn: RemnantPet {
        override val npcId = REMN_HOLY_DEATH_SPAWN
        override val itemId = PET_HOLY_DEATH_SPAWN
        override val inheritance = setOf(this)
        override val perks = setOf(
            DamageDealtReturnedAsPrayer(5.0),
            RestorePrayer(100.0, 180),
            BoostedPrayerRecovery(20.0)
        )
    }

    data object DarkHolyDeathSpawn : RemnantPet {
        override val npcId = REMN_DARK_HOLY_DEATH_SPAWN
        override val itemId = PET_DARK_HOLY_DEATH_SPAWN
        override val inheritance = setOf(this, HolyDeathSpawn)
        override val perks = setOf(
            DamageDealtReturnedAsPrayer(10.0),
            RestorePrayer(100.0, 90),
            BoostedPrayerRecovery(40.0)
        )
    }

    data object Seren: RemnantPet {
        override val npcId = REMN_SEREN
        override val itemId = PET_SEREN
        override val inheritance = setOf(this)
        override val perks = setOf(
            AdditionalDropChancePvm(3.0),
            STCompleteResetScrollChance(1.0),
            STCompleteChoiceScrollChance(1.0),
            STCompleteLarransBoosterChance(1.0),
            STCompletePetBoosterChance(1.0),
            STCompleteSlayerBoosterChance(1.0)
        )
    }

    data object DarkSeren: RemnantPet {
        override val npcId = REMN_DARK_SEREN
        override val itemId = PET_DARK_SEREN
        override val inheritance = setOf(this, Seren)
        override val perks = setOf(
            AdditionalDropChancePvm(6.0),
            STCompleteResetScrollChance(2.0),
            STCompleteChoiceScrollChance(2.0),
            STCompleteLarransBoosterChance(2.0),
            STCompletePetBoosterChance(2.0),
            STCompleteSlayerBoosterChance(2.0)
        )
    }

    data object CorruptBeast: RemnantPet {
        override val npcId = REMN_CORRUPT_BEAST
        override val itemId = PET_CORRUPT_BEAST
        override val inheritance = setOf(this)
        override val perks = setOf(
            IncreasedDamageGiven(10.0, secondaryPredicate = { true }, description = "10% damage given increase in all combat styles (only PvM)"),
            ReducedDamageTaken(10.0, secondaryPredicate = { true }, description = "10% damage taken reduction from all attacks (only PvM)"),
            SpecialEnergyRestoreOnPowerfulKill(10.0)
        )
    }

    data object DarkCorruptBeast: RemnantPet {
        override val npcId = REMN_DARK_CORRUPT_BEAST
        override val itemId = PET_DARK_CORRUPT_BEAST
        override val inheritance = setOf(this, CorruptBeast)
        override val perks = setOf(
            IncreasedDamageGiven(20.0, secondaryPredicate = { true }, description = "20% damage given increase in all combat styles (only PvM)"),
            ReducedDamageTaken(20.0, secondaryPredicate = { true }, description = "20% damage taken reduction from all attacks (only PvM)"),
            SpecialEnergyRestoreOnPowerfulKill(20.0)
        )
    }

    data object Roc: RemnantPet {
        override val npcId = REMN_ROC
        override val itemId = PET_ROC
        override val inheritance = setOf(this)
        override val perks = setOf(
            GlobalDropRateBoost(10.0),
            GlobalIncreasedNotedSupplies(10.0)
        )
    }

    data object DarkRoc: RemnantPet {
        override val npcId = REMN_DARK_ROC
        override val itemId = PET_DARK_ROC
        override val inheritance = setOf(this)
        override val perks = setOf(
            GlobalDropRateBoost(20.0),
            GlobalIncreasedNotedSupplies(20.0)
        )
    }

    data object Kratos: RemnantPet {
        override val npcId = REMN_KRATOS
        override val itemId = PET_KRATOS
        override val inheritance = setOf(this, PostiePete, Imp, Toucan, KingPenguin, HealerDeathSpawn, HolyDeathSpawn, Seren, CorruptBeast, Kklik)
        override val perks =
            inheritPerksFrom(PostiePete, Imp, Toucan, KingPenguin, Seren, CorruptBeast, Kklik).also {
                it.add(RestoreAll(100.0, requiredCooldown = 180))
                it.add(DamageDealtReturnedAsHealth(5.0))
                it.add(BoostedHealthRecovery(20.0))
                it.add(DamageDealtReturnedAsPrayer(5.0))
                it.add(BoostedPrayerRecovery(20.0))
            }
    }

    data object DarkKratos: RemnantPet {
        override val npcId = REMN_DARK_KRATOS
        override val itemId = PET_DARK_KRATOS
        override val inheritance = setOf(this, DarkPostiePete, DarkImp, DarkToucan, DarkKingPenguin, DarkHealerDeathSpawn, DarkHolyDeathSpawn, DarkSeren, DarkCorruptBeast, DarkKklik)
        override val perks = inheritPerksFrom(DarkPostiePete, DarkImp, DarkToucan, DarkKingPenguin, DarkSeren, DarkCorruptBeast, DarkKklik).also {
            it.add(RestoreAll(100.0, requiredCooldown = 90))
            it.add(DamageDealtReturnedAsHealth(10.0))
            it.add(BoostedHealthRecovery(40.0))
            it.add(DamageDealtReturnedAsPrayer(10.0))
            it.add(BoostedPrayerRecovery(40.0))
        }
    }

    data object CorruptKratos: RemnantPet {
        override val npcId = REMN_FISSILE_KRATOS
        override val itemId = PET_FISSILE_KRATOS
        override val inheritance = setOf(this, DarkKratos, DarkRoc)
        override val perks = inheritPerksFrom(DarkPostiePete, DarkImp, DarkToucan, DarkKingPenguin, DarkSeren, DarkCorruptBeast).also {
            it.add(RestoreAll(100.0, requiredCooldown = 90))
            it.add(DamageDealtReturnedAsHealth(10.0))
            it.add(BoostedHealthRecovery(40.0))
            it.add(DamageDealtReturnedAsPrayer(10.0))
            it.add(BoostedPrayerRecovery(40.0))
            it.add(GlobalDropRateBoost(25.0))
            it.add(GlobalIncreasedNotedSupplies(25.0))
        }
    }
}