/**
 *
 */
package org.jesse.game.content.skills.agility.seersrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_14928
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class FirstJumpGap : AgilityCourseObstacle(SeersRooftopCourse::class.java, 2) {
    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.faceObject(`object`)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) player.setAnimation(LEAP)
                else if (ticks == 1) {
                    player.setLocation(LEDGE)
                    player.setAnimation(Animation.LAND)
                } else if (ticks == 2) player.setAnimation(LEAP)
                else if (ticks == 3) {
                    player.setAnimation(Animation.LAND)
                    player.setLocation(END)
                    MarkOfGrace.spawn(player, SeersRooftopCourse.Companion.MARK_LOCATIONS, 60, 20)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 60
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14928)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 4
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 20.0
    }

    companion object {
        private val LEAP = Animation(2586)

        private val START = Location(2721, 3494, 3)
        private val LEDGE = Location(2719, 3495, 2)
        private val END = Location(2713, 3494, 2)
    }
}
