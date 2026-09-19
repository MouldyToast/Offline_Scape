package org.jesse.game.content.skills.agility.seersrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.WALL_14927
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class StartCourse : AgilityCourseObstacle(SeersRooftopCourse::class.java, 1) {
    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.faceObject(`object`)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) player.setAnimation(CLIMB)
                else if (ticks == 1) {
                    player.setAnimation(HANG)
                    player.setLocation(MAILBOX)
                    player.sendFilteredMessage("...jump, and grab hold of the sign!")
                } else if (ticks == 3) {
                    player.setAnimation(Animation.STOP)
                    player.setLocation(FINISH)
                    player.addAttribute("SeersTrapdoor", 0)
                    MarkOfGrace.spawn(player, SeersRooftopCourse.Companion.MARK_LOCATIONS, 60, 20)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getFilterableStartMessage(success: Boolean): String {
        return "You climb up the wall..."
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 60
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 4
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 45.0
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(WALL_14927)
    }

    companion object {
        private val CLIMB = Animation(737)
        private val HANG = Animation(1118)

        private val MAILBOX = Location(2729, 3488, 1)
        private val FINISH = Location(2729, 3491, 3)
    }
}
