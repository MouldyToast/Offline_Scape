package org.jesse.game.content.tournament.loc

import org.jesse.game.content.tournament.Tournament
import org.jesse.game.content.tournament.TournamentManager
import org.jesse.game.obj.ids.*
import org.jesse.game.plugin.optionsMenu
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.impl.equipment.Equipment
import org.jesse.game.world.entity.player.cutscene.FadeScreen
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

@Suppress("unused")
class TournamentPortalObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val tournaments: List<Tournament> = TournamentManager.listActiveTournaments()
        if (tournaments.isEmpty()) {
            player.dialogue { plain("There are currently no active tournaments.") }
            return
        }
        when (option) {
            "View Tournaments" -> {
                player.optionsMenu(tournaments) { tournament ->
                    if(player.equipment.isEmpty())
                        teleportTo(player, tournament)
                    else
                        player.sendMessage("Please unequip all of your items before entering")
                }
            }
        }
    }

    private fun teleportTo(
        player: Player,
        singleTournament: Tournament,
    ) = FadeScreen(player) { singleTournament.lobby.teleportPlayer(player) }.fade(3, true)

    override fun getObjects(): Array<Any> = arrayOf(TOURNAMENT_PORTAL_60446)

    companion object {
        val LOCATION_IN_FRONT_OF_PORTAL = Location(3105, 3487, 0)
    }
}

private fun Equipment.isEmpty(): Boolean {
    return this.container.isEmpty
}
