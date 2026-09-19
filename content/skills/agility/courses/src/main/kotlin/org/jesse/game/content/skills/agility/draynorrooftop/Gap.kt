package org.jesse.game.content.skills.agility.draynorrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_11631
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Gap : AgilityCourseObstacle(DraynorRooftopCourse::class.java, 6) {
    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(FIRST_ANIM)
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> {
                        player.setLocation(END_LOC)
                        player.setAnimation(SECOND_ANIM)
                    }

                    1 -> {
                        player.setAnimation(Animation.STOP)
                        MarkOfGrace.spawn(player, DraynorRooftopCourse.MARK_LOCATIONS, 40, 10)
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
        return intArrayOf(GAP_11631)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 4.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 1
    }

    companion object {
        private val START_LOC = Location(3094, 3255, 3)
        private val END_LOC = Location(3096, 3256, 3)
        private val FIRST_ANIM = Animation(2586, 15)
        private val SECOND_ANIM = Animation(2588)
    }
}
