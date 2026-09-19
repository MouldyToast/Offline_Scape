package org.jesse.game.content.skills.agility.draynorrooftop

import org.jesse.game.content.achievementdiary.diaries.LumbridgeDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.CRATE_11632
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Crate : AgilityCourseObstacle(DraynorRooftopCourse::class.java, 7) {
    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(FIRST_ANIM)
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0
            override fun run() {
                when (ticks++) {
                    0 -> {
                        player.setLocation(CRATE_LOC)
                        player.setAnimation(SECOND_ANIM)
                    }

                    1 -> player.setAnimation(Animation.STOP)
                    2 -> {
                        player.setAnimation(FIRST_ANIM)
                        player.setLocation(END_LOC)
                        player.setAnimation(SECOND_ANIM)
                    }

                    3 -> {
                        player.getAchievementDiaries().update(LumbridgeDiary.COMPLETE_DRAYNOR_VILLAGE_COURSE)
                        player.setAnimation(Animation.STOP)
                        MarkOfGrace.spawn(player, DraynorRooftopCourse.MARK_LOCATIONS, 40, 10)
                        stop()
                    }
                }
            }
        }, 0, 1)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 10
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(CRATE_11632)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 79.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 5
    }

    companion object {
        private val START_LOC = Location(3101, 3261, 3)
        private val END_LOC = Location(3103, 3261, 0)
        private val CRATE_LOC = Location(3102, 3261, 1)
        private val FIRST_ANIM = Animation(2586, 15)
        private val SECOND_ANIM = Animation(2588)
    }
}
