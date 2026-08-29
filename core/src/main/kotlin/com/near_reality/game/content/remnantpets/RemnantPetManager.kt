package com.near_reality.game.content.remnantpets

import com.near_reality.game.content.remnantpets.RemnantPerk.*
import com.near_reality.game.content.remnantpets.RemnantPet.*
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.ui.testinterfaces.GameNoticeboardInterface
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.utils.TimeUnit
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.25.2025
 */
class RemnantPetManager(val player: Player) {

    companion object {
        @JvmStatic val registeredPets = setOf(
            NonRemnantPet,
            PostiePete,
            DarkPostiePete,
            Imp,
            DarkImp,
            Toucan,
            DarkToucan,
            KingPenguin,
            DarkKingPenguin,
            Kklik,
            DarkKklik,
            ShadowWarrior,
            DarkShadowWarrior,
            ShadowArcher,
            DarkShadowArcher,
            ShadowWizard,
            DarkShadowWizard,
            HealerDeathSpawn,
            DarkHealerDeathSpawn,
            HolyDeathSpawn,
            DarkHolyDeathSpawn,
            Seren,
            DarkSeren,
            CorruptBeast,
            DarkCorruptBeast,
            Roc,
            DarkRoc,
            Kratos,
            DarkKratos,
            CorruptKratos
        )

        @JvmStatic val standardPets = setOf(
            PostiePete,
            Imp,
            Toucan,
            KingPenguin,
            Kklik,
            ShadowWarrior,
            ShadowArcher,
            ShadowWizard,
            HealerDeathSpawn,
            HolyDeathSpawn,
            Seren,
            CorruptBeast,
            Roc,
            Kratos
        )

        @JvmStatic val darkPets = setOf(
            DarkPostiePete,
            DarkImp,
            DarkToucan,
            DarkKingPenguin,
            DarkKklik,
            DarkShadowWarrior,
            DarkShadowArcher,
            DarkShadowWizard,
            DarkHealerDeathSpawn,
            DarkHolyDeathSpawn,
            DarkSeren,
            DarkCorruptBeast,
            DarkRoc,
            DarkKratos,
            CorruptKratos
        )
    }
    var currentRemnantPet: RemnantPet = NonRemnantPet

    fun updatePet(npc: NPC?) = npc?.let {
        currentRemnantPet = it.toRemnantPet()
        GameNoticeboardInterface.updateDropRate(player)
    } ?: NonRemnantPet

    fun resetNewDay() {
        player.larransBoostersToday = 0
        player.resetScrollsToday = 0
        player.choiceScrollsToday = 0
        player.petBoostersToday = 0
        player.slayerBoostersToday = 0
    }

    fun removePet() = run { currentRemnantPet = NonRemnantPet }

    fun handleDoubleClueScrollDrop(item: Item) {
        if(activePerks() has DoubleClueScrollDrops::class) {
            val perk = activePerks() get DoubleClueScrollDrops::class
            if (!(perk activeFor player)) return
            if (perk.roll()) {
                player.sendFilteredMessage(Colour.RS_GREEN.wrap("Your pet has found you an additional clue scroll."))
                player.tryAddInventoryThenBank(item.copy())
            }
        }
    }

    fun checkInstantSolveClueStep() : Boolean {
        if(activePerks() has InstantClueScrollSolveChance::class) {
            val perk = activePerks() get InstantClueScrollSolveChance::class
            if(!(perk activeFor player)) return false
            if (perk.roll()) {
                return true
            }
        }
        return false
    }

    fun hasDoubleSlayerPoints() : Boolean {
        if(activePerks() has DoubleSlayerPointChance::class) {
            val perk = activePerks() get DoubleSlayerPointChance::class
            if(!(perk activeFor player)) return false
            if(perk.roll()) {
                return true
            }
        }
        return false
    }

    fun hasExpeditiousEffect() : Boolean {
        if(activePerks() has SlayerTaskSpeedExtendChance::class) {
            val perk = activePerks() get SlayerTaskSpeedExtendChance::class
            if(!(perk activeFor player)) return false
            val extend = player.selectedImpPerkIsExtend
            return !extend
        }
        return false
    }

    fun hasSlaughterEffect() : Boolean {
        if(activePerks() has SlayerTaskSpeedExtendChance::class) {
            val perk = activePerks() get SlayerTaskSpeedExtendChance::class
            if(!(perk activeFor player)) return false
            val extend = player.selectedImpPerkIsExtend
            return extend
        }
        return false
    }

    fun modifySlaughterExpeditiousRate(initial: Int): Int {
        if(activePerks() has SlayerTaskSpeedExtendChance::class) {
            val perk = activePerks() get SlayerTaskSpeedExtendChance::class
            if(!(perk activeFor player)) return initial
            return perk.scalar.toInt()
        }
        return initial
    }

