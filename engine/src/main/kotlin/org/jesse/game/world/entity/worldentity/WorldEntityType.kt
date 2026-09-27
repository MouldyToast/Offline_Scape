package org.jesse.game.world.entity.worldentity

/**
 * A loc placed on a world entity's deck, relative to the template's south-west tile.
 * Values are transcribed from the live Pandemonium capture's `loc_add_change_v2` lines
 * (template-space coords printed by RSProx minus the template zone base).
 */
data class DeckLoc(
    val id: Int,
    val shape: Int,
    val rotation: Int,
    val dx: Int,
    val dz: Int,
    val level: Int,
)

/** A specific deck loc (for animating it): same fields as [DeckLoc]. */
typealias DeckRef = DeckLoc

/**
 * Helm / sail loc references and animation ids for a hull (raft ids from the live capture:
 * `human_sailing_alpha_helm_raft01_*`, `sailing_alpha_helm_raft01_*`, `sailing_boat_sail_kandarin_1x3_*`).
 * The wood sail (sailA) plays the plain anims, the linen sail (sailB) the `_offset` variants.
 * Transitions are followed one tick later by the steady anim (capture t446 -> t447, t453 -> t454).
 * The helm loop anims are re-sent every 10 ticks while navigating (capture t453, 463, 473, ...).
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
    val sailDownToFull: Int,
    val sailFull: Int,
    val sailFullToDown: Int,
    val sailDown: Int,
    val sailDownToFullOffset: Int,
    val sailFullOffset: Int,
    val sailFullToDownOffset: Int,
    val sailDownOffset: Int,
    val playerTrimStart: Int,
    val playerTrimLoop: Int,
    val playerTrimEnd: Int,
    val helmLocTrimStart: Int,
    val helmLocTrimLoop: Int,
    val helmLocTrimEnd: Int,
    /** Spotanim on sailB every tick while a gust is up (capture t510-512: vfx_wind_sail_raft01_full01). */
    val gustGraphic: Int,
    /** Spotanim on sailB every tick while boosted (capture t513-532: vfx_wind_sail_raft01_speedboost01). */
    val boostGraphic: Int,
)

/**
 * World entity (boat) hull templates.
 *
 * Verified sources:
 * - `id` / `activeLevel`: rev-240 cache `dump.worldentity` (`[worldentity_N]`, `mainlevel=1`).
 * - `templateZoneX/Z`: south-west template zone copied by `rebuild_worldentity_v4` in the live
 *   Pandemonium capture (build_area `source=` coords >> 3). All three live in map square (60,100);
 *   the hulls are static locs of that square (raft: `sailing_boat_hull_kandarin_1x3_wood` 59494 at (3843,6458,0)).
 * - `sizeX/sizeZ`: size in zones (RSProx prints tiles: raft/skiff 8x8, sloop 8x16).
 * - `boardDx/boardDz`: deck tile the player is teleported onto when boarding
 *   (capture t439: (15555,14276,1) = instance base (15552,14272) + (3,4) — the helm tile).
 * - `deck`: dynamic deck locs added after the rebuild. Only the raft is transcribed so far
 *   (Pandemonium dock raft, capture lines 14660–14671); skiff/sloop decks come with their content.
 */
enum class WorldEntityType(
    val id: Int,
    val templateZoneX: Int,
    val templateZoneZ: Int,
    val sizeX: Int,
    val sizeZ: Int,
    val activeLevel: Int,
    val boardDx: Int,
    val boardDz: Int,
    val deck: List<DeckLoc>,
    val sailing: SailingAnims? = null,
) {

    RAFT(
        1, 3840 shr 3, 6456 shr 3, 1, 1, 1, 3, 4,
        listOf(
            DeckLoc(59554, 10, 0, 3, 4, 1), // sailing_boat_steering_kandarin_1x3_wood (helm)
            DeckLoc(59530, 10, 0, 3, 3, 1), // sailing_boat_sail_kandarin_1x3_wood
            DeckLoc(29506, 10, 0, 3, 5, 1), // sailing_boat_sail_kandarin_1x3_linen
            DeckLoc(60245, 10, 0, 3, 2, 1), // sailing_boat_cargo_hold_regular_raft
            DeckLoc(32545, 22, 0, 2, 3, 1), // invisible_type0_nonblocking
            DeckLoc(58569, 22, 0, 4, 3, 1), // randomsound_ardent_ocean_gulls
            DeckLoc(58526, 22, 0, 2, 4, 1), // bgsound_sailing_ocean_water_loop_01
            DeckLoc(58568, 22, 0, 4, 4, 1), // randomsound_ardent_ocean_crashing_waves
            DeckLoc(32545, 22, 0, 2, 2, 1), // invisible_type0_nonblocking
            DeckLoc(32545, 22, 0, 4, 2, 1), // invisible_type0_nonblocking
            DeckLoc(32545, 22, 0, 2, 5, 1), // invisible_type0_nonblocking
            DeckLoc(32545, 22, 0, 4, 5, 1), // invisible_type0_nonblocking
        ),
        SailingAnims(
            helm = DeckRef(59554, 10, 0, 3, 4, 1),
            sailA = DeckRef(59530, 10, 0, 3, 3, 1),
            sailB = DeckRef(29506, 10, 0, 3, 5, 1),
            playerHelmStart = 13340, playerHelmLoop = 13341,
            helmLocStart = 13335, helmLocLoop = 13336, helmLocInactive = 13334,
            sailDownToFull = 13374, sailFull = 13373, sailFullToDown = 13369, sailDown = 13367,
            sailDownToFullOffset = 13882, sailFullOffset = 13881, sailFullToDownOffset = 13877, sailDownOffset = 13875,
            playerTrimStart = 13342, playerTrimLoop = 13343, playerTrimEnd = 13344,
            helmLocTrimStart = 13337, helmLocTrimLoop = 13338, helmLocTrimEnd = 13339,
            gustGraphic = 3529, boostGraphic = 3530,
        ),
    ),
    // Skiff board tile verified: capture t74 teleport to (15556,14276,1) = base + (4,4).
    SKIFF(2, 3840 shr 3, 6400 shr 3, 1, 1, 1, 4, 4, emptyList()),
    // Sloop board tile UNVERIFIED (never boarded in the capture) — deck centre placeholder.
    SLOOP(3, 3864 shr 3, 6432 shr 3, 1, 2, 1, 4, 8, emptyList()),
    ;

    /**
     * Movement stats in fine units (128 per tile), from the sidepanel varbits the live server sends on
     * boarding (raft, capture t439): basespeed 192, acceleration 64, speedcap 320, speedboost_duration 20.
     * Boost amount 64 = observed 2.0 tiles/tick during a trim (1.5 + 0.5). Same values used for all hulls
     * until skiff/sloop stats are captured.
     */
    val baseSpeed: Int get() = 192
    val acceleration: Int get() = 64
    val speedCap: Int get() = 320
    val boostAmount: Int get() = 64
    val boostDuration: Int get() = 20

    companion object {
        @JvmStatic
        fun byName(name: String): WorldEntityType? = entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}