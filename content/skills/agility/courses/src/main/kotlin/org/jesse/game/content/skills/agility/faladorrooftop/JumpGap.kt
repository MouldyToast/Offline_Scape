/**
 *
 */
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


class JumpGap : AgilityCourseObstacle(FaladorRooftopCourse::class.java, 4) {
    override fun startSuccess(player: Player, `object`: WorldObject) {
        val first = `object`.id == GAP_14903
        player.setFaceLocation(if (first) FIRST_FINISH else SECOND_FINISH)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0
            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP)
                    player.setForceMovement(
                        ForceMovement(
                            player.location,
                            15,
                            if (first) FIRST_FINISH else SECOND_FINISH,
                            35,
                            if (first) ForceMovement.NORTH else ForceMovement.WEST
                        )
                    )
                } else if (ticks == 1) {
                    player.setLocation(if (first) FIRST_FINISH else SECOND_FINISH)
                    MarkOfGrace.spawn(player, FaladorRooftopCourse.Companion.MARK_LOCATIONS, 50, 50)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 50
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 20.0
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14903, GAP_14904)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject): Location {
        return if (`object`.id == GAP_14903) FIRST_START else SECOND_START
    }

    companion object {
        private val FIRST_START = Location(3048, 3358, 3)
        private val FIRST_FINISH = Location(3048, 3361, 3)
        private val SECOND_START = Location(3045, 3361, 3)
        private val SECOND_FINISH = Location(3041, 3361, 3)
    }
}
