package org.jesse.game.content.skills.agility.canifisrooftop

import org.jesse.game.content.achievementdiary.diaries.MorytaniaDiary
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.content.skills.agility.canifisrooftop.GapInfo.Companion.get
import org.jesse.game.obj.ids.*
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class JumpGap : AgilityCourseObstacle(CanifisRooftopCourse::class.java, 2) {
    override fun startSuccess(player: Player, `object`: WorldObject) {
        val gap = get(`object`.id)
        if (gap == null) {
            return
        }
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(JUMP)
                } else if (ticks == 1) {
                    if (gap == GapInfo.FIFTH) {
                        player.getAchievementDiaries().update(MorytaniaDiary.COMPLETE_CANIFIS_COURSE_LAP)
                    }
                    player.setAnimation(LAND)
                    player.setLocation(gap.finish)
                    MarkOfGrace.spawn(player, CanifisRooftopCourse.MARK_LOCATIONS, 40, 40)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 40
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14844, GAP_14845, GAP_14846, GAP_14847, GAP_14897)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    override fun getSuccessXp(`object`: WorldObject): Double {
        if (`object`.id == GAP_14897) return 175.0
        return (if (`object`.id == 10823) 11 else 8).toDouble()
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject): Location? {
        return if (get(`object`.id) == null) `object` else get(`object`.id)!!.start
    }

    companion object {
        private val JUMP = Animation(2586)

        private val LAND = Animation(2588)
    }
}
