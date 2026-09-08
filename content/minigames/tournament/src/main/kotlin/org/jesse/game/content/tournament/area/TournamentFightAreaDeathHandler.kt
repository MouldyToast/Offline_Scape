package org.jesse.game.content.tournament.area

import org.jesse.game.content.tournament.Tournament
import org.jesse.game.content.tournament.TournamentState
import org.jesse.game.content.tournament.findOther
import org.jesse.game.plugin.safeDeaths
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.area.plugins.DeathPlugin

class TournamentFightAreaDeathHandler(
    private val tournament: Tournament
) : DeathPlugin by safeDeaths(
    information = "Items lost inside tournament-restricted areas are erased when the user leaves the tournament, which includes dying.",
    respawnLocationProvider = { tournament.lobby.getLocation(TournamentLobbyArea.RESPAWN_LOCATION) }
) {

    override fun sendDeath(player: Player?, source: Entity?): Boolean {
        player?:return false
        player.sendDeveloperMessage("sending death (state=${tournament.state::class.simpleName})")
        when(val state = tournament.state) {
            is TournamentState.RoundActive -> {
                val opponent = state.combatPairs.findOther(player)
                if (opponent != null) {
                    opponent.blockIncomingHits(5)
                    opponent.reset()
                    opponent.graphics = Graphics(1177)
                    opponent.sendDeveloperMessage("You have been granted immunity for 5 ticks")
                }
            }
            else -> Unit
        }
        player.animation = null
        player.lock()
        player.stopAll()
        if (player.prayerManager.isActive(Prayer.RETRIBUTION)) {
            player.prayerManager.applyRetributionEffect(source)
        }
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                if (player.isFinished || player.isNulled) {
                    stop()
                    return
                }
                when (ticks) {
                    1 -> {
                        player.animation = Player.DEATH_ANIMATION
                    }

                    4 -> {
                        try {
                            player.sendMessage("Oh dear, you have died.")
                            player.reset()
                            player.animation = Animation.STOP
                            player.variables.setSkull(false)
                        } finally {
                            tournament.removeFromCombatPairs(player, true)
                        }
                        player.setLocation(tournament.lobby.getLocation(TournamentLobbyArea.RESPAWN_LOCATION))
                    }

                    5 -> {
                        player.unlock()
                        player.animation = Animation.STOP
                        stop()
                    }
                }
                ticks++
            }
        }, 0, 0)
        return true
    }
}
