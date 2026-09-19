package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.TREE_14939
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

class Tree : AgilityCourseObstacle(PollnivneachRooftopCourse::class.java, 5) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setLocation(START.transform(0, 0, 1))

        WorldTasksManager.schedule(WorldTask {
            player.setForceMovement(jumpToFirstBranchMovement)
            player.sendSound(jumpToFirstBranchSound)
        })
        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(firstBranchAnim)
            player.setLocation(FIRST_BRANCH)
            player.sendSound(jumpToSecondBranchSound)
        }, 1)

        WorldTasksManager.schedule(WorldTask { player.setForceMovement(jumpToSecondBranchMovement) }, 2)
        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(secondBranchAnim)
            player.setLocation(SECOND_BRANCH)
            player.sendSound(jumpToSecondBranchSound)
        }, 3)

        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(PollnivneachRooftopCourse.Companion.landAnim)
            player.setForceMovement(jumpToRoofMovement)
        }, 4)

        WorldTasksManager.schedule(WorldTask {
            player.setLocation(ROOF)
            player.sendSound(landSound)
            player.faceDirection(Direction.NORTH)
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.Companion.MARK_LOCATIONS, 70, 20)
        }, 5)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 6
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 75.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 70
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(TREE_14939)
    }

    companion object {
        private val START: ImmutableLocation = ImmutableLocation(3368, 2976, 1)
        private val FIRST_BRANCH: ImmutableLocation = ImmutableLocation(3368, 2978, 2)
        private val SECOND_BRANCH: ImmutableLocation = ImmutableLocation(3368, 2980, 2)
        private val ROOF: ImmutableLocation = ImmutableLocation(3368, 2982, 1)
        private val jumpToFirstBranchMovement: ForceMovement = ForceMovement(FIRST_BRANCH, 30, ForceMovement.EAST)
        private val jumpToSecondBranchMovement: ForceMovement = ForceMovement(SECOND_BRANCH, 30, ForceMovement.EAST)
        private val jumpToRoofMovement: ForceMovement = ForceMovement(ROOF, 30, ForceMovement.EAST)
        private val firstBranchAnim = Animation(1122)
        private val secondBranchAnim = Animation(1124)
        private val jumpToFirstBranchSound: SoundEffect = SoundEffect(2468, 0, 10)
        private val jumpToSecondBranchSound: SoundEffect = SoundEffect(2459, 0, 35)
        private val landSound: SoundEffect = SoundEffect(2455)
    }
}
