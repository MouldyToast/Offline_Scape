package org.jesse.game.content.skills.agility.canifisrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_14848
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class JumpLongGap : AgilityCourseObstacle(CanifisRooftopCourse::class.java, 3) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setFaceLocation(FINISH)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(JUMP)
                } else if (ticks == 1) {
                    player.setLocation(LEDGE)
                    player.setAnimation(CLIMB)
                } else if (ticks == 2) {
                    player.setForceMovement(MOVE)
                } else if (ticks == 3) {
                    player.setLocation(FINISH)
                    MarkOfGrace.spawn(player, CanifisRooftopCourse.MARK_LOCATIONS, 40, 40)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 40
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14848)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 10.0
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    companion object {
        private val JUMP = Animation(2583)
        private val CLIMB = Animation(2585)

        private val START = Location(3487, 3499, 2)
        private val FINISH = Location(3479, 3499, 3)
        private val LEDGE = Location(3482, 3499, 3)
        private val MOVE: ForceMovement = ForceMovement(FINISH, 45, ForceMovement.WEST)
    }
}
