package org.jesse.game.content.skills.agility.alkharidrooftop

import org.jesse.game.content.achievementdiary.diaries.LumbridgeDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_14399
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.`object`.WorldObject

class Gap : AgilityCourseObstacle(AlKharidRooftopCourse::class.java, 8) {
    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(JUMP_ANIM)
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> player.setLocation(END_LOC)
                    1 -> {
                        player.getAchievementDiaries().update(LumbridgeDiary.COMPLETE_ALKHARID_COURSE)
                        player.setAnimation(Animation.STOP)
                        MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20)
                        stop()
                    }
                }
            }
        }, 0, 1)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 20
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14399)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 30.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 1
    }

    companion object {
        private val START_LOC = Location(3300, 3192, 3)
        private val END_LOC = Location(3299, 3194, 0)
        private val JUMP_ANIM = Animation(2586)
    }
}
