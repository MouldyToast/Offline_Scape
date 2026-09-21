package org.jesse.game.content.skills.agility.pyramid

import org.jesse.game.content.skills.agility.IrreversibleDirection
import org.jesse.game.content.skills.agility.pyramid.area.AgilityPyramidArea
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class JumpGap : FailableAgilityPyramidObstacle(5), IrreversibleDirection {
    override fun permanentSuccessLevel(): Int {
        return 75
    }

    override fun getFilterableStartMessage(success: Boolean): String {
        return "You jump the gap..."
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        cross(player, `object`, true)
    }

    override fun startFail(player: Player, `object`: WorldObject) {
        cross(player, `object`, false)
    }

    override fun endFail(player: Player, `object`: WorldObject?) {
        player.applyHit(Hit(8, HitType.REGULAR))
        player.sendFilteredMessage("... and miss your footing.")
    }

    private fun cross(player: Player, `object`: WorldObject, success: Boolean) {
        val walkDirection = Direction.getNPCDirection(player.getRoundedDirection())
        val destination: Location = player.location.transform(walkDirection, if (success) 3 else 1)
        player.faceObject(`object`)
        player.sendSound(SoundEffect(if (success) 2465 else 2463, 1, 20))
        player.setAnimation(if (success) jumpAnim else fallAnim)
        schedule(WorldTask { player.setForceMovement(ForceMovement(destination, 30, walkDirection.getDirection())) })
        schedule(WorldTask {
            if (!success) {
                player.setAnimation(Animation.STOP)
            }
            player.setLocation(
                if (success) destination else AgilityPyramidArea.Companion.getLowerTile(
                    destination.transform(
                        `object`.faceDirection,
                        2
                    )
                )
            )
        }, if (success) 1 else 7)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 22.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return if (success) 2 else 9
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(10859)
    }

    override fun getFailXp(`object`: WorldObject?): Double {
        return 0.0
    }

    override fun getReverseDirection(player: Player?, `object`: WorldObject): Direction {
        return `object`.faceDirection.getCounterClockwiseDirection(6)
    }

    override fun failOnReverse(): Boolean {
        return true
    }

    companion object {
        private val jumpAnim = Animation(3067)
        private val fallAnim = Animation(3068)
    }
}
