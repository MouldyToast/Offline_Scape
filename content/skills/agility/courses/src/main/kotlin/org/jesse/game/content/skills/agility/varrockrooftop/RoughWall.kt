package org.jesse.game.content.skills.agility.varrockrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.ROUGH_WALL_14412
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class RoughWall : AgilityCourseObstacle(VarrockRooftopCourse::class.java, 1) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) player.setAnimation(CLIMB)
                else if (ticks == 1) {
                    player.setLocation(JUMP_SPOT)
                    player.setAnimation(JUMP)
                } else if (ticks == 2) player.setForceMovement(ForceMovement(LAND_SPOT, 45, ForceMovement.WEST))
                else if (ticks == 3) {
                    player.setLocation(LAND_SPOT)
                    MarkOfGrace.spawn(player, VarrockRooftopCourse.Companion.MARK_LOCATIONS, 40, 30)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 12.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 4
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(ROUGH_WALL_14412)
    }

    companion object {
        private val JUMP = Animation(2585)
        private val CLIMB = Animation(828)

        private val JUMP_SPOT = Location(3220, 3414, 3)
        private val LAND_SPOT = Location(3219, 3414, 3)
    }
}
