package org.jesse.game.content.skills.agility.varrockrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.LEDGE_14836
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class HurdleLedge : AgilityCourseObstacle(VarrockRooftopCourse::class.java, 7) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        val finish = Location(player.getX(), player.getY() + 2, 3)
        player.setFaceLocation(finish)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(JUMP)
                    player.setForceMovement(ForceMovement(player.location, 15, finish, 35, ForceMovement.NORTH))
                } else if (ticks == 1) {
                    player.setLocation(finish)
                    MarkOfGrace.spawn(player, VarrockRooftopCourse.Companion.MARK_LOCATIONS, 40, 30)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 3.0
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(LEDGE_14836)
    }

    companion object {
        private val JUMP = Animation(1603)
    }
}
