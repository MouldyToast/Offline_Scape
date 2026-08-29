package com.near_reality.game.content.remnantpets

import com.near_reality.game.item.CustomNpcId.DRIFTER
import com.zenyte.game.content.Book
import com.zenyte.game.content.achievementdiary.Diary
import com.zenyte.game.content.serverevent.WorldBoost
import com.zenyte.game.content.well.WellPerk
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Expression
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.region.CharacterLoop
import com.zenyte.utils.TimeUnit
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.25.2025
 */
class Drifter : NPCPlugin() {
    override fun handle() {
        bind("Talk-to") { player, n ->
            if(!player.hasSpokenToDrifter || player.username.equals("kryeus", true)) {
                player.dialogue {
                    npcWithId(DRIFTER, "Sorry if I startled ya. You just reminded<br>me of a fella from the past who acted<br>like he knew what he was doing<br>and then broke all of my things.", Expression.CALM)
                    player("No problem. I'm used to it.<br>What is this place?")
                    npcWithId(DRIFTER, "Well I've been stuck down here for who knows<br>how long. I've been watching. You can just<br>chuck your pet in that fountain and<br>sometimes it comes back out different.", Expression.CALM)
                    player("Different?")
                    npcWithId(DRIFTER, "Well you'll need to get your hands<br>on some primal components as well...", Expression.ON_ONE_HAND)
                    player("Different?!", Expression.ANGRY)
                    npcWithId(DRIFTER, "Woah there. You remind me of<br>someone I used to know. Always sad<br>then always angry.", Expression.ANXIOUS)
                    player("Dude, answer the question.", Expression.ANNOYED)
                    npcWithId(DRIFTER, "Yes, different. They seem stronger<br>after they get infused with primal components.<br> IF they come back...", Expression.CALM)
                    player("Where can I find these primal components?")
                    npcWithId(DRIFTER, "I'll tell you in a minute, I just want to make sure you understand clearly...", Expression.CALM)
                    player("Understand what?", Expression.ANNOYED)
                    npcWithId(DRIFTER, "Sometimes what goes in,<br> doesn't come back out.", Expression.DISINTERESTED)
                    npcWithId(DRIFTER, "Anyways. Enough with keeping ya,<br> good luck.", Expression.CALM)
                    player.hasSpokenToDrifter = true
                }
            } else {
                player.dialogue {
                    options("Drifter's Intel") {
                        "Primal Item Locations / Values" {
                            player.sendPrimalInfo()
                        }
                        "Pet Info" {
                            player.sendPetInfo()
                        }
                        "Upgrade Info" {
                            player.sendUpgradeInfo()
                        }
                    }
                }

            }


        }
        bind("Primal Info") { player, n ->
            if(!player.hasSpokenToDrifter) {
                player.sendDrifterForceDialogue(n)
            } else {
                player.sendPrimalInfo()
            }
        }

        bind("Pet Info") { player, n ->
            if(!player.hasSpokenToDrifter) {
                player.sendDrifterForceDialogue(n)
            } else {
                player.sendPetInfo()
            }
        }

        bind("Upgrade Info") { player, n ->
            if(!player.hasSpokenToDrifter) {
                player.sendDrifterForceDialogue(n)
            } else {
                player.sendUpgradeInfo()
            }
        }

    }

    override fun getNPCs() = intArrayOf(DRIFTER)

    private fun Player.sendDrifterForceDialogue(drifter: NPC) {
        drifter.faceEntity(this)
        drifter.forceTalk = ForceTalk("Oi!")
        WorldTasksManager.schedule({
            dialogue {
                npc(
                    DRIFTER,
                    "You think you can just come in here and<br>know what to do? I remember a time when<br>a boi named Glab thought 'ey knew what<br>to do, then they mucked it all up."
                )
                npc(DRIFTER, "Whenever you're ready for a little chat, just<br>know I'll be waiting here for ya.")
            }
        }, 1)
    }

