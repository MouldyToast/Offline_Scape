package org.jesse.game.content.skills.agility.faladorrooftop

import org.jesse.game.content.achievementdiary.diaries.FaladorDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.EDGE_14925
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class FinishCourse : AgilityCourseObstacle(FaladorRooftopCourse::class.java, 6) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        val ledge = Location(player.getX() + 3, player.getY(), 3)
        val finish = Location(ledge.x + 2, player.getY(), 0)
        player.setFaceLocation(finish)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(LEAP)
                    player.setForceMovement(ForceMovement(ledge, 45, ForceMovement.EAST))
                } else if (ticks == 2) {
                    player.setLocation(ledge)
                } else if (ticks == 3) {
                    player.setAnimation(JUMP)
                } else if (ticks == 4) {
                    player.getAchievementDiaries().update(FaladorDiary.COMPLETE_ROOFTOP_COURSE)
                    player.setAnimation(LAND)
                    player.setLocation(finish)
                    MarkOfGrace.spawn(player, FaladorRooftopCourse.Companion.MARK_LOCATIONS, 50, 50)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return Location(3024, 3333, 3)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 50
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 4
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 180.0
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(EDGE_14925)
    }

    companion object {
        private val LEAP = Animation(1603)
        private val JUMP = Animation(2583)
        private val LAND = Animation(2588)
    }
}