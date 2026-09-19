package org.jesse.game.content.skills.agility.seersrooftop

import org.jesse.game.content.achievementdiary.diaries.KandarinDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.EDGE_14931
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dailychallenge.challenge.SkillingChallenge
import org.jesse.game.world.`object`.WorldObject

class FinishCourse : AgilityCourseObstacle(SeersRooftopCourse::class.java, 6) {
    override fun getLevel(`object`: WorldObject?): Int {
        return 60
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(EDGE_14931)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setFaceLocation(FINISH)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) player.setAnimation(Animation.LEAP)
                else if (ticks == 1) {
                    player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_LAPS_SEERS_COURSE)
                    player.getAchievementDiaries().update(KandarinDiary.COMPLETE_SEERS_VILLAGE_AGILITY_COURSE_LAP)
                    player.setAnimation(Animation.LAND)
                    player.setLocation(FINISH)
                    MarkOfGrace.spawn(player, SeersRooftopCourse.Companion.MARK_LOCATIONS, 60, 20)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 435.0
    }

    companion object {
        private val FINISH = Location(2704, 3464, 0)
    }
}
