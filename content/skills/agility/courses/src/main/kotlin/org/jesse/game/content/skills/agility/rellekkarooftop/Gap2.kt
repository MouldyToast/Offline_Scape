package org.jesse.game.content.skills.agility.rellekkarooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_14990
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Gap2 : AgilityCourseObstacle(RellekkaRooftopCourse::class.java, 4) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 80
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14990)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 14
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.setFaceLocation(`object`)
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> player.autoForceMovement(player.location.transform(0, 3, 0), 30, 25)
                    1 -> {
                        player.setAnimation(ANIM1)
                        player.getAppearance().setRenderAnimation(RENDER1)
                    }

                    2 -> player.addWalkSteps(2635, 3658, -1, false)
                    8 -> {
                        player.getAppearance().setRenderAnimation(RENDER2)
                        player.addWalkSteps(2640, 3653, -1, false)
                    }

                    14 -> {
                        player.getAppearance().resetRenderAnimation()
                        MarkOfGrace.spawn(player, RellekkaRooftopCourse.Companion.MARK_LOCATIONS, 40, 80)
                        stop()
                    }
                }
            }
        }, 1, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 85.0
    }

    companion object {
        private val RENDER1: RenderAnimation = RenderAnimation(755, 755, 754, 754, 754, 754, -1)
        private val RENDER2: RenderAnimation = RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK)


        private val ANIM1 = Animation(752)
    }
}
