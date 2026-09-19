package org.jesse.game.content.skills.agility.canifisrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.TALL_TREE_14843
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class TallTree : AgilityCourseObstacle(CanifisRooftopCourse::class.java, 1) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.addWalkSteps(FORCE_START.x, FORCE_START.y, -1, false)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0
            private var start = false

            override fun run() {
                if (player.location.positionHash == FORCE_START.positionHash) {
                    player.setFaceLocation(BRANCH)
                    start = true
                }
                if (start) {
                    if (ticks == 0) {
                        player.setAnimation(CLIMB)
                    } else if (ticks == 2) {
                        player.setForceMovement(ForceMovement(`object`, 60, ForceMovement.EAST))
                    } else if (ticks == 5) {
                        player.setLocation(FINISH)
                        MarkOfGrace.spawn(player, CanifisRooftopCourse.MARK_LOCATIONS, 40, 40)
                        stop()
                    }
                    ticks++
                }
            }
        }, 0, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 10.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 40
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 5
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(TALL_TREE_14843)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    companion object {
        private val CLIMB = Animation(1765)

        private val START = Location(3507, 3488, 0)
        private val FORCE_START = Location(3507, 3489, 0)
        private val BRANCH = Location(3508, 3489, 0)
        private val FINISH = Location(3506, 3492, 2)
    }
}
