package org.jesse.game.content.theatreofblood.room.verzikvitur.third

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.pathfinding.RouteFinder
import org.jesse.game.world.entity.pathfinding.RouteResult
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy
import org.jesse.game.world.entity.player.Player

/**
 * @author Jire
 */

internal fun VerzikVitur.spawnTornados() {
    attackSpeed = 5
    setForceTalk("I'm not done with you just yet!")
    for (p in room.validTargets) {
        val tornadoLocation = findTornadoLocation(p) ?: continue
        PurpleTornado(room, tornadoLocation).run {
            chaseTarget = p
            spawn()
        }
    }
}

internal fun VerzikVitur.findTornadoLocation(p: Player): Location? {
    val pLocation = p.location
    for (i in 0..32) {
        val gen = pLocation.transform(Direction.randomDirection(), Utils.random(4, 5))
        val routeResult = RouteFinder.findRoute(location, size, TileStrategy(gen), false)
        if (routeResult == RouteResult.ILLEGAL)
            return room.getLocation(3168, 4313, 0).transform(Direction.randomDirection(), Utils.random(6, 7))
        if (routeResult.steps < 7)
            return gen
    }
    return null
}