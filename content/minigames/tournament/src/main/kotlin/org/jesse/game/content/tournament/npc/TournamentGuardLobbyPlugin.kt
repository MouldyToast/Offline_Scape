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
 * Represents a [NPCPlugin] that handles the [TournamentGuardLobbyPlugin] [NPC] interactions.
 *
 * @author Tommeh | 31/05/2019 | 20:00
 * @author Stan van der Bend
 */
@Suppress("unused")
class TournamentGuardLobbyPlugin : NPCPlugin() {

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
        TournamentGuardLobby.handleSpectatorRequest(player, false, npc)

    private fun startTalkToDialogue(player: Player, npc: NPC) =
        TournamentGuardLobby.sendInitialDialogue(player, npc)

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
    }
}
