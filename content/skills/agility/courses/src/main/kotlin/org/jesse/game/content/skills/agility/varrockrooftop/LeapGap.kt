package org.jesse.game.content.skills.agility.varrockrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.*
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class LeapGap : AgilityCourseObstacle(VarrockRooftopCourse::class.java, 3) {
    override fun startSuccess(player: Player, `object`: WorldObject) {
        val direction = `object`.id == GAP_14414
        player.setFaceLocation(if (direction) LAND else LAND2)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(JUMP)
                } else if (ticks == 1) {
                    player.setLocation(if (direction) LAND else LAND2)
                    MarkOfGrace.spawn(player, VarrockRooftopCourse.Companion.MARK_LOCATIONS, 40, 30)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getSuccessXp(`object`: WorldObject): Double {
        if (`object`.id == GAP_14835) {
            return 4.0
        }
        return 17.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14414, GAP_14835)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject): Location {
        return if (`object`.id == GAP_14414) START else START2
    }

    companion object {
        private val JUMP = Animation(2586)

        private val START = Location(3201, 3416, 3)

        private val LAND = Location(3197, 3416, 1)

        private val START2 = Location(3232, 3402, 3)

        private val LAND2 = Location(3236, 3403, 3)
    }
}