    fun checkDoubleSkillingRemnantDrop() : Boolean {
        val activePerks = activePerks().filter { it.activeFor(player) }
        if(activePerks has DoubleRemnantSkillingDropsGielinor::class) {
            val perk = activePerks() get DoubleRemnantSkillingDropsGielinor::class
            return perk.roll()
        } else if (activePerks has DoubleRemnantSkillingDropsWildy::class) {
            val perk = activePerks() get DoubleRemnantSkillingDropsWildy::class
            return perk.roll()
        }
        return false
    }

    fun skipSeedPackRollConsume() : Boolean {
        if(activePerks() has NotConsumeSeedPackRoll::class) {
            val perk = activePerks() get NotConsumeSeedPackRoll::class
            if(!(perk activeFor player)) return false
            return perk.roll()
        }
        return false
    }

    fun doubleHerbAndSeedDrops() : Boolean {
        if(activePerks() has DoubleHerbAndSeedDrops::class) {
            val perk = activePerks() get DoubleHerbAndSeedDrops::class
            if(!(perk activeFor player)) return false
            return perk.roll()
        }
        return false
    }

    fun getNotedSuppliesModFactor() : Double {
        if(activePerks() has GlobalIncreasedNotedSupplies::class) {
            val perk = activePerks() get GlobalIncreasedNotedSupplies::class
            if(!(perk activeFor player)) return 1.0
            return (perk.scalar / 100.0) + 1.0 // 5 / 100 = 0.05D + 1.0D = 1.05D
        }
        return 1.0
    }

    fun checkEnsouledHeadConsume() : Boolean {
        if(activePerks() has EnsouledHeadsToPrayerXP::class) {
            val perk = activePerks() get EnsouledHeadsToPrayerXP::class
            if(!(perk activeFor player)) return false
            return perk.roll()
        }
        return false
    }

    fun getGlobalDropRateIncrease() : Double {
        if(activePerks() has GlobalDropRateBoost::class) {
            val perk = activePerks() get GlobalDropRateBoost::class
            if(!(perk activeFor player)) return 0.00
            return (perk.scalar / 100.0) // 20 / 100 = 0.20D
        }
        return 0.0
    }

    fun checkDoubleDropsPvm(): Boolean {
        if(activePerks() has AdditionalDropChancePvm::class) {
            val perk = activePerks() get AdditionalDropChancePvm::class
            if(!(perk activeFor player)) return false
            return perk.roll()
        }
        return false
    }

    fun checkDoubleDropsWildy(): Boolean {
        if(activePerks() has AdditionalWildyDropRoll::class) {
            val perk = activePerks() get AdditionalWildyDropRoll::class
            if(!(perk activeFor player)) return false
            return perk.roll()
        }
        return false
    }

    fun modifyBloodMoneyDrop(totalBloodmoney: Int) : Int {
        if(activePerks() has IncreasedBloodMoney::class) {
            val perk = activePerks() get IncreasedBloodMoney::class
            if(!(perk activeFor player)) return totalBloodmoney
            val mod = (perk.scalar / 100) + 1.0
            return floor(totalBloodmoney.toDouble() * mod).toInt()
        }
        return totalBloodmoney
    }

    fun modifyIncomingDamage(dmg: Int) : Int {
        if(activePerks() has ReducedDamageTaken::class) {
            val perk = activePerks() get ReducedDamageTaken::class
            if(!(perk activeFor player)) return dmg
            val mod = 1.0 - (perk.scalar / 100) // 100% - 10% = 90%  =>  90% x dmg = reduced damage
            return ceil(dmg.toDouble() * mod).toInt()
        }
        return dmg
    }

    fun modifyOutgoingDamage(dmg: Int) : Int {
        if(activePerks() has IncreasedDamageGiven::class) {
            val perk = activePerks() get IncreasedDamageGiven::class
            if(!(perk activeFor player)) return dmg
            val mod = 1.0 + (perk.scalar / 100) // 100% + 10% = 110%  =>  110% x dmg = increased damage
            return floor(dmg.toDouble() * mod).toInt()
        }
        return dmg
    }

    fun modifyHealingEffect(pts: Int) : Int {
        if(activePerks() has BoostedHealthRecovery::class) {
            val perk = activePerks() get BoostedHealthRecovery::class
            if(!(perk activeFor player)) return pts
            val mod = (perk.scalar / 100) + 1.0
            return floor(pts.toDouble() * mod).toInt()
        }
        return pts
    }

    fun modifyPrayerRestore(pts: Int) : Int {
        if(activePerks() has BoostedPrayerRecovery::class) {
            val perk = activePerks() get BoostedPrayerRecovery::class
            if(!(perk activeFor player)) return pts
            val mod = (perk.scalar / 100) + 1.0
            return floor(pts.toDouble() * mod).toInt()
        }
        return pts
    }

