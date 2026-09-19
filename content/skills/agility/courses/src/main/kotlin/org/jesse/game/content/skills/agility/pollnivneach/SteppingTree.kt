package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.TREE_14944
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class SteppingTree : AgilityCourseObstacle(PollnivneachRooftopCourse::class.java, 8) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setLocation(START.transform(0, 0, 1))
        player.setAnimation(jumpAnim)
        player.setForceMovement(jumpToTreeMovement)
        player.sendSound(jumpToTreeSound)
        WorldTasksManager.schedule(WorldTask {
            player.setLocation(TREE)
            player.setAnimation(jumpAnim)
            player.setForceMovement(jumpToRoofMovement)
            player.sendSound(jumpToRoofSound)
        }, 1)
        WorldTasksManager.schedule(WorldTask {
            player.setLocation(FINISH)
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.Companion.MARK_LOCATIONS, 70, 20)
        }, 2)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 60.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 70
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(TREE_14944)
    }

    companion object {
        private val START: ImmutableLocation = ImmutableLocation(3360, 2995, 2)
        private val TREE: ImmutableLocation = ImmutableLocation(3360, 2997, 2)
        private val FINISH: ImmutableLocation = ImmutableLocation(3359, 3000, 2)
        private val jumpAnim = Animation(1603)
        private val jumpToTreeMovement: ForceMovement = ForceMovement(TREE, 30, ForceMovement.NORTH)
        private val jumpToRoofMovement: ForceMovement = ForceMovement(FINISH, 30, ForceMovement.NORTH)
        private val jumpToTreeSound: SoundEffect = SoundEffect(1936)
        private val jumpToRoofSound: SoundEffect = SoundEffect(1936, 0, 10)
    }
}
