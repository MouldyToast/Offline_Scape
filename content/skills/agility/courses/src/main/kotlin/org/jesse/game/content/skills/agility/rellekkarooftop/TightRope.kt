package org.jesse.game.content.skills.agility.rellekkarooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.TIGHTROPE_14987
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class TightRope : AgilityCourseObstacle(RellekkaRooftopCourse::class.java, 3) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 80
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(TIGHTROPE_14987)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.addWalkSteps(2626, 3654, -1, false)
        MarkOfGrace.spawn(player, RellekkaRooftopCourse.Companion.MARK_LOCATIONS, 40, 80)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 40.0
    }

    override fun getRenderAnimation(): RenderAnimation {
        return RENDER
    }

    companion object {
        private val RENDER: RenderAnimation = RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK)

        private val ANIM1 = Animation(1995, 15)
        private val ANIM2 = Animation(1603)

        private val LOCATION1 = Location(2622, 3668, 3)
        private val EFFECT1: SoundEffect = SoundEffect(2495, 3, 0)
    }
}
