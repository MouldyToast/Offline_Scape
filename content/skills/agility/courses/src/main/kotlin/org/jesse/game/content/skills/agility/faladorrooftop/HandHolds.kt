/**
 *
 */
package org.jesse.game.content.skills.agility.faladorrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.HAND_HOLDS_14901
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class HandHolds : AgilityCourseObstacle(FaladorRooftopCourse::class.java, 3) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) player.setAnimation(JUMP)
                else if (ticks == 1) {
                    player.setAnimation(HANG)
                    player.setLocation(HOLDS[0])
                } else if (ticks == 2) player.setForceMovement(
                    ForceMovement(
                        HOLDS[5],
                        10,
                        HOLDS[1],
                        20,
                        ForceMovement.WEST
                    )
                )
                else if (ticks == 3) {
                    player.setAnimation(HANG)
                    player.setLocation(HOLDS[1])
                } else if (ticks == 4) player.setForceMovement(
                    ForceMovement(
                        player.location,
                        10,
                        HOLDS[2],
                        20,
                        ForceMovement.WEST
                    )
                )
                else if (ticks == 5) {
                    player.setAnimation(HANG)
                    player.setLocation(HOLDS[2])
                } else if (ticks == 6) player.setForceMovement(
                    ForceMovement(
                        player.location,
                        10,
                        HOLDS[3],
                        20,
                        ForceMovement.WEST
                    )
                )
                else if (ticks == 7) {
                    player.setAnimation(HANG)
                    player.setLocation(HOLDS[3])
                } else if (ticks == 8) player.setForceMovement(
                    ForceMovement(
                        player.location,
                        10,
                        HOLDS[4],
                        20,
                        ForceMovement.WEST
                    )
                )
                else if (ticks == 9) {
                    player.setAnimation(HANG)
                    player.setLocation(HOLDS[4])
                } else if (ticks == 10) {
                    player.setAnimation(LAND)
                    player.setForceMovement(JUMP_FINISH)
                } else if (ticks == 11) {
                    player.setAnimation(Animation.STOP)
                    player.setLocation(HOLDS[7])
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
        return 11
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 45.0
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(HAND_HOLDS_14901)
    }

    companion object {
        private val JUMP = Animation(2583)
        private val HANG = Animation(1118)
        private val LAND = Animation(1120)

        private val HOLDS = arrayOf<Location?>(
            Location(3050, 3351, 2),
            Location(3051, 3352, 2),
            Location(3051, 3353, 2),
            Location(3051, 3354, 2),
            Location(3051, 3355, 2),

            Location(3051, 3351, 2),  // special tile (between holds[0] and holds[1])
            Location(3051, 3356, 2),  // special tile (between holds[4] and holds[7])
            Location(3050, 3357, 3),  // holds[7] the end tile
        )
        private val JUMP_FINISH: ForceMovement = ForceMovement(HOLDS[6], 15, HOLDS[7], 35, ForceMovement.WEST)
    }
}