    private fun Player.sendPetInfo() {
        val entries = ArrayList<String>()
        options("Which type of pet would you like info about?") {
            "Base Pets" {
                for(pet in RemnantPetManager.standardPets) {
                    entries.add("")
                    entries.add("--- " + NPCDefinitions.get(pet.npcId).name + " ---")
                    for(perk in pet.perks) {
                        val lines = Book.splitIntoLine(perk.description, 55)
                        entries.addAll(listOf(*lines))
                    }
                }
                Diary.sendJournal(this@sendPetInfo, "Standard Remnant Pet Perks", entries)
            }
            "Primal Pets" {
                for(pet in RemnantPetManager.darkPets) {
                    entries.add("")
                    entries.add("--- " + NPCDefinitions.get(pet.npcId).name + " ---")
                    for(perk in pet.perks) {
                        val lines = Book.splitIntoLine(perk.description, 55)
                        entries.addAll(listOf(*lines))
                    }
                }
                Diary.sendJournal(this@sendPetInfo, "Primal Remnant Pet Perks", entries)
            }
        }
    }

    private fun Player.sendPrimalInfo() {
        val entries = ArrayList<String>()
        val valueMap = PrimalComponentsValues.valueMap
        for(primal in PrimalComponentsValues.locMap) {
            entries.add("" + ItemDefinitions.get(primal.key).name + " -- " + primal.value + " -- Pieces: " + (valueMap[primal.key]?.first ?: continue) + "-" + (valueMap[primal.key]?.last ?: continue))
        }
        Diary.sendJournal(this@sendPrimalInfo, "Primal Item Locations", entries)
    }

