package org.jesse.game.content.skills.agility.seersrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_14929
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class SecondJumpGap : AgilityCourseObstacle(SeersRooftopCourse::class.java, 4) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        val edge = Location(player.getX(), player.getY() - 3, 3)
        val finish = Location(player.getX(), player.getY() - 4, 3)
        player.setFaceLocation(edge)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) player.setAnimation(Animation.LEAP)
                else if (ticks == 1) {
                    player.setLocation(edge)
                    player.setAnimation(GRAB)
                } else if (ticks == 3) {
                    player.setLocation(finish)
                    MarkOfGrace.spawn(player, SeersRooftopCourse.Companion.MARK_LOCATIONS, 60, 20)
                    stop()
                }

                ticks++
            }
        }, 0, 0)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 60
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14929)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 20.0
    }

    companion object {
        private val GRAB = Animation(2585)
    }
}
