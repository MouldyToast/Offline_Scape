package org.jesse.game.content.theatreofblood.room.verzikvitur.second

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import org.jesse.game.content.theatreofblood.room.verzikvitur.spiders.NylocasHagios
import org.jesse.game.content.theatreofblood.room.verzikvitur.spiders.NylocasIschyros
import org.jesse.game.content.theatreofblood.room.verzikvitur.spiders.NylocasToxobolos
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.pathfinding.RouteFinder
import org.jesse.game.world.entity.pathfinding.RouteResult
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy
import org.jesse.game.world.entity.player.Player
import kotlin.random.Random

/**
 * @author Jire
 */

internal fun VerzikVitur.spawnCrabs() {
    for (p in room.validTargets) {
        val crabLocation = findCrabLocation(p) ?: continue

        val crabToSpawnClass = Utils.random(crabsToSpawnClass)
        val crab = crabToSpawnClass.java.getDeclaredConstructor(*crabConstructorParams).newInstance(room, crabLocation)
        crab.spawn()
    }
}

private fun VerzikVitur.findCrabLocation(player: Player): Location? {
    val location = player.location.copy()
    for (dist in 11 downTo 2) {
        val gen = location.transform(Direction.randomDirection(), Utils.random(6, 7))
        val routeResult = RouteFinder.findRoute(location, size, TileStrategy(gen), false)
        if (routeResult == RouteResult.ILLEGAL)
            return room.getLocation(3168, 4313, 0).transform(Direction.randomDirection(), Utils.random(6, 7))
        if (routeResult.steps < 7)
            return gen
    }
    return null
}

private val crabsToSpawnClass = arrayOf(NylocasHagios::class, NylocasToxobolos::class, NylocasIschyros::class)
private val crabConstructorParams = arrayOf(VerzikViturRoom::class.java, Location::class.java)