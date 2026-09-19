package org.jesse.game.content.skills.agility.ardougnerooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_15610
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Gap2 : AgilityCourseObstacle(ArdougneRooftopCourse::class.java, 4) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 90
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_15610)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.setFaceLocation(`object`)
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> {
                        player.setAnimation(ANIM1)
                        player.sendSound(EFFECT1)
                    }

                    1 -> {
                        player.setAnimation(ANIM2)
                        player.setLocation(LOCATION1)
                        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90)
                        stop()
                    }
                }
            }
        }, 1, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 21.0
    }

    companion object {
        private val ANIM1 = Animation(2586, 15)
        private val ANIM2 = Animation(2588)

        private val LOCATION1 = Location(2653, 3314, 3)
        private val EFFECT1: SoundEffect = SoundEffect(2462, 0, 15)
    }
}