package org.jesse.game.content.tournament.npc

import org.jesse.game.content.tournament.TournamentState
import org.jesse.game.content.tournament.tournamentOrNull
import org.jesse.game.GameInterface
import org.jesse.game.util.Direction
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.actions.NPCPlugin
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.region.DynamicArea

/**
 * Represents a [NPCPlugin] that handles the [TournamentGuardLobby] [NPC] interactions.
 *
 * @author Tommeh | 31/05/2019 | 20:00
 * @author Stan van der Bend
 */
@Suppress("unused")
class TournamentGuardLobby : NPCPlugin() {

    override fun handle() {
        bind("Talk-to", ::startTalkToDialogue)
        bind("Spectate", ::tryOpenSpectatorView)
    }

    private fun bind(option: String, action: (Player, NPC) -> Unit) {
        bind(option, object : OptionHandler {
            override fun handle(player: Player, npc: NPC) {
                action(player, npc)
            }
            override fun click(player: Player, npc: NPC, option: NPCOption) {
                player.routeEvent = findRoute(player, npc)
            }
        })
    }

    private fun tryOpenSpectatorView(player: Player, npc: NPC) =
        handleSpectatorRequest(player, false, npc)

    private fun startTalkToDialogue(player: Player, npc: NPC) =
        sendInitialDialogue(player, npc)

    private fun OptionHandler.findRoute(
        player: Player,
        npc: NPC,
    ) = TileEvent(
        player,
        TileStrategy(npc.location.transform(Direction.getDirection(npc.location, player.location), 1), 0)
    ) {
        player.stopAll()
        player.faceEntity(npc)
        this.handle(player, npc)
    }

    override fun getNPCs(): IntArray =
        intArrayOf(TOURNAMENT_GUARD)

    companion object {
        private val BASE_SPAWN_LOCATION = Location(3357, 7446, 0)

        fun spawn(area: DynamicArea) {
            World.spawnNPC(TOURNAMENT_GUARD, area.getLocation(BASE_SPAWN_LOCATION), Direction.EAST, 0)
        }

        fun handleSpectatorRequest(player: Player, fromOptionPrompt: Boolean, npc: NPC) {
            player.dialogue(npc) {
                if (!player.interfaceHandler.isResizable)
                    npc("You cannot spectate other fights while on fixed mode. Switch to resizable mode first.")
                else
                    when (player.tournamentOrNull?.state) {
                        is TournamentState.Scheduled ->
                            npc("The tournament has not started yet. It will start soon enough.")
                        is TournamentState.RoundActive ->
                            if (fromOptionPrompt)
                                npc("Yes of course! Just select a fight you would like to view.")
                                    .executeAction { GameInterface.TOURNAMENT_VIEWER.open(player) }
                            else
                                GameInterface.TOURNAMENT_VIEWER.open(player)
                        else ->
                            npc("The tournament has unfortunately already ended. Come back some other time.")
                    }
            }
        }

        fun sendInitialDialogue(player: Player, npc: NPC) {
            player.dialogue(npc) {
                npc("Greetings warrior, I\'m the Tournament Guard. What can I do for you?")
                options {
                    dialogueOption("Can you tell me more about the tournament?") {
                        npc("Yes of course! Every once in awhile a tournament will be started. Everyone is able to join and you will be granted a reward if you want to win all rounds!")
                        npc("Every tournament comes with a selected preset load-out. All the participants will be using this preset during their fights.")
                        npc("While you\'re in the lobby, you will however be able to take supplies like runes, food, potions and special-attack weapons from the Tournament Merchant.")
                    }
                    dialogueOption("Can I spectate a fight?") {
                        handleSpectatorRequest(player, true, npc)
                    }
                    "Nevermind."()
                }
            }
        }
    }
}
