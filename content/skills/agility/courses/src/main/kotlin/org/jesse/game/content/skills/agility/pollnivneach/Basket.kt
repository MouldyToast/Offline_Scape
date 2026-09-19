package org.jesse.game.content.skills.agility.pollnivneach

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.BASKET_14935
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Christopher
 * @since 3/30/2020
 */
class Basket : AgilityCourseObstacle(PollnivneachRooftopCourse::class.java, 1) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setLocation(player.location.transform(0, 0, 1))
        player.setAnimation(jumpAnim)
        player.sendSound(jumpSound)
        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(PollnivneachRooftopCourse.Companion.landAnim)
            player.setLocation(OVER_BASKET)
        })

        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(jumpAnim)
            player.sendSound(jumpSound)
        }, 1)
        WorldTasksManager.schedule(WorldTask {
            player.setAnimation(PollnivneachRooftopCourse.Companion.landAnim)
            player.setLocation(FINISH)
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.Companion.MARK_LOCATIONS, 70, 20)
        }, 2)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 3
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 70
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 10.0
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(BASKET_14935)
    }

    companion object {
        private val OVER_BASKET: ImmutableLocation = ImmutableLocation(3351, 2962, 1)
        private val FINISH: ImmutableLocation = ImmutableLocation(3351, 2964, 1)
        private val jumpAnim = Animation(2583)
        private val jumpSound: SoundEffect = SoundEffect(2468, 0, 20)
    }
}
