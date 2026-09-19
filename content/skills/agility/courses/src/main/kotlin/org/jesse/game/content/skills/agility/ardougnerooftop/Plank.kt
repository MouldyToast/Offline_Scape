package org.jesse.game.content.skills.agility.ardougnerooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.PLANK_26635
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Plank : AgilityCourseObstacle(ArdougneRooftopCourse::class.java, 3) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 90
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(PLANK_26635)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 5
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.sendSound(EFFECT1)
        player.addWalkSteps(2656, 3318, -1, false)
        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 50.0
    }

    override fun getRenderAnimation(): RenderAnimation {
        return RENDER
    }

    companion object {
        private val RENDER: RenderAnimation = RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK)
        private val EFFECT1: SoundEffect = SoundEffect(2495, 4, 0)
    }
}
