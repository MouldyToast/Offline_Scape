package org.jesse.game.content.boss.nex.`object`.actions

import org.jesse.game.content.commands.DeveloperCommands
import org.jesse.game.content.boss.nex.AncientChamberArea
import org.jesse.game.content.boss.nex.NexModule
import org.jesse.game.content.boss.nex.nexGodwarsInstance
import org.jesse.game.content.boss.nex.showAncientBarrierDialogue
import org.jesse.game.content.ItemRetrievalService.RetrievalServiceType.ANCIENT_PRISON
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.cutscene.FadeScreen
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.scripts.`object`.actions.ObjectActionScript
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.*

class AncientBarrierObjectaction : ObjectActionScript() {

    fun Player.enterNex() {
        FadeScreen(this) {
            if (nexGodwarsInstance != null) {
                nexGodwarsInstance!!.tryStartSpawnTimer()
                teleport(Location(nexGodwarsInstance!!.getX(2910), nexGodwarsInstance!!.getY(5203)))
            } else {
                NexModule.tryStartSpawnTimer()
                teleport(Location(2910, 5203, 0))
            }
        }.fade(2)
    }

    init {
        /**
         * NOTE: The abstraction inside the original Zenyte GWD areas, specifically
         * [org.jesse.game.content.godwars.objects.GodwarsBossDoorObject] does not allow separation into multiple doors.
         *
         * Inside nex chamber there are two locations, the actual combat area and the
         * safe area with the bank.
         *
         * Zenyte logic takes care of entering the safe area which takes the essence.
         * Here we are handling the barrier area which just enters the nex combat area.
         **/
        42967 {
            if (!DeveloperCommands.enabledNex) {
                player.dialogue { plain("Nex is currently disabled.") }
            } else if (option == "Pass") {
                if (player.x >= obj.x) {
                    player.dialogue { plain("You can only exit by using the altar.") }
                } else {
                    val service = player.retrievalService
                    if (service.type == ANCIENT_PRISON && !service.container.isEmpty) {
                        player.options("A nearby chest has some of your items. Do you still wish to proceed?") {
                            dialogueOption("Yes.", noPlayerMessage = true) {
                                player.enterNex()
                            }
                            dialogueOption("No.", noPlayerMessage = true) {}
                        }
                    } else if (player.nexGodwarsInstance == null && !NexModule.isNexSpawned() || player.nexGodwarsInstance != null && !player.nexGodwarsInstance!!.isNexSpawned()) {
                        if (player.showAncientBarrierDialogue) {
                            player.dialogue {
                                plain(
                                    "You are about to begin an encounter with Nex. Dying during this " +
                                            "encounter will not be considered a safe death. After entering, you " +
                                            "won't be able to leave using the barrier. Instead, you'll need to use " +
                                            "the alter to teleport out. Are you sure you wish to begin?"
                                )
                                options("Are you sure you wish to begin?") {
                                    dialogueOption("Yes.", noPlayerMessage = true) {
                                        player.enterNex()
                                    }
                                    dialogueOption("Yes, and don't ask again.", noPlayerMessage = true) {
                                        player.showAncientBarrierDialogue = false
                                        player.enterNex()
                                    }
                                    dialogueOption("No.", noPlayerMessage = true) {}
                                }
                            }
                        } else
                            player.enterNex()
                    } else
                        player.dialogue { plain("A group is already fighting Nex. You'll have to wait until they are done.") }
                }
            } else if (option == "Peek") {
                player.dialogue {
                    var players: Int
                    var status: String
                    if (player.nexGodwarsInstance != null) {
                        players = player.nexGodwarsInstance!!.playersInsideInt()
                        status = player.nexGodwarsInstance!!.getNexStatus()
                    } else {
                        players = AncientChamberArea.countPlayersInPrison()
                        status = NexModule.getNexStatus()
                    }

                    var message = if (players == 1) "There is 1 adventurer on the other side of the barrier." else "There are $players adventurers on the other side of the barrier."

                    if (status.isNotEmpty()) {
                        message += "<br>Nex is imbued with $status."
                    }

                    plain(message)
                }
            }
        }
    }
}
