package org.jesse.game.content.skills.agility.rellekkarooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.ROUGH_WALL_14946
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class RoughWall : AgilityCourseObstacle(RellekkaRooftopCourse::class.java, 1) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 80
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(ROUGH_WALL_14946)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0

            override fun run() {
                when (ticks++) {
                    0 -> player.setAnimation(ANIM1)
                    2 -> {
                        player.setLocation(LOCATION1)
                        player.setAnimation(Animation.STOP)
                        MarkOfGrace.spawn(player, RellekkaRooftopCourse.Companion.MARK_LOCATIONS, 40, 80)
                        stop()
                    }
                }
            }
        }, 0, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 20.0
    }

    companion object {
        private val ANIM1 = Animation(828, 15)

        private val LOCATION1 = Location(2626, 3676, 3)
    }
}