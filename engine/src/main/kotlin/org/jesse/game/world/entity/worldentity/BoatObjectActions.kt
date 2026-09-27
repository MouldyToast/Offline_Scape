package org.jesse.game.world.entity.worldentity

import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject
import kotlin.math.abs

/**
 * Deck loc actions. Both skip the usual walk-to-object route: the helmsman stands ON the helm tile
 * (live: board teleport lands on the helm tile and op1 needs no movement, capture t443), and the sails
 * are adjacent to it. Routing to a loc on/next to the player's own tile could step them off the helm.
 */
private fun withinReach(player: Player, obj: WorldObject): Boolean =
    player.plane == obj.plane && abs(player.x - obj.x) <= 1 && abs(player.y - obj.y) <= 1

/** Helm: op1 Navigate / Stop-navigating (multiloc on `sailing_boat_facility_lockedin`), op4 Escape. */
@Suppress("unused")
class BoatHelmObjectAction : ObjectAction {
    override fun handle(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        if (!withinReach(player, `object`)) {
            return
        }
        handleObjectAction(player, `object`, name, optionId, option)
    }

    override fun handleObjectAction(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        val entity = WorldEntities.atTile(`object`) ?: return
        when (optionId) {
            1 -> Sailing.toggleHelm(player, entity)
            else -> player.sendMessage("Nothing interesting happens.")
        }
    }

    // Raft helm multiloc: base sailing_boat_steering_kandarin_1x3_wood (59554) and its variants
    // _in_use (59555) / _idle (59556), in case the click resolves to the transmogrified id.
    override fun getObjects(): Array<Any> = arrayOf(59554, 59555, 59556)
}

/** Sails: op1 Trim (during a wind gust), op2 Set, op5 Un-set. */
@Suppress("unused")
class BoatSailsObjectAction : ObjectAction {
    override fun handle(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        if (!withinReach(player, `object`)) {
            return
        }
        handleObjectAction(player, `object`, name, optionId, option)
    }

    override fun handleObjectAction(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        val entity = WorldEntities.atTile(`object`) ?: return
        when (optionId) {
            2 -> Sailing.setSails(player, entity, true)
            5 -> Sailing.setSails(player, entity, false)
            1 -> Sailing.trim(player, entity)
            else -> player.sendMessage("Nothing interesting happens.")
        }
    }

    // sailing_boat_sail_kandarin_1x3_linen (raft sails - the op-bearing sail loc).
    override fun getObjects(): Array<Any> = arrayOf(29506)
}