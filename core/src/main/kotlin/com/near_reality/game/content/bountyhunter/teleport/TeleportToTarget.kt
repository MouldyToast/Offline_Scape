package com.near_reality.game.content.bountyhunter.teleport

import com.near_reality.game.content.bountyhunter.getTarget
import com.near_reality.game.content.bountyhunter.getWildernessLevel
import com.near_reality.game.content.bountyhunter.isBountyPaired
import com.near_reality.game.content.bountyhunter.teleport.TeleportToTarget.Companion.getLandingLocation
import com.near_reality.game.content.offset
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.pathfinding.RouteFinder
import com.zenyte.game.world.entity.pathfinding.RouteResult
import com.zenyte.game.world.entity.pathfinding.strategy.PredictedEntityStrategy
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy
import com.zenyte.game.world.entity.player.Player
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