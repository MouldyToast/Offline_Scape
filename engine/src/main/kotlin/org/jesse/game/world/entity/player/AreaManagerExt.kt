package org.jesse.game.world.entity.player

import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.AreaManager
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.DynamicArea
import org.jesse.game.world.region.RegionAreaAttachments
import kotlin.reflect.KClass

fun fixLocationIfInstanceDC(player: Player, location: Location): Location {
    val onEnterLocation = player.areaManager.onEnterLocation.takeIf { it != 0 }?.let { Location(it) }
    if (onEnterLocation != null)
        player.areaManager.onEnterLocation = 0
    return onEnterLocation?:location
}

/** Regions whose custom maps were removed in Stages 0-3; a save logged out there
 *  would otherwise land on blank terrain. Relocates to home (3087, 3490, 0). */
private val removedCustomRegions = intArrayOf(
    // quad dono island + chin dungeon (maps pre-deleted in 069c688f)
    6440, 6441, 6469, 6696, 6697,
    // primal zone, staff zone, 9517 island
    6582, 8314, 9517,
    // custom donator barrows crypts (never packed) + orphan barrows 13909
    11374, 11375, 11376, 11377, 11378, 11379, 13909,
    // donator zones incl. neighbour columns spawns/implings occupied
    13430, 13431, 13433, 13434, 13436, 13437, 13439, 13440, 13441, 13443,
    13550, 13552, 13686, 13689, 13692, 13693, 13695, 13697,
    // legacy DMM/tournament arenas
    14477, 14478, 14732, 14733, 14734, 15245, 15246, 15248
)

fun fixLocationIfRemovedRegion(location: Location): Location =
    if (location.regionId in removedCustomRegions) Location(3087, 3490, 0) else location

fun AreaManager.onLogin(player: Player) {
    val lastDynamicArea = lastDynamicAreaName
    if (lastDynamicArea != null) {
        lastDynamicAreaName = null
        RegionAreaAttachments.runLogin(lastDynamicArea, player)
    }
}

fun<T : DynamicArea> KClass<out T>.onLogin(action: (Player) -> Unit) {
    RegionAreaAttachments.onLogin(simpleName) {
        action(it)
    }
}
