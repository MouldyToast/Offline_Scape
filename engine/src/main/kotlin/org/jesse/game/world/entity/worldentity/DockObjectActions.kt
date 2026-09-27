package org.jesse.game.world.entity.worldentity

import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * Dock gangplanks (`sailing_gangplank_<dock>`, multiloc on `sailing_player_is_on_player_boat`):
 * - from land, op1 "Board": walk to the gangplank as usual, then board (spawning the boat if needed);
 * - from a deck, op1 "Disembark": the player cannot path from the deck instance to a root tile, so the
 *   route is skipped and the dock range check replaces it.
 */
@Suppress("unused")
class DockGangplankObjectAction : ObjectAction {
    override fun handle(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        val boat = WorldEntities.atTile(player.location)
        if (boat != null) {
            val dock = Dock.byGangplank(`object`.id, `object`.x, `object`.y) ?: return
            if (optionId == 1) {
                Docking.disembarkAtDock(player, boat, dock)
            }
            return
        }
        super.handle(player, `object`, name, optionId, option)
    }

    override fun handleObjectAction(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        val dock = Dock.byGangplank(`object`.id, `object`.x, `object`.y) ?: return
        when (optionId) {
            1 -> Docking.boardAtDock(player, dock)
            else -> player.sendMessage("Nothing interesting happens.")
        }
    }

    override fun getObjects(): Array<Any> = Dock.gangplankIds
}
