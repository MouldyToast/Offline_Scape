package org.jesse.game.content.skills.agility.wildernesscourse

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class ObstaclePipe : AgilityCourseObstacle(WildernessCourse::class.java, 1) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.resetWalkSteps()
        val obj: Location = Location(player.location)
        player.addWalkSteps(obj.x, obj.y, -1, false)
        player.lock()
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0
            override fun run() {
                when (ticks++) {
                    0 -> player.setAnimation(CLIMB_ANIM)
                    1 -> player.setForceMovement(ForceMovement(Location(obj.x, obj.y + 5, 0), 90, ForceMovement.NORTH))
                    4 -> player.setLocation(Location(obj.x, obj.y + 5, 0))
                    5 -> player.setForceMovement(
                        ForceMovement(
                            Location(obj.x, obj.y + 12, 0),
                            150,
                            ForceMovement.NORTH
                        )
                    )

                    8 -> player.setAnimation(CLIMB_ANIM)
                    10 -> player.setLocation(Location(obj.x, obj.y + 13, 0))
                    11 -> {
                        player.unlock()
                        stop()
                    }
                }
            }
        }, 0, 0)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 52
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(23137)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 12.5
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 11
    }

    companion object {
        private val CLIMB_ANIM = Animation(749)
        private val START = Location(3004, 3937, 0)
    }
}
