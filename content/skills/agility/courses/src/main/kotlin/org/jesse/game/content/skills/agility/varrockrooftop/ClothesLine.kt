package org.jesse.game.content.skills.agility.varrockrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.CLOTHES_LINE
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class ClothesLine : AgilityCourseObstacle(VarrockRooftopCourse::class.java, 2) {
    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.faceObject(`object`)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP)
                    player.setForceMovement(ForceMovement(player.location, 15, FIRST_JUMP, 35, ForceMovement.WEST))
                } else if (ticks == 1) player.setLocation(FIRST_JUMP)
                else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP)
                    player.setForceMovement(
                        ForceMovement(
                            player.location,
                            15,
                            SECOND_JUMP,
                            35,
                            ForceMovement.WEST
                        )
                    )
                } else if (ticks == 3) player.setLocation(SECOND_JUMP)
                else if (ticks == 4) {
                    player.setAnimation(Animation.JUMP)
                    player.setForceMovement(ForceMovement(player.location, 15, THIRD_JUMP, 35, ForceMovement.WEST))
                } else if (ticks == 5) {
                    player.setLocation(THIRD_JUMP)
                    MarkOfGrace.spawn(player, VarrockRooftopCourse.Companion.MARK_LOCATIONS, 40, 30)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 21.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 5
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(CLOTHES_LINE)
    }

    companion object {
        private val FIRST_JUMP = Location(3212, 3414, 3)
        private val SECOND_JUMP = Location(3210, 3414, 3)
        private val THIRD_JUMP = Location(3208, 3414, 3)
    }
}
