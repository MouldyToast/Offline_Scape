package org.jesse.game.content.skills.agility.ardougnerooftop

import org.jesse.game.content.achievementdiary.diaries.ArdougneDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_15612
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Gap4 : AgilityCourseObstacle(ArdougneRooftopCourse::class.java, 7) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 90
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_15612)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 17
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
                    }

                    3 -> player.addWalkSteps(2661, 3298, -1, false)
                    7 -> {
                        player.setAnimation(ANIM3)
                        player.autoForceMovement(LOCATION5, 15, 30)
                    }

                    10 -> player.addWalkSteps(2666, 3297, -1, false)
                    14 -> {
                        player.setAnimation(ANIM3)
                        player.autoForceMovement(LOCATION6, 15, 30)
                    }

                    15 -> player.setAnimation(ANIM4)
                    16 -> {
                        player.setAnimation(ANIM2)
                        player.setLocation(LOCATION2)
                        player.getAchievementDiaries().update(ArdougneDiary.COMPLETE_ARDOUGNE_ROOFTOP_LAP)
                        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90)
                    }
                }
            }
        }, 1, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 529.0
    }

    companion object {
        private val ANIM1 = Animation(2586, 15)
        private val ANIM2 = Animation(2588)
        private val ANIM3 = Animation(741)
        private val ANIM4 = Animation(2586)

        private val LOCATION1 = Location(2658, 3298, 1)
        private val LOCATION2 = Location(2668, 3297, 0)

        private val LOCATION5 = Location(2663, 3297, 1)
        private val LOCATION6 = Location(2667, 3297, 1)

        private val EFFECT1: SoundEffect = SoundEffect(2462, 0, 15)
    }
}
