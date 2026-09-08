package org.jesse.game.content.tournament.spectating

import org.jesse.game.world.PlayerEvent
import org.jesse.game.world.WorldEventListener

object TournamentSpectatorUpdateHook : WorldEventListener<PlayerEvent.Update> {

    override fun on(event: PlayerEvent.Update) {
        val player = event.player
        if (player.temporaryAttributes.containsKey("tournament_spectating"))
            TournamentViewerInterface.refreshSpectator(player)
    }
}
