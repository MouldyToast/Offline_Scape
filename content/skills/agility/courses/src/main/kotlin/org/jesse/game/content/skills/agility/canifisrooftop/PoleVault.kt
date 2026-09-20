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
                when (ticks) {
                    0 -> {
                        player.setFaceLocation(FINISH)
                        player.setAnimation(RUN)
                        player.setLocation(`object`)
                        player.setForceMovement(ForceMovement(START, 0, `object`, 45, ForceMovement.EAST))
                    }
                    1 -> {
                        player.setAnimation(VAULT)
                        player.setLocation(GLIDE[0])
                        player.setForceMovement(ForceMovement(`object`, 0, GLIDE[0], 30, ForceMovement.EAST))
                    }
                    in 2..6 -> {
                        val i = ticks - 2
                        val delay = if (ticks == 6) 28 else 30
                        player.setLocation(GLIDE[i + 1])
                        player.setForceMovement(ForceMovement(GLIDE[i], 0, GLIDE[i + 1], delay, ForceMovement.EAST))
                    }
                    7 -> {
                        player.setAnimation(LAND)
                        player.setLocation(FINISH)
                        MarkOfGrace.spawn(player, CanifisRooftopCourse.MARK_LOCATIONS, 40, 40)
                        stop()
                    }
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
        return 8
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

        // Diagonal SE glide waypoints — each +1x, -1y per tick
        private val GLIDE = arrayOf(
            Location(3482, 3481, 2),
            Location(3483, 3480, 2),
            Location(3484, 3479, 2),
            Location(3485, 3478, 2),
            Location(3486, 3477, 2),
            Location(3487, 3476, 2),
        )
    }
}