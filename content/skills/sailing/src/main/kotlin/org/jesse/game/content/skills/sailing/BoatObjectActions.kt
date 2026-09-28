package org.jesse.game.content.skills.sailing

import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject
import kotlin.math.abs

/**
 * Base for deck facility loc actions. The loc ids come from the hulls' facility declarations ([BoatType.facilityIds])
 * and the approach / facing policy from the clicked boat's facility of [kind] ([BoatFacility]):
 * - [FacilityApproach.ON_OR_ADJACENT]: no walk route; the op runs only from the loc's tile or next to it.
 * - [FacilityApproach.WALK_TO]: the engine's normal walk-to-loc route (default [ObjectAction.handle]); its runnable
 *   faces the loc and runs [handleObjectAction] on the tick after arrival.
 * - [FacilityFacing.DECK_SOUTH]: every click on a loc of the deck the player stands on keeps them facing deck-south,
 *   as live does. Without this the engine's ObjectHandler turns them toward the clicked loc (the linen sail is north
 *   of the helm).
 * - [FacilityFacing.LOC]: face the loc (the walk-to runnable already does; ON_OR_ADJACENT faces it here).
 */
abstract class BoatFacilityObjectAction(private val kind: FacilityKind) : ObjectAction {

    override fun handle(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        val boat = Boats.at(`object`) ?: return
        val facility = boat.type.facility(kind) ?: return
        when (facility.approach) {
            FacilityApproach.WALK_TO -> super.handle(player, `object`, name, optionId, option)
            FacilityApproach.ON_OR_ADJACENT -> {
                when (facility.facing) {
                    FacilityFacing.DECK_SOUTH -> faceIfOnSameDeck(player, boat)
                    FacilityFacing.LOC -> player.faceObject(`object`)
                }
                if (withinReach(player, `object`)) {
                    handleObjectAction(player, `object`, name, optionId, option)
                }
            }
        }
    }

    override fun getObjects(): Array<Any> = BoatType.facilityIds(kind)

    private fun withinReach(player: Player, obj: WorldObject): Boolean =
        player.plane == obj.plane && abs(player.x - obj.x) <= 1 && abs(player.y - obj.y) <= 1

    private fun faceIfOnSameDeck(player: Player, boat: Boat) {
        if (Boats.at(player.location) === boat) {
            Sailing.faceDeckSouth(player)
        }
    }
}

/** Helm: op1 Navigate / Stop-navigating (multiloc on `sailing_boat_facility_lockedin`), op4 Escape. */
@Suppress("unused")
class BoatHelmObjectAction : BoatFacilityObjectAction(FacilityKind.HELM) {
    override fun handleObjectAction(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        val boat = Boats.at(`object`) ?: return
        when (optionId) {
            1 -> Sailing.toggleHelm(player, boat)
            else -> player.sendMessage("Nothing interesting happens.")
        }
    }
}

/** Linen sail (the op-bearing sail loc): op1 Trim (during a wind gust), op2 Set, op5 Un-set. */
@Suppress("unused")
class BoatSailsObjectAction : BoatFacilityObjectAction(FacilityKind.SAIL_LINEN) {
    override fun handleObjectAction(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        val boat = Boats.at(`object`) ?: return
        when (optionId) {
            2 -> Sailing.setSails(player, boat, true)
            5 -> Sailing.setSails(player, boat, false)
            1 -> Sailing.trim(player, boat)
            else -> player.sendMessage("Nothing interesting happens.")
        }
    }
}

/**
 * Cargo hold: a multiloc on `sailing_carrying_cargo` (varbit 19134). The client always sends the base id 60245, so
 * the variant is resolved here from the varbit: 0 -> `_no_cargo` (op1 Open, op2 Deposit-all, op5 Modify),
 * 1 -> `_cargo` (op1 Deposit-held, op2 Deposit-all, op5 Modify). op5 is hidden by the spawn opflags.
 * Only the captured ops are implemented (porttasks: Open t18-t20, Deposit-held t101-t103); op2 Deposit-all is not
 * captured yet.
 */
@Suppress("unused")
class BoatCargoHoldObjectAction : BoatFacilityObjectAction(FacilityKind.CARGO_HOLD) {
    override fun handleObjectAction(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String?) {
        val boat = Boats.at(`object`) ?: return
        when {
            optionId == 1 && CargoHold.isCarryingCargo(player) -> CargoHold.depositHeld(player, boat)
            optionId == 1 -> CargoHold.open(player, boat)
            else -> player.sendMessage("Nothing interesting happens.")
        }
    }
}