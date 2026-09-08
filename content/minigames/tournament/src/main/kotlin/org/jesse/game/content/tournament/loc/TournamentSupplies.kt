package org.jesse.game.content.tournament.loc

import org.jesse.game.content.tournament.preset.TournamentPreset
import org.jesse.game.content.tournament.tournamentOrNull
import org.jesse.game.GameInterface
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * Handles the Tournament Supplies object action, which opens the Tournament Presets interface.
 *
 * @author Tommeh | 01/06/2019 | 14:50
 * @author Stan van der Bend
 */
@Suppress("unused")
class TournamentSupplies : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        when(player.tournamentOrNull?.preset) {
                TournamentPreset.F2P_PURE,
                TournamentPreset.MYSTERY_BOX,
                TournamentPreset.BOXING,
                TournamentPreset.DDS -> {
                    player.sendMessage("You are not able to access these supplies during this variant.")
                    return
                }
            else -> {}
        }
        if (option == "View")
            GameInterface.TOURNAMENT_PRESETS.open(player)
    }

    override fun getObjects(): Array<Any> =
        arrayOf(TOURNAMENT_SUPPLIES, TOURNAMENT_SUPPLIES_35007)
}
