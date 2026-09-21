package org.jesse.game.content.skills.agility.pyramid

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject
import kotlin.math.abs

class LowWall : AgilityCourseObstacle(AgilityPyramid::class.java, 1) {
    override fun getFilterableStartMessage(success: Boolean): String {
        return "You climb the low wall..."
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.sendSound(SoundEffect(2453, 1, 30))
        player.faceObject(`object`)
        player.setAnimation(climbAnim)
        val forward = abs(
            `object`.faceDirection.getDirection() - Direction.getNPCDirection(player.getRoundedDirection())
                .getDirection()
        ) <= 257
        val direction: Direction =
            if (forward) `object`.faceDirection else `object`.faceDirection.getCounterClockwiseDirection(4)
        val destination: Location? = player.location.transform(direction, 2)
        val currentTile: Location = Location(player.location)
        player.setLocation(destination)
        player.setForceMovement(ForceMovement(currentTile, 1, destination, 80, direction.getDirection()))
    }

    override fun getFilterableEndMessage(success: Boolean): String {
        return "... and make it over."
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 8.0
    }

    override fun getObjectIds(): IntArray = intArrayOf(10865)

    companion object {
        private val climbAnim = Animation(840, 15)
    }
}
