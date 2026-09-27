package org.jesse.game.content.skills.sailing

import org.jesse.game.world.entity.Location
import kotlin.math.abs
import kotlin.math.max

/**
 * Sailing docks with a gangplank: all 34 of them (`sailing_dock` rows 0-31, 59, 60). The 27 island mooring points
 * (ids 32-58) have no gangplank in the map and are not listed.
 *
 * Sources: the `sailing_dock` dbtable ships only its CLIENTSIDE columns (id, nice/inline name, level, sprite);
 * land_coord / sea_coord / boat_rotation are server-side and stripped. The rest comes from the rev-240 map
 * (OpenRS2 #2710, every map square scanned) - see investigation_sailing_docks_v1.md:
 *
 * - gangplank: static loc `sailing_gangplank_<dock>` (map level 1, bridged), a multiloc on
 *   `sailing_player_is_on_player_boat` (Board / Disembark). Its tile also holds a `sailing_gangplank_proxy` loc.
 * - **Placement rule** (the raft, derived from the live berths and checked against the map):
 *   - sea side = the proxy's rotation (the named gangplank's where there is no proxy): r0 south, r1 west,
 *     r2 north, r3 east, i.e. angle `rotation * 512`. Correct at 31 of 32 docks with a proxy - the raft's footprint
 *     is clear of land on that side (the named gangplank's own rotation is wrong at Rellekka and Entrana);
 *   - raft berth tile = gangplank + 2 tiles toward the sea (hull 1 tile off the pier, centred on the gangplank row);
 *   - land tile = gangplank - 1 tile (the disembark tile);
 *   - default facing = sea angle + 512: the raft lies along the pier (Port Sarim: rule = live).
 * - Verified live: Port Sarim (`sailing_straightintoland` t21), Musa Point (`sarim.txt`), the Pandemonium
 *   (session 1 t439, controls t32, `Bouy_recoverboat` t150). Musa Point is turned end-on to lie alongside the
 *   docked ship there; the Pandemonium berth is hand-placed (the rule berth overlaps its hull). Every other dock
 *   is "rule": the berth should be within a tile of live, the facing may differ (turned docks, like Musa Point).
 * - id: the `sailing_boat_1_port` / `sailing_boarded_boat_last_dock` value. The level requirement is noted per row
 *   but not enforced (no Sailing level yet).
 */
