package org.jesse.game.content.skills.agility.pyramid

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

class CrossGap : FailableAgilityPyramidObstacle(4) {
    override fun getFilterableStartMessage(success: Boolean): String {
        return "You put your foot on the ledge and try to edge across..."
    }

    override fun getFilterableEndMessage(success: Boolean): String {
        return "You skillfully edge across the gap"
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        cross(player, `object`, true)
    }

    override fun startFail(player: Player, `object`: WorldObject) {
        cross(player, `object`, false)
    }

    override fun endFail(player: Player, `object`: WorldObject?) {
        player.applyHit(Hit(8, HitType.REGULAR))
    }

    private fun cross(player: Player, `object`: WorldObject, success: Boolean) {
        val walkDirection: Direction = `object`.faceDirection.getCounterClockwiseDirection(6)
        val destination: Location = player.location.transform(walkDirection, 5)
        player.setFaceLocation(destination)
        schedule(object : WorldTask {
            private var ticks = 0
            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(Animation(startAnimation.getId(), 10))
                    player.sendSound(SoundEffect(2450, 1, 10))
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
                    if (ticks == 5) {
                        player.setForceMovement(ForceMovement(fallDestination, 30, walkDirection.getDirection()))
                    }
                    if (ticks == 6) {
                        player.setLocation(fallDestination)
                        stop()
                    }
                }
                if (ticks == 6 && success) {
                    player.getAppearance().resetRenderAnimation()
                    player.setAnimation(finishAnimation)
                    player.sendSound(SoundEffect(2455))
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

    override fun getRouteEvent(player: Player?, `object`: WorldObject): Location? {
        return getLocation(`object`)
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 7
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(10861, 10882, 10884)
    }

    override fun getFailXp(`object`: WorldObject?): Double {
        return 0.0
    }

    companion object {
        private val startAnimation = Animation(3057)
        private val slideAnimation = Animation(3060)
        private val fallAnimation = Animation(3056)
        private val finishAnimation = Animation(3058)

        fun getLocation(`object`: WorldObject): Location? {
            if (`object`.faceDirection == Direction.WEST) {
                return `object`.getPosition().transform(Direction.SOUTH_EAST)
            } else if (`object`.faceDirection == Direction.SOUTH) {
                return `object`.transform(2, 1, 0)
            } else if (`object`.faceDirection == Direction.NORTH) {
                return `object`.transform(Direction.WEST)
            } else {
                return `object`.transform(Direction.NORTH, 2)
            }
        }
    }
}
