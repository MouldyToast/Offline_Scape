package org.jesse.game.content.skills.agility.draynorrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.obj.ids.WALL_11630
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Wall : AgilityCourseObstacle(DraynorRooftopCourse::class.java, 5) {
    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(FIRST_ANIM)
        player.setForceMovement(
            ForceMovement(
                Location(player.getX(), player.getY() - 1, player.getPlane()),
                30,
                Location(player.getX(), player.getY() - 1, player.getPlane()),
                35,
                ForceMovement.SOUTH
            )
        )
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> {
                        player.setLocation(Location(3088, 3256, player.getPlane()))
                        player.setAnimation(SECOND_ANIM)
                    }

                    1 -> {
                        player.setLocation(Location(3088, 3255, player.getPlane()))
                        stop()
                    }
                }
            }
        }, 0, 1)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 10
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(WALL_11630)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 10.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }


    companion object {
        private val START_LOC = Location(3088, 3257, 3)
        private val FIRST_ANIM = Animation(2583)
        private val SECOND_ANIM = Animation(2585)
    }
}
