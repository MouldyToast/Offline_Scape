package org.jesse.game.content.skills.agility.alkharidrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.CABLE
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Cable : AgilityCourseObstacle(AlKharidRooftopCourse::class.java, 3) {
    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return Location(3266, 3166, 3)
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> {
                        val startLoc = player.location
                        player.sendFilteredMessage("You begin an almighty run-up...")
                        player.setAnimation(RUNUP_ANIM)
                        player.setLocation(FIRST_LOC)
                        player.setForceMovement(
                            ForceMovement(
                                startLoc, 0,
                                FIRST_LOC, 45,
                                ForceMovement.EAST
                            )
                        )
                    }

                    1 -> {
                        player.sendFilteredMessage("You gained enough momentum to swing to the other side!")
                        player.setAnimation(ROPESWING_ANIM)
                        player.setLocation(FIRST_LOC)
                        player.setForceMovement(
                            ForceMovement(
                                FIRST_LOC, 0,
                                SECOND_LOC, 60,
                                ForceMovement.EAST
                            )
                        )
                    }

                    2 -> {
                        player.setLocation(SECOND_LOC)
                        MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20)
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
        return intArrayOf(CABLE)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 40.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    companion object {
        private val FIRST_LOC = Location(3268, 3166, 3)
        private val SECOND_LOC = Location(3284, 3166, 3)
        private val RUNUP_ANIM = Animation(1995)
        private val ROPESWING_ANIM = Animation(751)
    }
}