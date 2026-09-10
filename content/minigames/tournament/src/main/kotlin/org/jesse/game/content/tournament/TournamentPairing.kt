package org.jesse.game.content.tournament

import org.jesse.game.content.tournament.area.TournamentLobbyArea
import org.jesse.game.content.tournament.spectating.deadman_spectator_enable_script_id
import org.jesse.game.content.tournament.spectating.set_renderself_script_id
import com.runespawn.util.weakMutableSetOf
import com.runespawn.util.weakReference
import org.jesse.game.GameInterface
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player

class TournamentPair(
    first: Player,
    second: Player
) {
    val first by weakReference(first)
    val second by weakReference(second)
    val spectators = weakMutableSetOf<Player>()
    lateinit var spectatorLocation: Location

    fun canSpectate(): Boolean =
        this::spectatorLocation.isInitialized

    fun isSpectating(player: Player): Boolean =
        spectators.contains(player)

    fun addSpectator(player: Player) {
        spectators.add(player)
    }

    fun getOther(player: Player): Player? =
        if (first == player) second else first

    override fun toString(): String = buildString {
        append(first?.name)
        append(" vs ")
        append(second?.name)
    }

    fun cancelSpectators(lobby: TournamentLobbyArea) {
        val spectators = spectators.toSet()
        spectators.forEach { player ->
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
            lobby.teleportPlayer(player)
        }
        this.spectators.clear()
    }
}


fun restoreOriginalGameFrame(player: Player) {
    val interfaceHandler = player.interfaceHandler
    player.packetDispatcher.sendPane(interfaceHandler.pane)
    for (position in InterfacePosition.VALUES) {
        if (position.gameframeInterfaceId == -1 || position == InterfacePosition.NIGHTMARE_TOTEMS_POS || position == InterfacePosition.NOTIFICATION_POS || position == InterfacePosition.FRIENDS_TAB || position == InterfacePosition.JOURNAL_TAB_HEADER || position == InterfacePosition.ACCOUNT_MANAGEMENT) {
            continue
        }
        val gameInter = GameInterface.get(position.gameframeInterfaceId)
        if (gameInter.isPresent) {
            gameInter.get().open(player)
        } else {
            interfaceHandler.sendInterface(position, position.gameframeInterfaceId)
        }
    }
    interfaceHandler.openJournal()
}
internal fun MutableCollection<Player>.removePairs(): List<TournamentPair> {
    val shuffledPlayers = shuffled().run {
        if (size % 2 != 0)
            drop(1)
        else
            this
    }.toSet()
    removeAll(shuffledPlayers)
    return shuffledPlayers.chunked(2).map { TournamentPair(it[0], it[1]) }
}

internal fun Iterable<TournamentPair>.find(player: Player): TournamentPair? =
    firstOrNull { it.first == player || it.second == player }

internal fun Iterable<TournamentPair>.findOther(player: Player): Player? =
    find(player)?.getOther(player)
