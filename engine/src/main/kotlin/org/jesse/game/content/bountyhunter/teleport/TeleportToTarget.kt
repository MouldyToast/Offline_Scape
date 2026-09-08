package org.jesse.game.content.bountyhunter.teleport

import org.jesse.game.content.bountyhunter.getTarget
import org.jesse.game.content.bountyhunter.getWildernessLevel
import org.jesse.game.content.bountyhunter.isBountyPaired
import org.jesse.game.content.bountyhunter.teleport.TeleportToTarget.Companion.getLandingLocation
import org.jesse.game.content.offset
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.pathfinding.RouteFinder
import org.jesse.game.world.entity.pathfinding.RouteResult
import org.jesse.game.world.entity.pathfinding.strategy.PredictedEntityStrategy
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy
import org.jesse.game.world.entity.player.Player
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-14
 */
class TeleportToTarget {

    companion object{
        fun canTeleportToTarget(player: Player): Boolean {
            if (!player.isBountyPaired()) return false
            val target = player.getTarget()
            return if (target.isPresent) {
                if (player.location.withinDistance(target.get(), 20))
                    return false
                return target.get().getWildernessLevel() > 0
            }
            else false
        }

        fun teleportToTarget(player: Player) =
            player.getTarget().ifPresent { player.teleport(player.getLandingLocation(it.location offset(Pair(-6, -6)))) }

        private fun Player.getLandingLocation(location: Location?): Location {
            val xOffset = Random.nextInt(12)
            val yOffset = Random.nextInt(12)
            val tempLocation = Location(location?.offset(Pair(xOffset, yOffset)))
            if (!canPathToTarget(tempLocation))
                return getLandingLocation(location)
            if (!isValidTile(tempLocation))
                return getLandingLocation(location)
            return tempLocation
        }

        private fun Player.canPathToTarget(tempLocation: Location): Boolean {
            if (!isBountyPaired()) return false
            val target = getTarget()
            if (target.isPresent) {
                val path = RouteFinder.findRoute(tempLocation, 0, TileStrategy(target.get().location), true)
                sendDeveloperMessage("Steps: " + path.steps)
                target.get().sendDeveloperMessage("Steps: " + path.steps)
                if (path.steps < 2 || path.steps > 8)
                return false
            }
            return true
        }

        private fun isValidTile(location: Location): Boolean {
            return !World.isTileFree(location, 0) || isWall(location)
        }

        private fun isWall(location: Location): Boolean {
            var isWall = false
            for (type in 0..22) {
                isWall = World.getObjectWithType(location, type) != null
                if (isWall) break
            }
            return isWall;
        }
    }
}