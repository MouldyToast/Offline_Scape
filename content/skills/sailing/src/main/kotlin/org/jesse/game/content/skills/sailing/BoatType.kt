package org.jesse.game.content.skills.sailing

import org.jesse.game.world.entity.worldentity.DeckLoc
import org.jesse.game.world.entity.worldentity.WorldEntityTemplate

/** A specific deck loc (for animating it): same fields as [DeckLoc]. */
typealias DeckRef = DeckLoc

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
 * Helm / sail loc references and animation ids for a hull (raft ids from the live captures:
 * `human_sailing_alpha_helm_raft01_*`, `sailing_alpha_helm_raft01_*`, `sailing_boat_sail_kandarin_1x3_*`).
 * The wood sail (sailA) plays the plain anims, the linen sail (sailB, the op-bearing loc) the `_offset` variants.
 * The helm loop anims are re-sent every 10 ticks while navigating (controls capture t47, 57+10, 73+10, ...).
 */
data class SailingAnims(
    val helm: DeckRef,
    val sailA: DeckRef,
    val sailB: DeckRef,
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
    /** Spotanim on sailB during a gust while the boat still moves faster than half speed (`vfx_wind_sail_raft01_full01`). */
    val gustGraphic: Int,
    /** Spotanim on sailB during a gust at half speed or less (`vfx_wind_sail_raft01_half01`, controls capture t139). */
    val gustGraphicHalf: Int,
    /** Spotanim on sailB while boosted (`vfx_wind_sail_raft01_speedboost01`). */
    val boostGraphic: Int,
)

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
 * - `deck`: dynamic deck locs added after the rebuild, transcribed from the capture's `loc_add_change_v2`
 *   lines (template-space coords minus the template zone base). Only the raft is transcribed so far
 *   (Pandemonium dock raft, capture lines 14660-14671); skiff/sloop decks come with their content.
 */
enum class BoatType(
    val template: WorldEntityTemplate,
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
            deck = listOf(
                // Opflags from the rebuild tick (controls capture t32): helm and cargo hold ops 1-4,
                // both sails only op2 "Set" while the sails are down; the sound / invisible locs keep the default.
                DeckLoc(59554, 10, 0, 3, 4, 1, 0b1111), // sailing_boat_steering_kandarin_1x3_wood (helm)
                DeckLoc(59530, 10, 0, 3, 3, 1, 0b10), // sailing_boat_sail_kandarin_1x3_wood
                DeckLoc(29506, 10, 0, 3, 5, 1, 0b10), // sailing_boat_sail_kandarin_1x3_linen
                DeckLoc(60245, 10, 0, 3, 2, 1, 0b1111), // sailing_boat_cargo_hold_regular_raft
                DeckLoc(32545, 22, 0, 2, 3, 1), // invisible_type0_nonblocking
                DeckLoc(58569, 22, 0, 4, 3, 1), // randomsound_ardent_ocean_gulls
                DeckLoc(58526, 22, 0, 2, 4, 1), // bgsound_sailing_ocean_water_loop_01
                DeckLoc(58568, 22, 0, 4, 4, 1), // randomsound_ardent_ocean_crashing_waves
                DeckLoc(32545, 22, 0, 2, 2, 1), // invisible_type0_nonblocking
                DeckLoc(32545, 22, 0, 4, 2, 1), // invisible_type0_nonblocking
                DeckLoc(32545, 22, 0, 2, 5, 1), // invisible_type0_nonblocking
                DeckLoc(32545, 22, 0, 4, 5, 1), // invisible_type0_nonblocking
            ),
        ),
        SailingAnims(
            helm = DeckRef(59554, 10, 0, 3, 4, 1),
            sailA = DeckRef(59530, 10, 0, 3, 3, 1),
            sailB = DeckRef(29506, 10, 0, 3, 5, 1),
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
    SKIFF(WorldEntityTemplate(2, 3840 shr 3, 6400 shr 3, 1, 1, 1, 4, 4, emptyList()), null),

    // Sloop board tile UNVERIFIED (never boarded in the capture) - deck centre placeholder.
    SLOOP(WorldEntityTemplate(3, 3864 shr 3, 6432 shr 3, 1, 2, 1, 4, 8, emptyList()), null),
    ;

    /**
     * Movement stats in fine units (128 per tile), from the sidepanel varbits the live server sends on
     * boarding (raft: basespeed 192, acceleration 64, speedcap 320, speedboost_duration 20).
     * Boost amount 64 = observed 2.0 tiles/tick during a trim (controls capture t292-t311: 256).
     * Half sails (move mode 1) hold 64 = 0.5 tiles/tick (controls capture t174-t189).
     * Same values used for all hulls until skiff/sloop stats are captured.
     */
    val baseSpeed: Int get() = 192
    val acceleration: Int get() = 64
    val speedCap: Int get() = 320
    val boostAmount: Int get() = 64
    val boostDuration: Int get() = 20
    val halfSpeed: Int get() = 64

    companion object {
        @JvmStatic
        fun byName(name: String): BoatType? = entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}
