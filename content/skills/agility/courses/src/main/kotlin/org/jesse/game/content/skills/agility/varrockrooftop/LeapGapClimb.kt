package org.jesse.game.content.skills.agility.varrockrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_14833
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class LeapGapClimb : AgilityCourseObstacle(VarrockRooftopCourse::class.java, 5) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        val ledge = Location(player.getX(), player.getY() - 3, 3)
        val end = Location(player.getX(), player.getY() - 4, 3)
        player.setFaceLocation(ledge)
        player.lock()
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0
            override fun run() {
                if (ticks == 1) {
                    player.setAnimation(Animation.JUMP)
                    player.setForceMovement(ForceMovement(ledge, 60, ForceMovement.SOUTH))
                } else if (ticks == 2) {
                    player.setAnimation(CLIMB)
                    player.setLocation(ledge)
                } else if (ticks == 3) {
                    player.setForceMovement(ForceMovement(end, 30, ForceMovement.SOUTH))
                } else if (ticks == 4) {
                    player.setLocation(end)
                    MarkOfGrace.spawn(player, VarrockRooftopCourse.Companion.MARK_LOCATIONS, 40, 30)
                    player.unlock()
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14833)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 5
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 9.0
    }

    companion object {
        private val CLIMB = Animation(2585)
    }
}
