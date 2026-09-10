package org.jesse.game.content.tournament.spectating

import org.jesse.game.content.tournament.TournamentController
import org.jesse.game.content.tournament.restoreOriginalGameFrame
import org.jesse.game.content.tournament.tournamentPairSpectating
import org.jesse.game.content.tournament.tournamentSpectatorLocation
import org.jesse.game.GameInterface
import org.jesse.game.model.ui.Interface
import org.jesse.game.world.entity.player.Player
import java.util.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-12-11
 */
class SpectatingInventoryInterface : Interface() {

    override fun attach() {
        put(5, "Stop Viewing")
        put(6, "Center-Model")
        put(7, "North-West-Model")
        put(8, "North-East-Model")
        put(9, "South-East-Model")
        put(10, "South-West-Model")
        put(11, "Center")
        put(12, "North-West")
        put(13, "North-East")
        put(14, "South-East")
        put(15, "South-West")
    }

    override fun open(player: Player?) {
        player ?: return

        with(player.packetDispatcher) {
            // The middle model
            sendComponentModel(`interface`, getComponent("Center-Model"), -1)

            sendComponentVisibility(`interface`, getComponent("North-West-Model"), true)
            sendComponentVisibility(`interface`, getComponent("North-East-Model"), true)
            sendComponentVisibility(`interface`, getComponent("South-East-Model"), true)
            sendComponentVisibility(`interface`, getComponent("South-West-Model"), true)

            sendComponentVisibility(`interface`, getComponent("North-West"), true)
            sendComponentVisibility(`interface`, getComponent("North-East"), true)
            sendComponentVisibility(`interface`, getComponent("South-East"), true)
            sendComponentVisibility(`interface`, getComponent("South-West"), true)
        }

    }

    override fun build() {
        bind("Stop Viewing") { player: Player? -> this.close(player) }
    }

    override fun getInterface(): GameInterface =
        GameInterface.TOURNAMENT_SPECTATING_INVENTORY

    override fun close(player: Player, replacement: Optional<GameInterface?>?) {
        val tournament = TournamentController.Global.getTournamentIfActive()
        tournament ?: return
        player.tournamentPairSpectating = null
        player.tournamentSpectatorLocation = null
        player.interfaceHandler.closeInterface(GameInterface.TOURNAMENT_SPECTATING)
        restoreOriginalGameFrame(player)
        GameInterface.CHARACTER_SUMMARY.open(player)
        player.isHidden = false
        player.unlock()
        player.resetFreeze()
        player.packetDispatcher.freecam(false)
        with(player.packetDispatcher) {
            sendClientScript(deadman_spectator_enable_script_id, 0)
            sendClientScript(set_renderself_script_id, 1)
        }
        tournament.lobby.teleportPlayer(player)
    }
}