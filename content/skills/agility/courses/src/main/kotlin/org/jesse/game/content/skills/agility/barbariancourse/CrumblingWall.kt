package org.jesse.game.content.skills.agility.barbariancourse

import org.jesse.game.content.achievementdiary.diaries.KandarinDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location

import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class CrumblingWall : AgilityCourseObstacle(BarbarianOutpostCourse::class.java, 5) {
    override fun preconditions(player: Player, `object`: WorldObject): Boolean {
        if (player.getX() >= `object`.x) {
            player.sendMessage("You can\'t climb over the wall from here.")
            return false
        }
        return true
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        val destination = Location(`object`.x + 1, `object`.y, `object`.plane)
        player.setAnimation(ANIM)
        player.setForceMovement(ForceMovement(destination, 90, ForceMovement.EAST))
        WorldTasksManager.schedule(WorldTask {
            if (destination.x == 2543) {
                player.getAchievementDiaries().update(KandarinDiary.COMPLETE_BARBARIAN_AGILITY_COURSE_LAP)
            }
            player.setLocation(destination)
        }, 2)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject): Location {
        if (`object`.matches(Location(2536, 3553, 0))) {
            return WESTERN_START
        } else if (`object`.matches(Location(2539, 3553, 0))) {
            return MIDDLE_START
        }
        return EASTERN_START
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 35
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(1948)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 13.7
    }

    override fun getStartMessage(success: Boolean): String {
        return "You climb the low wall..."
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    companion object {
        private val WESTERN_START = Location(2535, 3553, 0)
        private val MIDDLE_START = Location(2538, 3553, 0)
        private val EASTERN_START = Location(2541, 3553, 0)
        private val ANIM = Animation(839, 15)
    }
}
