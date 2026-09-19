package org.jesse.game.content.skills.agility.ardougnerooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.STEEP_ROOF
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class SteepRoof : AgilityCourseObstacle(ArdougneRooftopCourse::class.java, 6) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 90
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(STEEP_ROOF)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 6
    }

    override fun preconditions(player: Player, `object`: WorldObject?): Boolean {
        if (player.location.matches(Location(2654, 3299, 3))) {
            player.sendMessage("You can't go back from here.")
            return false
        }
        return true
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.setFaceLocation(`object`)
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> {
                        player.setAnimation(ANIM1)
                        player.getAppearance().setRenderAnimation(RENDER)
                    }

                    1 -> player.autoForceMovement(Location(2654, 3299, 3), 30)
                    2 -> player.autoForceMovement(Location(2656, 3297, 3), 30)
                    5 -> {
                        player.addWalkSteps(2656, 3295, 3)
                        player.getAppearance().resetRenderAnimation()
                        player.setAnimation(ANIM2)
                        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90)
                        stop()
                    }
                }
            }
        }, 1, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 57.0
    }

    companion object {
        private val ANIM1 = Animation(753)
        private val ANIM2 = Animation(759)

        private val RENDER: RenderAnimation = RenderAnimation(757, 757, 756, 756, 756, 756, -1)
    }
}
