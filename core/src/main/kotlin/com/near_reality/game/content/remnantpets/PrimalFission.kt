package com.near_reality.game.content.remnantpets

import com.near_reality.game.content.remnantpets.RemnantPet.*
import com.near_reality.game.item.CustomItemId.PRIMAL_COMPONENTS
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.region.CharacterLoop
import mgi.types.config.npcs.NPCDefinitions

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.25.2025
 */
object PrimalFission {

    fun attemptFission(petItem: Item, player: Player) {
        val pet = petItem.toRemnantPet()
        val tier = tierMappings[pet] ?: return

        val required = tierToPrimalComponentRequirement[tier]!!
        if (player.inventory.getAmountOf(ItemId.PRIMAL_COMPONENTS) < required) {
            player.sendMessage("You lack the required primal components ($required total needed)")
            return
        }

        val chucks = player chucksFor pet
        val chuckBoost = (tierToFailureBonus[tier]!! * chucks)
        val initialRate = tierToStartingOdds[tier]!!
        val initialCompBoost = required * tierToPrimalComponentBoost[tier]!!
        val cap = tierToCappedSuccessRate[tier]!!
        val initialSuccess = (initialRate + chuckBoost + initialCompBoost).coerceAtMost(cap.toDouble())

        player.sendInputInt("How many components to use? (success with min #: $initialSuccess%)") { cnt ->
            if (cnt < required) {
                player.sendMessage("You cannot use less than required ($required total needed)")
                return@sendInputInt
            }
            val newCompBoost = tierToPrimalComponentBoost[tier]!! * cnt
            val newSuccessChance = (initialRate + chuckBoost + newCompBoost).coerceAtMost(cap.toDouble())
            player.dialogue {
                options("Do you want to attempt fission? (success: $newSuccessChance%)") {
                    "Yes (I might lose my pet)" {
                        doFission(pet, petItem, player, cnt, newSuccessChance)
                    }
                    "No" { finish() }
                }
            }
        }
    }

    private fun doFission(pet: RemnantPet, petItem: Item, player: Player, cnt: Int, newSuccessChance: Double) {
        player.lock(2)
        val newPet = pet.upgrade() ?: return
        player.animation = Animation.SMITH
        if (player.inventory.deleteItems(petItem, Item(PRIMAL_COMPONENTS, cnt)).result == RequestResult.SUCCESS) {
            val isSuccess = Utils.roll(newSuccessChance)
            WorldTasksManager.schedule({
                if (isSuccess) {
                    val newPetItem = Item(newPet.itemId)
                    player.collectionLog.add(newPetItem)
                    player.inventory.addItem(newPetItem)
                    player broadcastPrimalPet newPet
                    player.inventory.addItem(Item(ItemId.WORLD_BOOST_TOKEN))
                } else {
                    player awardCompLeftoverFor cnt
                    player awardFragmentsFor pet
                    player incrementBadChuck pet
                    player broadcastBadChuck pet
                }
                GameLogger.log {
                    GameLogMessage.PetChuck(
                        username = player.username,
                        petName =  pet.name(),
                        success = isSuccess
                    )
                }
            }, 1)
        }
    }

    fun askForConfirmationForFK(player: Player, petItem: Item) {
        val pet = petItem.toRemnantPet()
        if (pet == NonRemnantPet || RemnantPetManager.standardPets.contains(pet)) return
        player.dialogue {
            options("THIS IS IRREVERSIBLE, ARE YOU SURE YOU WANT THIS?") {
                "Yes, GO FOR IT" {
                    if (player removed pet) {
                        when (pet) {
                            is DarkRoc -> player.burnedDarkRoc = true
                            is DarkKratos -> player.burnedDarkKratos = true
                            else -> {}
                        }
                        player broadcast "${player.username} has just sacrificed their " + NPCDefinitions.get(pet.npcId).name + " to the primal fire!"
                        CharacterLoop.find(player.location, 10, DrifterNPC::class.java, { true }).forEach {
                            it.forceTalk = ForceTalk("NICE WORK!")
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    val upgradeMappings = mapOf(
        PostiePete to DarkPostiePete,
        Imp to DarkImp,
        Toucan to DarkToucan,
        KingPenguin to DarkKingPenguin,
        Kklik to DarkKklik,
        ShadowWarrior to DarkShadowWarrior,
        ShadowArcher to DarkShadowArcher,
        ShadowWizard to DarkShadowWizard,
        HealerDeathSpawn to DarkHealerDeathSpawn,
        HolyDeathSpawn to DarkHolyDeathSpawn,
        Seren to DarkSeren,
        CorruptBeast to DarkCorruptBeast,
        Roc to DarkRoc,
        Kratos to DarkKratos
    )

    @JvmStatic
    val tierMappings = mapOf(
        PostiePete to PetTier.LOW,
        Toucan to PetTier.LOW,
        KingPenguin to PetTier.LOW,
        Kklik to PetTier.LOW,
        Imp to PetTier.LOW,
        ShadowWarrior to PetTier.LOW,
        ShadowArcher to PetTier.LOW,
        ShadowWizard to PetTier.LOW,
        HealerDeathSpawn to PetTier.MEDIUM,
        HolyDeathSpawn to PetTier.MEDIUM,
        Seren to PetTier.MEDIUM,
        CorruptBeast to PetTier.MEDIUM,
        Roc to PetTier.HIGH,
        Kratos to PetTier.HIGH
    )

    @JvmStatic
    val tierToStartingOdds = mapOf(
        PetTier.LOW to 30,
        PetTier.MEDIUM to 25,
        PetTier.HIGH to 20,
    )

    @JvmStatic
    val tierToFailureBonus = mapOf(
        PetTier.LOW to 5.0,     /* 50% @ 10 + 30% = 80% */
        PetTier.MEDIUM to 3.5,  /* 35% @ 10 + 25% = 60% */
        PetTier.HIGH to 2.5,    /* 25% @ 10 + 20% = 45% */
    )

    @JvmStatic
    val tierToCappedSuccessRate = mapOf(
        PetTier.LOW to 80,
        PetTier.MEDIUM to 65,
        PetTier.HIGH to 50,
    )

    @JvmStatic
    val tierToPrimalComponentBoost = mapOf(
        PetTier.LOW to 1.0,
        PetTier.MEDIUM to 0.5,
        PetTier.HIGH to 0.2,
    )

    @JvmStatic
    val tierToPrimalComponentRequirement = mapOf(
        PetTier.LOW to 7,
        PetTier.MEDIUM to 15,
        PetTier.HIGH to 25,
    )
}


enum class PetTier {
    HIGH, MEDIUM, LOW
}