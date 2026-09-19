package org.jesse.game.content.skills.agility.barbariancourse

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class BalancingLedge : AgilityCourseObstacle(BarbarianOutpostCourse::class.java, 4) {
    override fun getStartMessage(success: Boolean): String {
        return "You put your foot on the ledge and try to edge across..."
    }

    override fun getEndMessage(success: Boolean): String {
        return "You skillfully edge across the gap."
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.addWalkSteps(2532, 3547, -1, false)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 35
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(23547)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 22.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 4
    }

    override fun getRenderAnimation(): RenderAnimation {
        return RENDER
    }

    companion object {
        private val RENDER: RenderAnimation = RenderAnimation(RenderAnimation.STAND, 756, RenderAnimation.WALK)
        private val START_LOC = Location(2536, 3547, 1)
    }
}
