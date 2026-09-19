package org.jesse.game.content.skills.agility.draynorrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.NARROW_WALL
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class NarrowWall : AgilityCourseObstacle(DraynorRooftopCourse::class.java, 4) {
    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        MarkOfGrace.spawn(player, DraynorRooftopCourse.MARK_LOCATIONS, 40, 10)
        player.setAnimation(Animation(753))
        player.addWalkSteps(3089, 3262, -1, false)
        player.addWalkSteps(3088, 3261, -1, false)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 10
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(NARROW_WALL)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 7.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 4
    }

    override fun getRenderAnimation(): RenderAnimation {
        return RENDER
    }

    companion object {
        private val RENDER: RenderAnimation = RenderAnimation(757, 757, 756, 756, 756, 756, -1)
        private val START_LOC = Location(3089, 3265, 3)
    }
}
