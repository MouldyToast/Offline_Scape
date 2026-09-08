package com.near_reality.game.content.tournament.npc

import com.near_reality.game.content.tournament.TournamentState
import com.near_reality.game.content.tournament.tournamentOrNull
import com.zenyte.game.GameInterface
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.npc.ids.*
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.region.DynamicArea

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
