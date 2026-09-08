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
