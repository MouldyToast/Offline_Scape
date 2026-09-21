package org.jesse.game.content.skills.agility.pyramid

import org.jesse.game.content.skills.agility.IrreversibleObject
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Plank : FailableAgilityPyramidObstacle(3), IrreversibleObject {
    override fun getFilterableStartMessage(success: Boolean): String {
        return "You walk carefully across the slippery plank..."
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        cross(player, `object`, true)
    }

    override fun startFail(player: Player, `object`: WorldObject) {
        cross(player, `object`, false)
    }

    override fun endFail(player: Player, `object`: WorldObject?) {
        player.applyHit(Hit(10, HitType.REGULAR))
    }

    private fun cross(player: Player, `object`: WorldObject, success: Boolean) {
        val walkDirection: Direction = `object`.faceDirection.getCounterClockwiseDirection(4)
        val destination: Location = player.location.transform(walkDirection, 5)
        val sound = SoundEffect(2470, 1, 10, if (success) 4 else 2)
        val reverse = checkForReverse(player, `object`)
        player.setFaceLocation(destination)
        schedule(object : WorldTask {
            private var ticks = 0
            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(Animation(standAnim.getId(), 10))
                    player.sendSound(sound)
                } else if (ticks == 1) {
                    player.getAppearance().setRenderAnimation(
                        RenderAnimation(
                            RenderAnimation.STAND,
                            walkAnim.getId(),
                            RenderAnimation.RUN
                        )
                    )
                    player.setAnimation(Animation.STOP)
                    player.addWalkSteps(destination.x, destination.y, -1, false)
                }
                if (!success) {
                    val fallDirection: Direction =
                        `object`.faceDirection.getCounterClockwiseDirection(if (reverse) 2 else 6)
                    val fallDestination: Location? =
                        player.location.transform(fallDirection, 2).transform(0, 0, -1)
                    if (ticks == 3) {
                        player.stop(Player.StopType.WALK)
                        player.getAppearance().resetRenderAnimation()
                        player.setAnimation(if (reverse) reverseFallAnim else fallAnim)
                    }
                    if (ticks == 4) {
                        player.setForceMovement(ForceMovement(fallDestination, 30, walkDirection.getDirection()))
                    }
                    if (ticks == 5) {
                        player.setLocation(fallDestination)
                        stop()
                    }
                }
                if (ticks == 6) {
                    player.getAppearance().resetRenderAnimation()
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 56.4
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject): Location {
        return Location(`object`)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return if (success) 7 else 6
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(10867, 10868)
    }

    override fun getFailXp(`object`: WorldObject?): Double {
        return 0.0
    }

    override fun getFailObjectIds(): IntArray = intArrayOf(10867)

    override fun failOnReverse(): Boolean {
        return true
    }

    companion object {
        private val walkAnim = Animation(762)
        private val standAnim = Animation(763)
        private val fallAnim = Animation(764)
        private val reverseFallAnim = Animation(3069)
    }
}
