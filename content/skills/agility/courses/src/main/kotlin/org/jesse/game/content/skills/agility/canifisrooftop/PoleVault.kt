package org.jesse.game.content.skills.agility.canifisrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.POLEVAULT
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class PoleVault : AgilityCourseObstacle(CanifisRooftopCourse::class.java, 4) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setLocation(START)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.setFaceLocation(FINISH)
                    player.setAnimation(RUN)
                    player.setForceMovement(ForceMovement(`object`, 45, ForceMovement.EAST))
                } else if (ticks == 1) player.setAnimation(VAULT)
                else if (ticks == 2) {
                    player.setLocation(`object`)
                    player.setForceMovement(ForceMovement(LEDGE, 45, ForceMovement.EAST))
                } else if (ticks == 4) {
                    player.setAnimation(LAND)
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
        return intArrayOf(POLEVAULT)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 5
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 10.0
    }

    companion object {
        private val RUN = Animation(1995)
        private val VAULT = Animation(7132)
        private val LAND = Animation(2588)

        private val START = Location(3479, 3484, 2)
        private val FINISH = Location(3489, 3476, 3)
        private val LEDGE = Location(3488, 3476, 2)
    }
}