    fun healPlayerOnDamage(dmg: Int) {
        if(activePerks() has DamageDealtReturnedAsHealth::class) {
            val perk = activePerks() get DamageDealtReturnedAsHealth::class
            if(!(perk activeFor player)) return
            val mod = (perk.scalar / 100) // 10% x dmg = heal
            val heal = floor(dmg.toDouble() * mod).toInt()
            if(heal > 0) player.heal(heal)
            return
        }
        return
    }

    fun restorePrayerOnDamage(dmg: Int) {
        if(activePerks() has DamageDealtReturnedAsPrayer::class) {
            val perk = activePerks() get DamageDealtReturnedAsPrayer::class
            if(!(perk activeFor player)) return
            val mod = (perk.scalar / 100) // 10% x dmg = restore
            val heal = floor(dmg.toDouble() * mod).toInt()
            if(heal > 0) player.prayerManager.restorePrayerPoints(heal, true)
            return
        }
        return
    }

    fun restoreSpecialEnergy() {
        if(activePerks() has SpecialEnergyRestoreOnPowerfulKill::class) {
            val perk = activePerks() get SpecialEnergyRestoreOnPowerfulKill::class
            if(!(perk activeFor player)) return
            val energy = player.combatDefinitions.specialEnergy
            player.combatDefinitions.specialEnergy = min(100.0, (energy + perk.scalar.toInt()).toDouble()).toInt()
            return
        }
        return
    }

    fun processBoostersCheck() {
            activePerks()
                .filter { it activeFor player }
                .filter { it is STCompleteSlayerBoosterChance || it is STCompletePetBoosterChance || it is STCompleteLarransBoosterChance || it is STCompleteChoiceScrollChance || it is STCompleteResetScrollChance }
                .filter { it.roll() }
                .randomOrNull()?.let {
                    when(it) {
                        is STCompleteResetScrollChance -> { player.tryAddInventoryThenBank(Item(ItemId.SLAYER_TASK_RESET_SCROLL)); player.sendMessage("Your pet has found you a slayer reset scroll!"); player.resetScrollsToday++}
                        is STCompleteSlayerBoosterChance -> { player.tryAddInventoryThenBank(Item(ItemId.SLAYER_BOOSTER)); player.sendMessage("Your pet has found you a slayer booster!"); player.slayerBoostersToday++}
                        is STCompletePetBoosterChance -> { player.tryAddInventoryThenBank(Item(ItemId.PET_BOOSTER)); player.sendMessage("Your pet has found you a pet booster!"); player.petBoostersToday++}
                        is STCompleteLarransBoosterChance -> { player.tryAddInventoryThenBank(Item(ItemId.LARRANS_KEY_BOOSTER)); player.sendMessage("Your pet has found you a larran's key booster!");  player.larransBoostersToday++}
                        is STCompleteChoiceScrollChance -> { player.tryAddInventoryThenBank(Item(ItemId.SLAYER_TASK_PICKER_SCROLL)); player.sendMessage("Your pet has found you a slayer task picker scroll!");  player.choiceScrollsToday++}
                        else -> {}
                    }
                }
    }

    fun attemptRestore() {
        activePerks()
            .filter { it activeFor player }
            .filter { it is RestoreAll || it is RestoreHealth || it is RestorePrayer}
            .randomOrNull()?.let {
                when(it) {
                    is RestoreAll -> { restoreAll() }
                    is RestoreHealth -> { restoreHealth() }
                    is RestorePrayer -> { restorePrayer() }
                    else -> return
                }
            } ?:  {
            val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - player.lastTickPetUsage).toInt()
            val reportedCD = currentRemnantPet.cooldownAmt() - totalSeconds
            player.sendMessage("You cannot do this yet. You need to wait $reportedCD second(s) to do this again.")
        }
    }

    private fun restoreHealth() {
        player.graphics = Graphics(1177)
        player.heal(player.maxHitpoints)
        player.sendFilteredMessage("Your pet restores your health.")
        putOnCooldown()
    }

    private fun restorePrayer() {
        player.graphics = Graphics(1177)
        player.prayerManager.prayerPoints = player.skills.getLevelForXp(Skills.PRAYER)
        player.sendFilteredMessage("Your pet restores your prayer.")
        putOnCooldown()
    }

    private fun restoreAll() {
        player.graphics = Graphics(1177)
        player.heal(player.maxHitpoints)
        player.prayerManager.prayerPoints = player.skills.getLevelForXp(Skills.PRAYER)
        player.sendFilteredMessage("Your pet restores your health, prayer, skills and stamina.")
        player.variables.runEnergy = 100.0
        for (i in SkillConstants.SKILLS.indices) {
            if (player.skills.getLevel(i) < player.skills.getLevelForXp(i)) {
                player.skills.setLevel(i, player.skills.getLevelForXp(i))
            }
        }
        player.toxins.reset()
        player.combatDefinitions.specialEnergy = 100
        putOnCooldown()
    }

    private fun putOnCooldown() {
        player.lastTickPetUsage = System.currentTimeMillis()
    }

}