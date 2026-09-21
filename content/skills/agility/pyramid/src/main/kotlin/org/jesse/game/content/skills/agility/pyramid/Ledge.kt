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
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Ledge : FailableAgilityPyramidObstacle(2), IrreversibleDirection {
    override fun getFilterableStartMessage(success: Boolean): String {
        return "You put your foot on the ledge and try to edge across..."
    }

    override fun getFilterableEndMessage(success: Boolean): String {
        return if (success) "You skillfully edge across the gap." else "You slip and fall to the level below."
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
        val walkDirection: Direction = `object`.faceDirection.getCounterClockwiseDirection(6)
        val destination: Location = player.location.transform(walkDirection, 5)
        val sound = SoundEffect(2451, 1, 20, if (success) 5 else 2)
        player.setFaceLocation(destination)
        schedule(object : WorldTask {
            private var ticks = 0
            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(Animation(startAnimation.getId(), 10))
                    player.sendSound(sound)
                } else if (ticks == 1) {
                    player.getAppearance().setRenderAnimation(
                        RenderAnimation(
                            RenderAnimation.STAND,
                            slideAnimation.getId(),
                            RenderAnimation.RUN
                        )
                    )
                    player.setAnimation(Animation.STOP)
                    player.addWalkSteps(destination.x, destination.y, -1, false)
                }
                if (!success) {
                    val fallDestination: Location? = AgilityPyramidArea.Companion.getLowerTile(
                        player.location.transform(`object`.faceDirection, 2)
                    )
                    if (ticks == 3) {
                        player.stop(Player.StopType.WALK)
                        player.getAppearance().resetRenderAnimation()
                        player.setAnimation(fallAnimation)
                    }
                    if (ticks == 4) {
                        player.setForceMovement(ForceMovement(fallDestination, 30, walkDirection.getDirection()))
                    }
                    if (ticks == 5) {
                        player.setLocation(fallDestination)
                        stop()
                    }
                }
                if (ticks == 6 && success) {
                    player.getAppearance().resetRenderAnimation()
                    player.setAnimation(finishAnimation)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 52.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject): Location? {
        return CrossGap.Companion.getLocation(`object`)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return if (success) 7 else 6
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(10860, 10886, 10888)
    }

    override fun getFailXp(`object`: WorldObject?): Double {
        return 0.0
    }

    override fun getReverseDirection(player: Player?, `object`: WorldObject): Direction {
        return `object`.faceDirection.getCounterClockwiseDirection(6)
    }

    override fun failOnReverse(): Boolean {
        return false
    }

    companion object {
        private val startAnimation = Animation(753)
        private val slideAnimation = Animation(756)
        private val fallAnimation = Animation(3062)
        private val finishAnimation = Animation(759)
    }
}
