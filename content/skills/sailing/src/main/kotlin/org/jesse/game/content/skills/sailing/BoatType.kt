package org.jesse.game.content.skills.sailing

import org.jesse.game.world.entity.worldentity.DeckLoc
import org.jesse.game.world.entity.worldentity.WorldEntityTemplate

/**
 * A sail animation set for one sail loc: the steady anim per sail state and the transition between each pair.
 * Every state change plays the transition this tick and the steady anim the next tick
 * (Sailing_around_controls capture: t174-175 down->half, t190-191 half->full, t137-138 full->half, t139-140 half->down).
 */
data class SailAnimSet(
    val down: Int,
    val half: Int,
    val full: Int,
    val downToHalf: Int,
    val downToFull: Int,
    val halfToDown: Int,
    val halfToFull: Int,
    val fullToDown: Int,
    val fullToHalf: Int,
)

/**
 * Helm / sail animation ids for a hull (raft ids from the live captures: `human_sailing_alpha_helm_raft01_*`,
 * `sailing_alpha_helm_raft01_*`, `sailing_boat_sail_kandarin_1x3_*`). Which locs they play on comes from the hull's
 * facilities ([BoatType.helmLoc], [BoatType.sailALoc], [BoatType.sailBLoc]).
 * The wood sail (sail A) plays the plain anims, the linen sail (sail B, the op-bearing loc) the `_offset` variants.
 * The helm loop anims are re-sent every 10 ticks while navigating (controls capture t47, 57+10, 73+10, ...).
 */
data class SailingAnims(
    val playerHelmStart: Int,
    val playerHelmLoop: Int,
    val helmLocStart: Int,
    val helmLocLoop: Int,
    val helmLocInactive: Int,
    val sailAAnims: SailAnimSet,
    val sailBAnims: SailAnimSet,
    val playerTrimStart: Int,
    val playerTrimLoop: Int,
    val playerTrimEnd: Int,
    val helmLocTrimStart: Int,
    val helmLocTrimLoop: Int,
    val helmLocTrimEnd: Int,
    /** Spotanim on sail B during a gust while the boat still moves faster than half speed (`vfx_wind_sail_raft01_full01`). */
    val gustGraphic: Int,
    /** Spotanim on sail B during a gust at half speed or less (`vfx_wind_sail_raft01_half01`, controls capture t139). */
    val gustGraphicHalf: Int,
    /** Spotanim on sail B while boosted (`vfx_wind_sail_raft01_speedboost01`). */
    val boostGraphic: Int,
)

/*
 * Raft facilities. Deck positions and spawn opflags from the rebuild tick (controls capture t32; Pandemonium capture
 * lines 14660-14671): helm and cargo hold ops 1-4 (op5 "Modify" hidden), both sails only op2 "Set" while down.
 * Multilocs from rev-240 dump.loc: the helm on `sailing_boat_facility_lockedin` (0/2 -> _idle 59556, 1/3 -> _in_use
 * 59555), the cargo hold on `sailing_carrying_cargo` (0 -> _no_cargo 60577 "Open", 1 -> _cargo 60581 "Deposit-held").
 * List order is the deck spawn order (unchanged from before the facility refactor).
 */
private val RAFT_FACILITIES = listOf(
    BoatFacility(
        FacilityKind.HELM,
        DeckLoc(59554, 10, 0, 3, 4, 1, 0b1111), // sailing_boat_steering_kandarin_1x3_wood
        variants = intArrayOf(59555, 59556),
        approach = FacilityApproach.ON_OR_ADJACENT,
        facing = FacilityFacing.DECK_SOUTH,
    ),
    BoatFacility(
        FacilityKind.SAIL_WOOD,
        DeckLoc(59530, 10, 0, 3, 3, 1, 0b10), // sailing_boat_sail_kandarin_1x3_wood
        approach = FacilityApproach.ON_OR_ADJACENT,
        facing = FacilityFacing.DECK_SOUTH,
        // dbrow 8299 sailing_boat_regular_mast_raft (loc = this sail) -> 8182 sailing_boat_regular_mast_raft_stats:
        // boat_stormresistance 0, boat_speedboost_duration 20.
        stats = FacilityStats(8182, speedBoostDuration = 20),
    ),
    BoatFacility(
        FacilityKind.SAIL_LINEN,
        DeckLoc(29506, 10, 0, 3, 5, 1, 0b10), // sailing_boat_sail_kandarin_1x3_linen
        approach = FacilityApproach.ON_OR_ADJACENT,
        facing = FacilityFacing.DECK_SOUTH,
    ),
    BoatFacility(
        FacilityKind.CARGO_HOLD,
        DeckLoc(60245, 10, 0, 3, 2, 1, 0b1111), // sailing_boat_cargo_hold_regular_raft
        variants = intArrayOf(60577, 60581),
        approach = FacilityApproach.WALK_TO,
        facing = FacilityFacing.LOC,
        // dbrow 8462 sailing_boat_facility_cargo_hold_regular_raft -> 8239 sailing_boat_regular_cargohold_raft_stats:
        // boat_cargohold_size 20 (the hold's capacity text, porttasks t19 943:5 = "20").
        stats = FacilityStats(8239, cargoHoldSize = 20),
    ),
)

