package org.jesse.game.content.pvm_arena.loc

import org.jesse.game.content.pvm_arena.area.PvmArenaLobbyArea
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * Handles the exiting of the fight area of a PvM Arena.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class PvmArenaExitPortalPlugin : ObjectAction {

    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?,
    ) {
        player ?: return
        `object` ?: return
        player.dialogue {
            plain("Are you sure you wish to leave this area?<br>You will no longer receive any points for participating in the PvM Arena activity.")
            options("Leave this area?") {
                "Yes" {
                    PvmArenaLobbyArea.moveInto(player)
                }
                "No" {}
            }
        }
    }


    override fun getObjects(): Array<Any> =
        arrayOf(EXIT_PORTAL_27096)
}