enum class Dock(
    val id: Int,
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
    PORT_SARIM(0, "Port Sarim", 59835, 3051, 3193, 3050, 3193, 3053, 3193, 0), // lvl 1, rule = live
    PANDEMONIUM(1, "the Pandemonium", 59836, 3070, 2987, 3069, 2987, 3074, 2987, 1024), // lvl 1, live
    LANDS_END(2, "Land's End", 59837, 1507, 3403, 1506, 3403, 1509, 3403, 0), // lvl 5, rule
    MUSA_POINT(3, "Musa Point", 59838, 2961, 3146, 2960, 3146, 2964, 3146, 1536), // lvl 10, live
    HOSIDIUS(4, "Hosidius", 59839, 1726, 3452, 1726, 3453, 1726, 3450, 512), // lvl 5, rule
    RIMMINGTON(5, "Rimmington", 59840, 2906, 3225, 2907, 3225, 2904, 3225, 1024), // lvl 18, rule
    CATHERBY(6, "Catherby", 59841, 2796, 3412, 2796, 3413, 2796, 3410, 512), // lvl 20, rule
    PORT_PISCARILIUS(7, "Port Piscarilius", 59842, 1845, 3687, 1845, 3688, 1845, 3685, 512), // lvl 15, rule
    BRIMHAVEN(8, "Brimhaven", 59843, 2758, 3230, 2759, 3230, 2756, 3230, 1024), // lvl 25, rule
    ARDOUGNE(9, "Ardougne", 59844, 2671, 3265, 2671, 3266, 2671, 3263, 512), // lvl 28, rule
    PORT_KHAZARD(10, "Port Khazard", 59845, 2686, 3162, 2685, 3162, 2688, 3162, 0), // lvl 30, rule
    WITCHAVEN(11, "Witchaven", 59846, 2747, 3305, 2746, 3305, 2749, 3305, 0), // lvl 34, rule
    ENTRANA(12, "Entrana", 59847, 2879, 3336, 2878, 3336, 2881, 3336, 0), // lvl 36, rule
    CIVITAS_ILLA_FORTIS(13, "Civitas illa Fortis", 59848, 1775, 3142, 1776, 3142, 1773, 3142, 1024), // lvl 38, rule
    CORSAIR_COVE(14, "Corsair Cove", 59849, 2580, 2844, 2579, 2844, 2582, 2844, 0), // lvl 40, rule
    CAIRN_ISLE(15, "Cairn Isle", 59850, 2750, 2952, 2751, 2952, 2748, 2952, 1024), // lvl 42, rule
    SUNSET_COAST(16, "the Sunset Coast", 59851, 1512, 2974, 1513, 2974, 1510, 2974, 1024), // lvl 44, rule
    THE_SUMMER_SHORE(17, "the Summer Shore", 59852, 3174, 2367, 3174, 2368, 3174, 2365, 512), // lvl 45, rule
    ALDARIN(18, "Aldarin", 59853, 1452, 2970, 1452, 2969, 1452, 2972, 1536), // lvl 46, rule
    RUINS_OF_UNKAH(19, "the Ruins of Unkah", 59854, 3144, 2825, 3145, 2825, 3142, 2825, 1024), // lvl 48, rule
    VOID_KNIGHTS_OUTPOST(20, "the Void Knights' Outpost", 59855, 2651, 2678, 2651, 2677, 2651, 2680, 1536), // lvl 50, rule
    PORT_ROBERTS(21, "Port Roberts", 59856, 1861, 3307, 1862, 3307, 1859, 3307, 1024), // lvl 50, rule
    RED_ROCK(22, "Red Rock", 59857, 2809, 2509, 2808, 2509, 2811, 2509, 0), // lvl 52, rule
    RELLEKKA(23, "Rellekka", 59858, 2630, 3705, 2630, 3704, 2630, 3707, 1536), // lvl 62, rule
    ETCETERIA(24, "Etceteria", 59859, 2613, 3840, 2613, 3841, 2613, 3838, 512), // lvl 65, rule
    PORT_TYRAS(25, "Port Tyras", 59860, 2144, 3120, 2144, 3121, 2144, 3118, 512), // lvl 66, rule
    DEEPFIN_POINT(26, "Deepfin Point", 59861, 1923, 2758, 1923, 2759, 1923, 2755, 512), // lvl 67, rule
    JATIZSO(27, "Jatizso", 59862, 2412, 3780, 2412, 3781, 2412, 3778, 512), // lvl 68, rule
    NEITIZNOT(28, "Neitiznot", 59863, 2309, 3782, 2310, 3782, 2307, 3782, 1024), // lvl 68, rule
    PRIFDDINAS(29, "Prifddinas", 59864, 3182, 6076, 3182, 6077, 3182, 6074, 512), // lvl 70, rule
    PISCATORIS(30, "Piscatoris", 59865, 2304, 3689, 2305, 3689, 2302, 3689, 1024), // lvl 75, rule
    LUNAR_ISLE(31, "Lunar Isle", 59866, 2152, 3881, 2151, 3881, 2154, 3881, 0), // lvl 76, rule
    WYRMSCRAIG(59, "Wyrmscraig", 62397, 2568, 2296, 2569, 2296, 2566, 2296, 1024), // lvl 62, rule
    WYRMSCRAIG_CAVE(60, "Wyrmscraig Cavern", 62398, 2582, 8606, 2583, 8606, 2580, 8606, 1024), // lvl 62, rule
    ;

    val landTile: Location
        get() = Location(landX, landZ, 0)

    /** Chebyshev distance from the gangplank to the boat's root tile. */
    fun distanceTo(boat: Boat): Int {
        val root = boat.entity.rootTile
        return max(abs(root.x - gangplankX), abs(root.y - gangplankZ))
    }

    /** Whether [boat] is close enough to this dock to board / disembark (captures: 3, 4.5 and 7 tiles). */
    fun isNear(boat: Boat): Boolean = distanceTo(boat) <= DOCKING_RANGE

    companion object {
        /** Unverified threshold: live docked at 3 and 7 (Port Sarim, the latter at full speed) and spawned at 4.5 (Pandemonium) tiles. */
        const val DOCKING_RANGE = 10

        @JvmStatic
        fun byGangplank(id: Int, x: Int, z: Int): Dock? =
            entries.firstOrNull { it.gangplankId == id } ?: entries.firstOrNull { it.gangplankX == x && it.gangplankZ == z }

        @JvmStatic
        fun nearest(boat: Boat): Dock? = entries.minByOrNull { it.distanceTo(boat) }

        /** Dock by its `sailing_dock` id (the value of `sailing_boat_1_port`). */
        @JvmStatic
        fun byId(id: Int): Dock? = entries.firstOrNull { it.id == id }

        @JvmStatic
        val gangplankIds: Array<Any>
            get() = entries.map { it.gangplankId as Any }.toTypedArray()
    }
}