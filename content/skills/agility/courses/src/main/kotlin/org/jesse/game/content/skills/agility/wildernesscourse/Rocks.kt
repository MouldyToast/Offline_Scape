package org.jesse.game.content.skills.agility.wildernesscourse

import org.jesse.game.content.achievementdiary.diaries.WildernessDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dailychallenge.challenge.SkillingChallenge
import org.jesse.game.world.`object`.WorldObject

class Rocks : AgilityCourseObstacle(WildernessCourse::class.java, 5) {
    override fun getEndMessage(success: Boolean): String {
        return "You reach the top."
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.setAnimation(CLIMBING_ANIM)
        player.setForceMovement(
            ForceMovement(
                Location(`object`.x, `object`.y - 3, `object`.plane),
                120,
                ForceMovement.SOUTH
            )
        )
        WorldTasksManager.schedule(WorldTask {
            player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_LAPS_WILDERNESS_AGILITY_COURSE)
            player.getAchievementDiaries().update(WildernessDiary.COMPLETE_AGILITY_COURSE_LAP)
            player.setAnimation(Animation.STOP)
            player.setLocation(Location(`object`.x, `object`.y - 3, `object`.plane))
        }, 3)
    }


    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 4
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 52
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(23640)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 0.0
    }

    companion object {
        private val CLIMBING_ANIM = Animation(740)
    }
}
