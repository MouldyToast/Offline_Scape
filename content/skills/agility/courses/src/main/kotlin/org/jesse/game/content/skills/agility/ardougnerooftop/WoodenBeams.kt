package org.jesse.game.content.skills.agility.ardougnerooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.WOODEN_BEAMS
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class WoodenBeams : AgilityCourseObstacle(ArdougneRooftopCourse::class.java, 1) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 90
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(WOODEN_BEAMS)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 5
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.setFaceLocation(`object`)
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> player.setAnimation(ANIM1)
                    1 -> {
                        player.setLocation(LOCATION1)
                        player.setAnimation(ANIM2)
                    }

                    2 -> {
                        player.setLocation(LOCATION2)
                        player.setAnimation(ANIM2)
                    }

                    3 -> {
                        player.setLocation(LOCATION3)
                        player.setAnimation(ANIM3)
                        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90)
                        stop()
                    }
                }
            }
        }, 1, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 43.0
    }

    companion object {
        private val ANIM1 = Animation(737, 15)
        private val ANIM2 = Animation(737)
        private val ANIM3 = Animation(2588)

        private val LOCATION1 = Location(2673, 3298, 1)
        private val LOCATION2 = Location(2673, 3298, 2)
        private val LOCATION3 = Location(2671, 3299, 3)
    }
}
