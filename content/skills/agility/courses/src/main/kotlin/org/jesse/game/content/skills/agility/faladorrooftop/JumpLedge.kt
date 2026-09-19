package org.jesse.game.content.skills.agility.faladorrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.*
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class JumpLedge : AgilityCourseObstacle(FaladorRooftopCourse::class.java, 5) {
    override fun startSuccess(player: Player, `object`: WorldObject) {
        val axis = `object`.id == LEDGE_14920 || `object`.id == LEDGE_14924
        val direction: Int =
            if (axis) (if (`object`.id == LEDGE_14920) ForceMovement.WEST else ForceMovement.EAST) else ForceMovement.SOUTH
        val offset =
            if (axis) (if (`object`.id == LEDGE_14920) player.getX() - 2 else player.getX() + 2) else (if (`object`.name == "Gap") player.getY() - 4 else player.getY() - 2)
        val finish = if (axis) Location(offset, player.getY(), 3) else Location(player.getX(), offset, 3)
        player.setFaceLocation(finish)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0
            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(JUMP)
                    player.setForceMovement(ForceMovement(finish, 45, direction))
                } else if (ticks == 2) {
                    player.setLocation(finish)
                    MarkOfGrace.spawn(player, FaladorRooftopCourse.Companion.MARK_LOCATIONS, 50, 50)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject): Location {
        return when (`object`.id) {
            GAP_14919 -> Location(3018, 3353, 3)
            LEDGE_14920 -> Location(3016, 3346, 3)
            LEDGE_14921 -> Location(3013, 3344, 3)
            LEDGE_14922 -> Location(3013, 3335, 3)
            LEDGE_14923 -> Location(3013, 3335, 3)
            LEDGE_14924 -> Location(3017, 3333, 3)
            else -> `object`
        }
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 50
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun getSuccessXp(`object`: WorldObject): Double {
        return (if (`object`.id == GAP_14919) 25 else 10).toDouble()
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14919, LEDGE_14920, LEDGE_14921, LEDGE_14922, LEDGE_14923, LEDGE_14924)
    }

    companion object {
        private val JUMP = Animation(1603)
    }
}