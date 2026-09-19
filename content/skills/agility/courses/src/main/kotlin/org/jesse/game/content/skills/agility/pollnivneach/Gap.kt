package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.GAP_14938
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject


class Gap : AgilityCourseObstacle(PollnivneachRooftopCourse::class.java, 4) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setLocation(START.transform(0, 0, 1))
        WorldTasksManager.schedule(WorldTask {
            player.setForceMovement(jumpMovement)
            player.sendSound(jumpToEdge)
        })
        WorldTasksManager.schedule(WorldTask {
            player.setLocation(HANG_LOCATION)
            player.setAnimation(climbAnim)
        }, 1)
        WorldTasksManager.schedule(WorldTask {
            player.setLocation(ROOF)
            player.faceDirection(Direction.EAST)
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.Companion.MARK_LOCATIONS, 70, 20)
        }, 3)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 4
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 35.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 70
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(GAP_14938)
    }

    companion object {
        private val START: ImmutableLocation = ImmutableLocation(3362, 2977, 1)
        private val HANG_LOCATION: ImmutableLocation = ImmutableLocation(3365, 2976, 2)
        private val ROOF: ImmutableLocation = ImmutableLocation(3366, 2976, 1)
        private val jumpMovement: ForceMovement = ForceMovement(HANG_LOCATION, 30, ForceMovement.EAST)
        private val climbAnim = Animation(2585)
        private val jumpToEdge: SoundEffect = SoundEffect(2468, 0, 15)
    }
}
