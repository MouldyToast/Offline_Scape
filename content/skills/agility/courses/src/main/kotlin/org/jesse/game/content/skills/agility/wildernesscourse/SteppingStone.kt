package org.jesse.game.content.skills.agility.wildernesscourse

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class SteppingStone : AgilityCourseObstacle(WildernessCourse::class.java, 3) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.lock()
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0

            override fun run() {
                player.setAnimation(Animation.JUMP)
                player.setForceMovement(
                    ForceMovement(
                        Location(player.getX() - 1, player.getY(), player.getPlane()),
                        35,
                        ForceMovement.WEST
                    )
                )
                player.addWalkSteps(player.getX() - 1, player.getY(), 2, false)
                if (ticks == 5) {
                    player.unlock()
                    stop()
                }
                ticks++
            }
        }, 0, 1)
    }

    override fun getStartMessage(success: Boolean): String {
        return "You start crossing the stepping stones..."
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 52
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(23556)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 20.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 11
    }

    companion object {
        private val START_LOC = Location(3002, 3960, 0)
    }
}
