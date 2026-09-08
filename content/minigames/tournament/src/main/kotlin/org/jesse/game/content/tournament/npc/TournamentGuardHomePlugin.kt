package org.jesse.game.content.tournament.npc

import org.jesse.game.content.tournament.TournamentManager
import org.jesse.game.plugin.optionsMenu
import org.jesse.game.GameInterface
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.actions.NPCPlugin
import org.jesse.game.world.entity.npc.actions.NPCPlugin.OptionHandler
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Represents the Tournament Guard NPC at home.
 *
 * @author Tommeh | 07/06/2019 | 00:01
 * @author Stan van der Bend
 */
@Suppress("unused")
class TournamentGuardHomePlugin : NPCPlugin() {

    override fun handle() {
        bind("Talk-to", ::startDialogue)
        bind("Trade", ::openTournamentShop)
        bind("View Tournaments", ::viewTournaments)
    }

    private fun startDialogue(player: Player, npc: NPC) =
        sendInitialDialogue(player, npc)


    companion object {
            fun sendInitialDialogue(player: Player, npc: NPC) {

                player.dialogue(npc) {
                    npc("Greetings warrior, I'm the Tournament Guard. What can I do for you?")
                    options {
                        dialogueOption("Can you tell me more about the tournament system?") {
                            npc("Yes of course! Every once in awhile a tournament will be started. Everyone is able to join and you will be granted a reward if you want to win all rounds!")
                            npc("Every tournament comes with a selected preset load-out. All the participants will be using this preset during their fights.")
                            npc("While you're in the lobby, you will however be able to take supplies like runes, food, potions and special-attack weapons from the Tournament Merchant.")
                        }
                        dialogueOption("Can I view the current tournaments?") {
                            npc("Yes of course! Here's an overview.")
                                .executeAction { viewTournaments(player, npc) }
                        }
                        dialogueOption("Nevermind")
                    }
                }
            }

            private fun viewTournaments(player: Player, @Suppress("UNUSED_PARAMETER") npc: NPC) {
                player.optionsMenu(TournamentManager.listActiveTournaments()) { tournament ->
                    tournament.lobby.teleportPlayer(player)
                }
            }
        }

    private fun openTournamentShop(player: Player, @Suppress("UNUSED_PARAMETER") ignored: NPC) =
        GameInterface.TOURNAMENT_SHOP.open(player)

    private fun bind(option: String, action: (Player, NPC) -> Unit) {
        bind(option, OptionHandler { player, npc -> action(player, npc) })
    }


    override fun getNPCs(): IntArray =
        intArrayOf(TOURNAMENT_GUARD_16012)
}
