package org.jesse.game.content.skills.agility.faladorrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.ROUGH_WALL_14898
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class RoughWall : AgilityCourseObstacle(FaladorRooftopCourse::class.java, 1) {
    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.faceObject(`object`)
        player.setAnimation(CLIMB)
        WorldTasksManager.schedule(WorldTask {
            player.setLocation(FINISH)
            MarkOfGrace.spawn(player, FaladorRooftopCourse.Companion.MARK_LOCATIONS, 50, 50)
        }, 1)
        player.addAttribute("SeersTrapdoor", 1)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 50
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(ROUGH_WALL_14898)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 10.0
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    companion object {
        private val CLIMB = Animation(828)
        private val START = Location(3036, 3341, 0)
        private val FINISH = Location(3036, 3342, 3)
    }
}
