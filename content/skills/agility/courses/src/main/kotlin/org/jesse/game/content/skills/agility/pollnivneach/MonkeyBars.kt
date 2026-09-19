package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.MONKEYBARS
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class MonkeyBars : AgilityCourseObstacle(PollnivneachRooftopCourse::class.java, 7) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(jumpAnim)
        player.sendSound(crossingSound)
        WorldTasksManager.schedule(WorldTask { player.addWalkSteps(FINISH.x, FINISH.y, -1, false) })
        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(dropAnim)
            player.sendSound(landSound)
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.Companion.MARK_LOCATIONS, 70, 20)
        }, 8)
    }

    override fun getRenderAnimation(): RenderAnimation {
        return RENDER
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 9
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 55.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 70
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(MONKEYBARS)
    }

    companion object {
        private val START: ImmutableLocation = ImmutableLocation(3358, 2984, 2)
        private val FINISH: ImmutableLocation = ImmutableLocation(3358, 2991, 2)
        private val jumpAnim = Animation(742)
        private val dropAnim = Animation(743)
        private val RENDER: RenderAnimation = RenderAnimation(745, 744, 744, 744, 744, 744, 744)
        private val crossingSound: SoundEffect = SoundEffect(2466, 0, 40, 15)
        private val landSound: SoundEffect = SoundEffect(2473)
    }
}
