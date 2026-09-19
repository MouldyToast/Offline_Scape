package org.jesse.game.content.skills.agility.wildernesscourse

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class RopeSwing : AgilityCourseObstacle(WildernessCourse::class.java, 2) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setAnimation(SWINGING_ANIM)
        World.sendObjectAnimation(`object`, ROPE_ANIM)
        player.setFaceLocation(FORCE_MOVEMENT.getToFirstTile())
        player.setForceMovement(FORCE_MOVEMENT)
        WorldTasksManager.schedule(WorldTask { player.setLocation(FORCE_MOVEMENT.getToSecondTile()) }, 1)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 52
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(23132)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 20.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 2
    }

    companion object {
        private val START_LOC = Location(3005, 3953, 0)
        private val SWINGING_ANIM = Animation(751)
        private val ROPE_ANIM = Animation(497)
        private val FORCE_MOVEMENT: ForceMovement =
            ForceMovement(Location(3005, 3954, 0), 30, Location(3005, 3958, 0), 60, ForceMovement.NORTH)
    }
}
