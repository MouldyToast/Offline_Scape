package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.MARKET_STALL_14936
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class MarketStall : AgilityCourseObstacle(PollnivneachRooftopCourse::class.java, 2) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(PollnivneachRooftopCourse.Companion.runningStartAnim)

        WorldTasksManager.schedule(WorldTask {
            player.setLocation(START.transform(0, 0, 1))
            player.setForceMovement(jumpToStallMovement)
            player.setAnimation(firstJumpAnim)
            player.sendSound(jumpToStallSound)
        })
        WorldTasksManager.schedule(WorldTask { player.setLocation(OVER_STALL) }, 2)

        WorldTasksManager.schedule(WorldTask {
            player.sendSound(jumpToRoofSound)
            player.setForceMovement(jumpToRoofMovement)
            player.setAnimation(PollnivneachRooftopCourse.Companion.landAnim)
        }, 3)
        WorldTasksManager.schedule(WorldTask {
            player.setLocation(ROOF)
            player.sendSound(landSound)
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
        return 45.0
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(MARKET_STALL_14936)
    }

    companion object {
        private val START: ImmutableLocation = ImmutableLocation(3350, 2968, 1)
        private val OVER_STALL: ImmutableLocation = ImmutableLocation(3350, 2971, 1)
        private val ROOF: ImmutableLocation = ImmutableLocation(3352, 2973, 1)
        private val jumpToStallMovement: ForceMovement = ForceMovement(OVER_STALL, 45, ForceMovement.NORTH)
        private val jumpToRoofMovement: ForceMovement = ForceMovement(ROOF, 30, ForceMovement.NORTH)
        private val firstJumpAnim = Animation(1603)
        private val jumpToStallSound: SoundEffect = SoundEffect(1936)
        private val jumpToRoofSound: SoundEffect = SoundEffect(2468)
        private val landSound: SoundEffect = SoundEffect(2455)
    }
}
