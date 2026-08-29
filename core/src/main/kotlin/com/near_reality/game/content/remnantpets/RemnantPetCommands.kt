package com.near_reality.game.content.remnantpets

import com.google.common.eventbus.Subscribe
import com.zenyte.GameToggles
import com.zenyte.GameToggles.ORIGINS_PRIMAL_FIRE_ENABLED
import com.zenyte.GameToggles.ORIGINS_PRIMAL_WORKBENCH_ENABLED
import com.zenyte.game.content.Book
import com.zenyte.game.content.achievementdiary.Diary
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.events.ServerLaunchEvent
import com.zenyte.game.world.entity.player.dialogue.options
import mgi.types.config.npcs.NPCDefinitions
import java.util.*

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.25.2025
 */
object RemnantPetCommands {

    @Subscribe
    @JvmStatic
    fun onServerLaunch(event: ServerLaunchEvent?) {
        register()
    }

    fun register() {
        GameCommands.Command(PlayerPrivilege.TRUE_DEVELOPER, "disableprimalwb", "Killswitch for primal workbench") { p, _ ->
            ORIGINS_PRIMAL_WORKBENCH_ENABLED = !ORIGINS_PRIMAL_WORKBENCH_ENABLED
            p.sendMessage("Status: $ORIGINS_PRIMAL_WORKBENCH_ENABLED")
        }

            GameCommands.Command(PlayerPrivilege.TRUE_DEVELOPER, "disableprimalfire", "Killswitch for primal fire") { p, _ ->
                ORIGINS_PRIMAL_FIRE_ENABLED = !ORIGINS_PRIMAL_FIRE_ENABLED
                p.sendMessage("Status: $ORIGINS_PRIMAL_FIRE_ENABLED")
            }
        GameCommands.Command(PlayerPrivilege.PLAYER, "mypet", "Displays info about your current remnant pet and their boosts") { p, _ ->
            if(p.remnantPetManager.currentRemnantPet != RemnantPet.NonRemnantPet) {
                val entries = ArrayList<String>()
                p.remnantPetManager.activePerks().forEach {
                    if(!(it activeFor p)) {
                        val lines = Book.splitIntoLine(it.description + " (inactive)", 55)
                        entries.addAll(listOf(*lines))
                    } else {
                        val lines = Book.splitIntoLine(it.description, 55)
                        entries.addAll(listOf(*lines))
                    }
                }
                Diary.sendJournal(p, "Current Remnant Pet Perks", entries)
            } else {
                p.sendMessage("Your current pet is cosmetic only!")
            }
        }
        GameCommands.Command(PlayerPrivilege.PLAYER, "petinfo", "Displays info about remnant pets and their perks") { p, _ ->
            val entries = ArrayList<String>()
            p.options("Which type?") {
                "Base Pets" {
                    for(pet in RemnantPetManager.standardPets) {
                        entries.add("")
                        entries.add("--- " + NPCDefinitions.get(pet.npcId).name + " ---")
                        for(perk in pet.perks) {
                            val lines = Book.splitIntoLine(perk.description, 55)
                            entries.addAll(listOf(*lines))
                        }
                    }
                    Diary.sendJournal(p, "Standard Remnant Pet Perks", entries)
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
                    Diary.sendJournal(p, "Primal Remnant Pet Perks", entries)
                }
            }
        }
    }
}