package org.jesse.game.content.skills.agility.alkharidrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.TIGHTROPE_14398
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class FirstTightRope : AgilityCourseObstacle(AlKharidRooftopCourse::class.java, 2) {
    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20)
        player.addWalkSteps(3272, 3181, -1, false)
        player.addWalkSteps(3272, 3172, -1, false)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 10
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(TIGHTROPE_14398)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 30.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 9
    }

    override fun getRenderAnimation(): RenderAnimation {
        return RENDER
    }

    companion object {
        private val RENDER: RenderAnimation = RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK)
        private val START_LOC = Location(3272, 3182, 3)
    }
}
