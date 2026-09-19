package org.jesse.game.content.skills.agility.wildernesscourse

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class LogBalance : AgilityCourseObstacle(WildernessCourse::class.java, 4) {
    override fun getStartMessage(success: Boolean): String {
        return "You walk carefully across the slippery log..."
    }

    override fun getEndMessage(success: Boolean): String {
        return "...You make it safely to the other side."
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.addWalkSteps(2994, 3945, -1, false)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 7
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 52
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(23542)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 20.0
    }

    override fun getRenderAnimation(): RenderAnimation {
        return RENDER
    }

    companion object {
        private val RENDER: RenderAnimation = RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK)
        private val START_LOC = Location(3002, 3945, 0)
    }
}
