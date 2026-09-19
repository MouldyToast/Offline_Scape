package org.jesse.game.content.skills.agility.draynorrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.ROUGH_WALL
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class RoughWall : AgilityCourseObstacle(DraynorRooftopCourse::class.java, 1) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        MarkOfGrace.spawn(player, DraynorRooftopCourse.MARK_LOCATIONS, 40, 10)
        player.useStairs(828, END_LOC, 1, 1)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 10
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(ROUGH_WALL)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 5.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 1
    }

    companion object {
        private val END_LOC = Location(3102, 3279, 3)
    }
}