/** Raft deck locs that are not facilities: ambient sound and invisible locs (keep the default all-ops opflags). */
private val RAFT_DECOR = listOf(
    DeckLoc(32545, 22, 0, 2, 3, 1), // invisible_type0_nonblocking
    DeckLoc(58569, 22, 0, 4, 3, 1), // randomsound_ardent_ocean_gulls
    DeckLoc(58526, 22, 0, 2, 4, 1), // bgsound_sailing_ocean_water_loop_01
    DeckLoc(58568, 22, 0, 4, 4, 1), // randomsound_ardent_ocean_crashing_waves
    DeckLoc(32545, 22, 0, 2, 2, 1), // invisible_type0_nonblocking
    DeckLoc(32545, 22, 0, 4, 2, 1), // invisible_type0_nonblocking
    DeckLoc(32545, 22, 0, 2, 5, 1), // invisible_type0_nonblocking
    DeckLoc(32545, 22, 0, 4, 5, 1), // invisible_type0_nonblocking
)

/**
 * dbrow 8264 sailing_boat_regular_raft_base (loc `sailing_boat_hull_kandarin_1x3_wood`) -> 8161
 * sailing_boat_regular_raft_base_stats: boat_hp_max 20, boat_basespeed 192, boat_speedcap 320 (all three match what
 * the live server sends on boarding).
 */
private val RAFT_HULL_STATS = HullStats(8161, hpMax = 20, baseSpeed = 192, speedCap = 320)

/** The deck is the facilities' locs followed by the decor, in that order. */
private fun deckOf(facilities: List<BoatFacility>, decor: List<DeckLoc>): List<DeckLoc> =
    facilities.map { it.loc } + decor

/**
 * Boat hulls: the engine [WorldEntityTemplate] each hull is built from, plus its sailing data.
 *
 * Verified sources (template):
 * - `configId` / `activeLevel`: rev-240 cache `dump.worldentity` (`[worldentity_N]`, `mainlevel=1`).
 * - `templateZoneX/Z`: south-west template zone copied by `rebuild_worldentity_v4` in the live
 *   Pandemonium capture (build_area `source=` coords >> 3). All three live in map square (60,100);
 *   the hulls are static locs of that square (raft: `sailing_boat_hull_kandarin_1x3_wood` 59494 at (3843,6458,0)).
 * - `sizeX/sizeZ`: size in zones (RSProx prints tiles: raft/skiff 8x8, sloop 8x16).
 * - `boardDx/boardDz`: deck tile the player is teleported onto when boarding
 *   (capture t439: (15555,14276,1) = instance base (15552,14272) + (3,4) - the helm tile).
 * - `deck`: derived from [facilities] + the hull's decor locs (see [deckOf]). Only the raft is transcribed so far;
 *   skiff/sloop decks come with their content.
 */
