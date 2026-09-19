package org.jesse.game.content.skills.agility.alkharidrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.ZIP_LINE_14403
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class ZipLine : AgilityCourseObstacle(AlKharidRooftopCourse::class.java, 4) {
    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(JUMP_ANIM)
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> {
                        player.setLocation(FIRST_LOC)
                        player.setAnimation(TEETH_GRIP_ANIM)
                    }

                    1 -> {
                        player.setAnimation(TEETH_GRIP_SWING_ANIM)
                        player.setForceMovement(FORCE_MOVEMENT)
                    }

                    6 -> {
                        player.setAnimation(Animation.STOP)
                        player.setLocation(END_LOC)
                        WorldTasksManager.schedule(WorldTask {
                            player.addWalkSteps(walkDestination.x, walkDestination.y, 1, true)
                            MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20)
                        })
                        stop()
                    }
                }
            }
        }, 0, 1)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 20
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(ZIP_LINE_14403)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 40.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 14
    }

    companion object {
        private val START_LOC = Location(3301, 3163, 3)
        private val FIRST_LOC = Location(3303, 3163, 1)
        private val END_LOC = Location(3314, 3163, 1)
        private val walkDestination = Location(3315, 3163, 1)
        private val JUMP_ANIM = Animation(2586, 10)
        private val TEETH_GRIP_ANIM = Animation(1601)
        private val TEETH_GRIP_SWING_ANIM = Animation(1602)
        private val FORCE_MOVEMENT: ForceMovement = ForceMovement(END_LOC, 300, ForceMovement.EAST)
    }
}
