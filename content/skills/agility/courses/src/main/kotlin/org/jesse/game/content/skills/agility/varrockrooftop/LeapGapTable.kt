package org.jesse.game.content.skills.agility.varrockrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_14834
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class LeapGapTable : AgilityCourseObstacle(VarrockRooftopCourse::class.java, 6) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setFaceLocation(TABLE)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0
            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(RUN)
                } else if (ticks == 1) {
                    player.setAnimation(JUMP)
                    player.setForceMovement(JUMP_FM)
                } else if (ticks == 2) {
                    player.setLocation(TABLE)
                    player.setForceMovement(TABLE_LEAP_FM)
                } else if (ticks == 3) {
                    player.setLocation(LEDGE)
                    player.setAnimation(CLIMB)
                } else if (ticks == 4) player.setForceMovement(CLIMB_FM)
                else if (ticks == 5) {
                    player.setLocation(FINISH)
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
        return 6
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 22.0
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14834)
    }

    companion object {
        private val RUN = Animation(1995)
        private val CLIMB = Animation(2585)
        private val JUMP = Animation(4789)
        private val TABLE = Location(3215, 3399, 3)
        private val LEDGE = Location(3217, 3399, 3)
        private val FINISH = Location(3218, 3399, 3)
        private val JUMP_FM: ForceMovement = ForceMovement(TABLE, 45, 1446)
        private val TABLE_LEAP_FM: ForceMovement = ForceMovement(TABLE, 15, LEDGE, 35, ForceMovement.EAST)
        private val CLIMB_FM: ForceMovement = ForceMovement(FINISH, 30, ForceMovement.EAST)
    }
}
