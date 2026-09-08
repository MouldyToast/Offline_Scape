package org.jesse.game.content.tournament.spectating

import org.jesse.game.content.tournament.TournamentPair
import com.runespawn.util.weakMutableSetOf
import org.jesse.game.world.entity.player.Player

class TournamentSpectatorController(
    private val pair: TournamentPair
) {
    private val spectators = weakMutableSetOf<Player>()

    fun onFightOver() {
        spectators.forEach { it.interfaceHandler?.closeInterfaces() }
    }

    fun remove(player: Player) {
        TODO("Not yet implemented")
    }

    fun add(player: Player) {
        TODO("Not yet implemented")
    }
}
