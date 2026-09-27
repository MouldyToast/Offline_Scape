package org.jesse.game.content.skills.sailing

import org.jesse.game.world.entity.Location
import kotlin.math.abs
import kotlin.math.max

/**
 * Sailing docks. The `sailing_dock` dbtable in the cache only carries its CLIENTSIDE columns
 * (dock_id, nice_name, level_required, sprite) - land_coord / sea_coord / boat_rotation are server-side
 * and stripped, so every dock here is transcribed from a live capture:
 *
 * - gangplank: static map loc `sailing_gangplank_<dock>` (map file: level 1, bridged -> level 0 server-side),
 *   a multiloc on `sailing_player_is_on_player_boat` (0 = embark: Board, 1 = disembark: Disembark).
 * - land tile: where the disembark teleport lands (Port Sarim t606 (3050,3193,0), Pandemonium t... (3069,2987,0)).
 * - sea tile + rotation: where your boat spawns when you board at this dock
 *   (Pandemonium raft t439: (3074.5, 2987.5) angle 1024. Port Sarim: intro skiff t74 (3054.5, 3193.5) angle 0 -
 *   the raft's own Port Sarim berth is not in the capture yet).
 */
enum class Dock(
    val displayName: String,
    val gangplankId: Int,
    val gangplankX: Int,
    val gangplankZ: Int,
    val landX: Int,
    val landZ: Int,
    val seaTileX: Int,
    val seaTileZ: Int,
    val rotation: Int,
) {
    PORT_SARIM("Port Sarim", 59835, 3051, 3193, 3050, 3193, 3054, 3193, 0),
    PANDEMONIUM("the Pandemonium", 59836, 3070, 2987, 3069, 2987, 3074, 2987, 1024),
    ;

    val landTile: Location
        get() = Location(landX, landZ, 0)

    /** Chebyshev distance from the gangplank to the boat's root tile. */
    fun distanceTo(boat: Boat): Int {
        val root = boat.entity.rootTile
        return max(abs(root.x - gangplankX), abs(root.y - gangplankZ))
    }

    /** Whether [boat] is close enough to this dock to board / disembark (capture: 3-5 tiles). */
    fun isNear(boat: Boat): Boolean = distanceTo(boat) <= DOCKING_RANGE

    companion object {
        /** Unverified threshold: live docked at 3 (Port Sarim) and spawned at 4.5 (Pandemonium) tiles. */
        const val DOCKING_RANGE = 10

        @JvmStatic
        fun byGangplank(id: Int, x: Int, z: Int): Dock? =
            entries.firstOrNull { it.gangplankId == id } ?: entries.firstOrNull { it.gangplankX == x && it.gangplankZ == z }

        @JvmStatic
        fun nearest(boat: Boat): Dock? = entries.minByOrNull { it.distanceTo(boat) }

        @JvmStatic
        val gangplankIds: Array<Any>
            get() = entries.map { it.gangplankId as Any }.toTypedArray()
    }
}
