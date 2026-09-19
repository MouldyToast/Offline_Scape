package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.ROUGH_WALL_14940
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class RoughWall : AgilityCourseObstacle(PollnivneachRooftopCourse::class.java, 6) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.faceDirection(Direction.NORTH)
        player.setAnimation(climbingAnim)
        WorldTasksManager.schedule(WorldTask {
            player.setLocation(FINISH)
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.Companion.MARK_LOCATIONS, 70, 20)
        })
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 5.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 70
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(ROUGH_WALL_14940)
    }

    companion object {
        private val FINISH: ImmutableLocation = ImmutableLocation(3365, 2983, 2)
        private val climbingAnim = Animation(828)
    }
}
