package org.jesse.game.content.skills.agility.seersrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.content.skills.agility.Shortcut
import org.jesse.game.obj.ids.TIGHTROPE_14932
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Tightrope : AgilityCourseObstacle(SeersRooftopCourse::class.java, 3), Shortcut {
    override fun handleObjectAction(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String) {
        super<AgilityCourseObstacle>.handleObjectAction(player, `object`, name, optionId, option)
    }

    override fun startSuccess(player: Player, `object`: WorldObject?) {
        MarkOfGrace.spawn(player, SeersRooftopCourse.Companion.MARK_LOCATIONS, 60, 20)
        player.addWalkSteps(2710, 3489, -1, false)
        player.addWalkSteps(2710, 3481, -1, false)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 60
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(TIGHTROPE_14932)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 9
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 20.0
    }

    override fun getRenderAnimation(): RenderAnimation {
        return RENDER
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    companion object {
        private val RENDER: RenderAnimation = RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK)
        private val START = Location(2710, 3490, 2)
    }
}
