package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.BANNER_14937
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Banner : AgilityCourseObstacle(PollnivneachRooftopCourse::class.java, 3) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(PollnivneachRooftopCourse.Companion.runningStartAnim)

        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(Animation.STOP)
            player.setLocation(START.transform(0, 0, 1))
            player.setForceMovement(jumpToBannerMovement)
            player.sendSound(jumpToBannerSound)
        })
        WorldTasksManager.schedule(WorldTask {
            player.setLocation(BANNER)
            player.setAnimation(hangingAnim)
        }, 1)

        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(PollnivneachRooftopCourse.Companion.landAnim)
            player.setForceMovement(jumpToRoofMovement)
            player.sendSound(jumpToRoofSound)
        }, 3)
        WorldTasksManager.schedule(WorldTask {
            player.setLocation(FINISH)
            player.sendSound(landSound)
            player.faceDirection(Direction.NORTH_EAST)
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.Companion.MARK_LOCATIONS, 70, 20)
        }, 4)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 5
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 70
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 65.0
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(BANNER_14937)
    }

    companion object {
        private val START: ImmutableLocation = ImmutableLocation(3354, 2976, 1)
        private val BANNER: ImmutableLocation = ImmutableLocation(3357, 2977, 2)
        private val FINISH: ImmutableLocation = ImmutableLocation(3360, 2977, 1)
        private val jumpToBannerMovement: ForceMovement = ForceMovement(BANNER, 30, ForceMovement.NORTH)
        private val jumpToRoofMovement: ForceMovement = ForceMovement(FINISH, 30, ForceMovement.NORTH)
        private val hangingAnim = Animation(1118)
        private val jumpToBannerSound: SoundEffect = SoundEffect(2468, 0, 10)
        private val jumpToRoofSound: SoundEffect = SoundEffect(2459, 0, 35)
        private val landSound: SoundEffect = SoundEffect(2455)
    }
}