    private fun Player.sendUpgradeInfo() {
        val entries = ArrayList<String>()
        val lineSplitter = ArrayList<String>()
        options("Which tier of pet would you like info about?") {
            "Low Tier (15-20k rem)" {
                entries.add("Rates")
                entries.add("----------")
                lineSplitter.add("The base rate to successfully upgrade this pet is ${PrimalFission.tierToStartingOdds[PetTier.LOW]}%.")
                lineSplitter.add("This rate can be increased in two different ways. ")
                lineSplitter.add("For each failure, this rate increases by ${PrimalFission.tierToFailureBonus[PetTier.LOW]}%.")
                lineSplitter.add("Each component will raise the success chance by ${PrimalFission.tierToPrimalComponentBoost[PetTier.LOW]}%.")
                lineSplitter.add("Both boosts can increase the odds of success to a maximum of ${PrimalFission.tierToCappedSuccessRate[PetTier.LOW]}% for this tier.")
                lineSplitter.add("")
                lineSplitter.add("")
                lineSplitter.add("Upgrade Requirements")
                lineSplitter.add("--------------------")
                lineSplitter.add("Additionally, a set of primal components are required to attempt fission. ")
                lineSplitter.add("The minimum number for this tier is ${PrimalFission.tierToPrimalComponentRequirement[PetTier.LOW]} components.")
                lineSplitter.add("If you fail to combine your pet with primal components, you will lose your pet PERMANENTLY.")
                lineSplitter.add("A portion of both your remnant points and some of your components will be returned to your inventory on a failure.")
                for(line in lineSplitter) {
                    val lines = Book.splitIntoLine(line, 55)
                    entries.addAll(listOf(*lines))
                }
                Diary.sendJournal(this@sendUpgradeInfo, "Low Tier Upgrade Info", entries)
            }
            "Medium Tier (25k-35k rem)" {
                entries.add("Rates")
                entries.add("----------")
                lineSplitter.add("The base rate to successfully upgrade this pet is ${PrimalFission.tierToStartingOdds[PetTier.MEDIUM]}%.")
                lineSplitter.add("This rate can be increased in two different ways. ")
                lineSplitter.add("For each failure, this rate increases by ${PrimalFission.tierToFailureBonus[PetTier.MEDIUM]}%.")
                lineSplitter.add("Each component will raise the success chance by ${PrimalFission.tierToPrimalComponentBoost[PetTier.MEDIUM]}%.")
                lineSplitter.add("Both boosts can increase the odds of success to a maximum of ${PrimalFission.tierToCappedSuccessRate[PetTier.MEDIUM]}% for this tier.")
                lineSplitter.add("")
                lineSplitter.add("")
                lineSplitter.add("Upgrade Requirements")
                lineSplitter.add("--------------------")
                lineSplitter.add("Additionally, a set of primal components are required to attempt fission. ")
                lineSplitter.add("The minimum number for this tier is ${PrimalFission.tierToPrimalComponentRequirement[PetTier.MEDIUM]} components.")
                lineSplitter.add("If you fail to combine your pet with primal components, you will lose your pet PERMANENTLY.")
                lineSplitter.add("A portion of both your remnant points and some of your components will be returned to your inventory on a failure.")
                for(line in lineSplitter) {
                    val lines = Book.splitIntoLine(line, 55)
                    entries.addAll(listOf(*lines))
                }
                Diary.sendJournal(this@sendUpgradeInfo, "Medium Tier Upgrade Info", entries)
            }
            "High Tier (55k-175k rem)" {
                entries.add("Rates")
                entries.add("----------")
                lineSplitter.add("The base rate to successfully upgrade this pet is ${PrimalFission.tierToStartingOdds[PetTier.HIGH]}%.")
                lineSplitter.add("This rate can be increased in two different ways. ")
                lineSplitter.add("For each failure, this rate increases by ${PrimalFission.tierToFailureBonus[PetTier.HIGH]}%.")
                lineSplitter.add("Each component will raise the success chance by ${PrimalFission.tierToPrimalComponentBoost[PetTier.HIGH]}%.")
                lineSplitter.add("Both boosts can increase the odds of success to a maximum of ${PrimalFission.tierToCappedSuccessRate[PetTier.HIGH]}% for this tier.")
                lineSplitter.add("")
                lineSplitter.add("")
                lineSplitter.add("Upgrade Requirements")
                lineSplitter.add("--------------------")
                lineSplitter.add("Additionally, a set of primal components are required to attempt fission. ")
                lineSplitter.add("The minimum number for this tier is ${PrimalFission.tierToPrimalComponentRequirement[PetTier.HIGH]} components.")
                lineSplitter.add("If you fail to combine your pet with primal components, you will lose your pet PERMANENTLY.")
                lineSplitter.add("A portion of both your remnant points and some of your components will be returned to your inventory on a failure.")
                for(line in lineSplitter) {
                    val lines = Book.splitIntoLine(line, 55)
                    entries.addAll(listOf(*lines))
                }
                Diary.sendJournal(this@sendUpgradeInfo, "High Tier Upgrade Info", entries)
            }
            "Fissile Tier" {
                dialogue {
                    if(player.receivedFissileKratos) {
                        npcWithId(DRIFTER, "That's all I got for you for now, chief.<br>I'll send you a letter if I come across anything<br>else that might be worth your while.", Expression.CALM_TALK)
                        return@dialogue
                    }
                    if(!player.burnedDarkRoc && !player.burnedDarkKratos) {
                        npcWithId(DRIFTER, "You have no idea what you're getting into.<br>Talk to me once you've commited to the cause.<br>Chuck a Primal Kratos and Primal Roc<br>into the basin and then we can talk.", Expression.ON_ONE_HAND)
                        return@dialogue
                    }
                    if(player.burnedDarkRoc && player.burnedDarkKratos && !player.receivedFissileKratos) {
                        npcWithId(DRIFTER, "No way! You actually did it!?<br>That's absolutely wild, and here I was worried<br>you were going to glab' it up.<br>Have your prize and a little something from me.", Expression.GUFFAW)
                        player.receivedFissileKratos = true
                        val fissileKratos = Item(ItemId.PET_FISSILE_KRATOS)
                        player.collectionLog.add(fissileKratos)
                        player.inventory.addOrDrop(fissileKratos)
                        player.inventory.addOrDrop(Item(ItemId.REGAL_MYSTERY_BOX, 5))
                        WorldBroadcasts.sendMessage("${player.username} has just received Fissile Kratos! Double Drops have been activated for two hours!", BroadcastType.SUPER_RARE_DROP, true)
                        val endTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2)
                        val worldBoost = WorldBoost(WellPerk.DOUBLE_DROPS, endTime, TimeUnit.DAYS.toHours(1))
                        worldBoost.activate(false)
                        return@dialogue
                    }
                }
            }
        }
    }
}