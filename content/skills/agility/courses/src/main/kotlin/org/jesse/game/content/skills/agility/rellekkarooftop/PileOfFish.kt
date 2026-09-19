package org.jesse.game.content.skills.agility.rellekkarooftop

import org.jesse.game.content.achievementdiary.diaries.FremennikDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.PILE_OF_FISH
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class PileOfFish : AgilityCourseObstacle(RellekkaRooftopCourse::class.java, 7) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 80
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(PILE_OF_FISH)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 4
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
                        player.getAchievementDiaries().update(FremennikDiary.COMPLETE_RELLEKKA_AGILITY_COURSE_LAP)
                        player.addWalkSteps(2652, 3676, -1, false)
                    }

                    2 -> player.addWalkSteps(2652, 3676, -1, false)
                    3 -> {
                        MarkOfGrace.spawn(player, RellekkaRooftopCourse.Companion.MARK_LOCATIONS, 40, 80)
                        stop()
                    }
                }
            }
        }, 1, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 475.0
    }

    companion object {
        private val ANIM1 = Animation(2586, 15)
        private val ANIM2 = Animation(2588)

        private val LOCATION1 = Location(2653, 3676, 0)
        private val EFFECT1: SoundEffect = SoundEffect(2462, 0, 15)
    }
}
