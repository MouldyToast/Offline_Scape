package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.DRYING_LINE
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

class DryingLine : AgilityCourseObstacle(PollnivneachRooftopCourse::class.java, 9) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setLocation(START.transform(0, 0, 1))
        player.setAnimation(jumpToLineAnim)
        player.setForceMovement(jumpToLineMovement)
        player.sendSound(jumpToLineSound)
        WorldTasksManager.schedule(WorldTask { player.setLocation(DRYING_LINE_LOC) })
        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(jumpToFinishAnim)
            player.sendSound(jumpToFinishSound)
        }, 1)
        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(PollnivneachRooftopCourse.Companion.landAnim)
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

    override fun getLevel(`object`: WorldObject?): Int {
        return 70
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(DRYING_LINE)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 540.0
    }

    companion object {
        private val START: ImmutableLocation = ImmutableLocation(3362, 3002, 2)
        private val DRYING_LINE_LOC = ImmutableLocation(3363, 3000, 2)
        private val FINISH: ImmutableLocation = ImmutableLocation(3363, 2998, 0)
        private val jumpToLineAnim = Animation(741)
        private val jumpToFinishAnim = Animation(2586)
        private val jumpToLineMovement: ForceMovement = ForceMovement(DRYING_LINE_LOC, 30, ForceMovement.SOUTH)
        private val jumpToLineSound: SoundEffect = SoundEffect(2461)
        private val jumpToFinishSound: SoundEffect = SoundEffect(2462, 0, 15)
    }
}