enum class BoatType(
    val template: WorldEntityTemplate,
    val facilities: List<BoatFacility>,
    val hullStats: HullStats,
    val anims: SailingAnims?,
) {

    RAFT(
        WorldEntityTemplate(
            configId = 1,
            templateZoneX = 3840 shr 3,
            templateZoneZ = 6456 shr 3,
            sizeX = 1,
            sizeZ = 1,
            activeLevel = 1,
            boardDx = 3,
            boardDz = 4,
            deck = deckOf(RAFT_FACILITIES, RAFT_DECOR),
            // worldentity_1: boundssizex=128, boundssizez=384 (1 x 3 tiles, centred on the pivot).
            boundsSizeX = 128,
            boundsSizeZ = 384,
        ),
        RAFT_FACILITIES,
        RAFT_HULL_STATS,
        SailingAnims(
            playerHelmStart = 13340, playerHelmLoop = 13341,
            helmLocStart = 13335, helmLocLoop = 13336, helmLocInactive = 13334,
            // sailing_boat_sail_kandarin_1x3_{down,half,full,down_to_half,down_to_full,half_to_down,half_to_full,
            // full_to_down,full_to_half} (+ _offset on the linen sail).
            sailAAnims = SailAnimSet(
                down = 13367, half = 13370, full = 13373,
                downToHalf = 13371, downToFull = 13374,
                halfToDown = 13368, halfToFull = 13375,
                fullToDown = 13369, fullToHalf = 13372,
            ),
            sailBAnims = SailAnimSet(
                down = 13875, half = 13878, full = 13881,
                downToHalf = 13879, downToFull = 13882,
                halfToDown = 13876, halfToFull = 13883,
                fullToDown = 13877, fullToHalf = 13880,
            ),
            playerTrimStart = 13342, playerTrimLoop = 13343, playerTrimEnd = 13344,
            helmLocTrimStart = 13337, helmLocTrimLoop = 13338, helmLocTrimEnd = 13339,
            gustGraphic = 3529, gustGraphicHalf = 3528, boostGraphic = 3530,
        ),
    ),

    // Skiff board tile verified: capture t74 teleport to (15556,14276,1) = base + (4,4).
    // worldentity_2 bounds 256 x 640 (2 x 5 tiles); collision unverified for this hull.
    // No facilities or stats transcribed yet: movement uses the raft's stats (as before the refactor).
    SKIFF(
        WorldEntityTemplate(
            2, 3840 shr 3, 6400 shr 3, 1, 1, 1, 4, 4, emptyList(),
            boundsSizeX = 256, boundsSizeZ = 640,
        ),
        emptyList(),
        RAFT_HULL_STATS,
        null,
    ),

    // Sloop board tile UNVERIFIED (never boarded in the capture) - deck centre placeholder.
    // worldentity_3 bounds 384 x 1280 (3 x 10 tiles), boundsoffsetz=-256 (sign unverified).
    SLOOP(
        WorldEntityTemplate(
            3, 3864 shr 3, 6432 shr 3, 1, 2, 1, 4, 8, emptyList(),
            boundsSizeX = 384, boundsSizeZ = 1280, boundsOffsetZ = -256,
        ),
        emptyList(),
        RAFT_HULL_STATS,
        null,
    ),
    ;

    init {
        // A hull that sails needs the locs its anims play on.
        if (anims != null) {
            require(facility(FacilityKind.HELM) != null && facility(FacilityKind.SAIL_WOOD) != null &&
                    facility(FacilityKind.SAIL_LINEN) != null) {
                "$name has sailing anims but no helm / wood sail / linen sail facility"
            }
        }
    }

    /** This hull's facility of [kind], or null. */
    fun facility(kind: FacilityKind): BoatFacility? = facilities.firstOrNull { it.kind == kind }

    private fun requireLoc(kind: FacilityKind): DeckLoc =
        facility(kind)?.loc ?: throw IllegalStateException("$name has no $kind facility")

    /** The helm loc. Only for hulls with [anims] (guaranteed by the init check). */
    val helmLoc: DeckLoc
        get() = requireLoc(FacilityKind.HELM)

    /** Sail A: the wood sail (plain sail anims). */
    val sailALoc: DeckLoc
        get() = requireLoc(FacilityKind.SAIL_WOOD)

    /** Sail B: the linen sail (`_offset` anims, the op-bearing loc whose opflags change with the sail state). */
    val sailBLoc: DeckLoc
        get() = requireLoc(FacilityKind.SAIL_LINEN)

    /**
     * Movement stats in fine units (128 per tile).
     * - [baseSpeed], [speedCap]: the hull's stats row ([hullStats], raft dbrow 8161).
     * - [boostDuration]: the mast's stats row (raft dbrow 8182, on the wood sail facility); hulls without a mast
     *   facility yet use the raft's 20.
     * - [acceleration] 64: captured (sidepanel varbit on boarding). Not in the raft's hull or mast rows - only the
     *   camphor-and-up mast rows (8186+) carry `boat_acceleration,64`; keep the captured value until its row is found.
     * - [boostAmount] 64 = observed 2.0 tiles/tick during a trim (controls capture t292-t311: 256).
     * - [halfSpeed]: half sails (move mode 1) hold 64 = 0.5 tiles/tick (controls capture t174-t189).
     */
    val baseSpeed: Int get() = hullStats.baseSpeed
    val speedCap: Int get() = hullStats.speedCap
    val boostDuration: Int get() = facility(FacilityKind.SAIL_WOOD)?.stats?.speedBoostDuration ?: RAFT_BOOST_DURATION
    val acceleration: Int get() = 64
    val boostAmount: Int get() = 64
    val halfSpeed: Int get() = 64

    /** Cargo hold capacity (the hold facility's stats row; raft dbrow 8239: 20), 0 when the hull has no hold. */
    val cargoHoldSize: Int get() = facility(FacilityKind.CARGO_HOLD)?.stats?.cargoHoldSize ?: 0

    companion object {
        /** Raft mast boost duration (dbrow 8182), for hulls whose mast is not transcribed yet. */
        private const val RAFT_BOOST_DURATION = 20

        @JvmStatic
        fun byName(name: String): BoatType? = entries.firstOrNull { it.name.equals(name, ignoreCase = true) }

        /** Every loc id a click on a facility of [kind] can arrive with, across all hulls (object action registration). */
        @JvmStatic
        fun facilityIds(kind: FacilityKind): Array<Any> =
            entries.flatMap { type -> type.facility(kind)?.ids ?: emptyList() }.distinct().toTypedArray()
    }
}