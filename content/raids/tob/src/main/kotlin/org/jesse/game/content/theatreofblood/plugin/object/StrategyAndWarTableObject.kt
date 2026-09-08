package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.GameInterface
import org.jesse.game.content.theatreofblood.VerSinhazaArea
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * Both the Strategy and War tables are found in Verzik Vitur treasure vault at the end of the Theatre of Blood.
 *
 * The strategy table can only be interacted with by participants.
 * The war table can only be interacted with by spectators.
 *
 * Reading the table will open an interface containing details about the players' performance in the theatre.
 * The interface contains information about the number of times each player died,
 * the time spent in each room, and who the Most Valuable Player (MVP) was.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class StrategyAndWarTableObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (option == "Read") {
            val party = VerSinhazaArea.getParty(player,
                checkMembers = obj.id == STRATEGY_TABLE,
                checkViewers = false,
                checkSpectators = obj.id == WAR_TABLE
            )
            if (party != null) {
                GameInterface.TOB_PERFORMANCE_DETAILS.open(player)
            } else {
                player.sendDeveloperMessage("NOT in a party")
            }
        }
    }

    override fun getObjects() = arrayOf(
        STRATEGY_TABLE,
        WAR_TABLE
    )
}
