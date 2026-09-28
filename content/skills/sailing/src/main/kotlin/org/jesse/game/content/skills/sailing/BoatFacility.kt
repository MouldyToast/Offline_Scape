package org.jesse.game.content.skills.sailing

import org.jesse.game.world.entity.worldentity.DeckLoc

/** What a deck facility is. Sailing code looks facilities up by kind on the boat's [BoatType]. */
enum class FacilityKind {
    /** The helm (steering). The helmsman stands on its tile. */
    HELM,

    /** The wood sail: animated with the plain sail anims, carries no op handler of its own. */
    SAIL_WOOD,

    /** The linen sail: animated with the `_offset` sail anims, the op-bearing sail loc (Trim / Set / Un-set). */
    SAIL_LINEN,

    /** The cargo hold. */
    CARGO_HOLD,
}

/** How the player gets to a facility before its op runs. */
enum class FacilityApproach {
    /**
     * No walk route: the op only runs when the player is on the loc's tile or next to it (Chebyshev distance 1).
     * The helmsman stands ON the helm (board teleport lands on the helm tile, op1 needs no movement: controls capture
     * t443) and the sails are next to it - a route to a loc on/next to the player's own tile could step them off the helm.
     */
    ON_OR_ADJACENT,

    /** The engine's normal walk-to-loc route; the op runs on the tick after arrival (cargo hold: porttasks t18 -> t19). */
    WALK_TO,
}

/** Which way the player faces when they click a facility. */
enum class FacilityFacing {
    /** Always deck-south on the player's own deck (helm, sails: see [Sailing.faceDeckSouth]). */
    DECK_SOUTH,

    /** Toward the loc (cargo hold: porttasks t19 faces (3, 2) from (3, 3)). */
    LOC,
}

/**
 * One facility on a hull: the single declaration its deck loc, its multiloc variants, its interaction policy and
 * its cache stats row come from. [BoatType] derives the template's deck list from these, the sailing anims look the
 * locs up here, and the object actions register [ids].
 *
 * @param loc the deck loc (id, shape, rotation, deck-local position, spawn opflags).
 * @param variants multiloc variant loc ids. The client always sends the BASE id ([DeckLoc.id]) - every cargo hold
 * click in porttasks.txt arrives as 60245 even while the `_cargo` variant is showing - so the variants are only
 * registered defensively; an action resolves the variant from the varbit itself.
 * @param stats the facility's `sailing_boat_facility_stats` row, or null (the helm has none in the dump).
 */
class BoatFacility(
    val kind: FacilityKind,
    val loc: DeckLoc,
    val variants: IntArray = IntArray(0),
    val approach: FacilityApproach,
    val facing: FacilityFacing,
    val stats: FacilityStats? = null,
) {
    /** Every loc id a click on this facility can arrive with. */
    val ids: List<Int>
        get() = listOf(loc.id) + variants.toList()
}

/**
 * A facility's row in dbtable `sailing_boat_facility_stats` (CLIENTSIDE columns, rev-240 dump.dbrow), transcribed.
 * The loc -> stats row link is clientside too: the `sailing_boat_sail` / `sailing_boat_facility` rows carry both
 * `loc` and `facility_stats` (dump.dbrow 8299: `sailing_boat_sail_kandarin_1x3_wood` -> 8182; 8462:
 * `sailing_boat_cargo_hold_regular_raft` -> 8239). Columns the row does not set are 0 (the table default).
 *
 * Transcribed, not read at runtime: `DBRowDefinition` does decode dbrows, but by column index, and the index order
 * is only inferred from the dump's column listing.
 */
class FacilityStats(
    val row: Int,
    val speedBoostDuration: Int = 0,
    val cargoHoldSize: Int = 0,
)

/**
 * A hull's base stats row in `sailing_boat_facility_stats` (the `sailing_boat_hull` row's `facility_stats`,
 * dump.dbrow 8264 `sailing_boat_regular_raft_base` -> 8161), transcribed like [FacilityStats].
 */
class HullStats(
    val row: Int,
    val hpMax: Int,
    val baseSpeed: Int,
    val speedCap: Int,
)
