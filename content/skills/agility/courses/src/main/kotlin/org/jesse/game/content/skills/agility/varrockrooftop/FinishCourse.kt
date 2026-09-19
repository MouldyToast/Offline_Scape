package org.jesse.game.content.skills.agility.varrockrooftop

import org.jesse.game.content.achievementdiary.diaries.VarrockDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.EDGE
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dailychallenge.challenge.SkillingChallenge
import org.jesse.game.world.`object`.WorldObject

class FinishCourse : AgilityCourseObstacle(VarrockRooftopCourse::class.java, 8) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        val ledge = Location(player.getX(), player.getY() + 1, 3)
        val finish = Location(player.getX(), player.getY() + 2, 0)
        player.setFaceLocation(ledge)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP)
                    player.setForceMovement(ForceMovement(player.location, 15, ledge, 35, ForceMovement.NORTH))
                } else if (ticks == 1) {
                    player.setLocation(ledge)
                    player.setAnimation(LAND)
                } else if (ticks == 2) {
                    player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_LAPS_VARROCK_COURSE)
                    player.getAchievementDiaries().update(VarrockDiary.COMPLETE_AGILITY_COURSE_LAP)
                    player.setLocation(finish)
                    MarkOfGrace.spawn(player, VarrockRooftopCourse.Companion.MARK_LOCATIONS, 40, 30)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(EDGE)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 125.0
    }

    companion object {
        private val LAND = Animation(2586)
    }
}
