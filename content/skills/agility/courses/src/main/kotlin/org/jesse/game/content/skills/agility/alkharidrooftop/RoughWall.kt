package org.jesse.game.content.skills.agility.alkharidrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.ROUGH_WALL_11633
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class RoughWall : AgilityCourseObstacle(AlKharidRooftopCourse::class.java, 1) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20)
        player.useStairs(828, END_LOC, 1, 1)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 20
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(ROUGH_WALL_11633)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 10.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 1
    }

    companion object {
        private val END_LOC = Location(3273, 3192, 3)
    }
}
