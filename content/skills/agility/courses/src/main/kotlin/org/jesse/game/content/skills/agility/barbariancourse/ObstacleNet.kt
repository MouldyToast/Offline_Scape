package org.jesse.game.content.skills.agility.barbariancourse

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class ObstacleNet : AgilityCourseObstacle(BarbarianOutpostCourse::class.java, 3) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.useStairs(828, if (player.getY() <= 3545) SOUTHERN_END_LOC else NORTHERN_END_LOC, 1, 2)
    }

    override fun getRouteEvent(player: Player, `object`: WorldObject?): Location {
        return if (player.getY() <= 3545) SOUTHERN_START_LOC else NORTHERN_START_LOC
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 35
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(20211)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 8.2
    }

    override fun getStartMessage(success: Boolean): String {
        return "You climb the obstacle net..."
    }

    override fun getEndMessage(success: Boolean): String {
        return "...to the platform above."
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 1
    }

    companion object {
        private val SOUTHERN_START_LOC = Location(2539, 3545, 0)
        private val NORTHERN_START_LOC = Location(2539, 3546, 0)

        private val SOUTHERN_END_LOC = Location(2537, 3545, 1)
        private val NORTHERN_END_LOC = Location(2537, 3546, 1)
    }
}
