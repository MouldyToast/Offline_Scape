package org.jesse.game.content.skills.agility.varrockrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.WALL_14832
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class BalanceWall : AgilityCourseObstacle(VarrockRooftopCourse::class.java, 4) {
    override fun startSuccess(player: Player, `object`: WorldObject?) {
        player.setFaceLocation(JUMP_SPOT)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) {
                    player.setAnimation(RUN)
                    player.setForceMovement(ForceMovement(JUMP_SPOT, 30, ForceMovement.WEST))
                } else if (ticks == 1) player.setLocation(JUMP_SPOT)
                else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP)
                    player.setForceMovement(ForceMovement(player.location, 15, WALLS[0], 35, ForceMovement.WEST))
                } else if (ticks == 3) {
                    player.setAnimation(HANG)
                    player.setLocation(WALLS[0])
                } else if (ticks == 4) player.setForceMovement(
                    ForceMovement(
                        player.location,
                        10,
                        WALLS[1],
                        20,
                        ForceMovement.WEST
                    )
                )
                else if (ticks == 5) {
                    player.setAnimation(HANG)
                    player.setLocation(WALLS[1])
                } else if (ticks == 6) player.setForceMovement(
                    ForceMovement(
                        player.location,
                        10,
                        WALLS[2],
                        20,
                        ForceMovement.WEST
                    )
                )
                else if (ticks == 7) {
                    player.setAnimation(HANG)
                    player.setLocation(WALLS[2])
                } else if (ticks == 8) player.setForceMovement(
                    ForceMovement(
                        player.location,
                        10,
                        WALLS[3],
                        20,
                        ForceMovement.WEST
                    )
                )
                else if (ticks == 9) {
                    player.setAnimation(HANG)
                    player.setLocation(WALLS[3])
                } else if (ticks == 10) player.setForceMovement(
                    ForceMovement(
                        player.location,
                        10,
                        WALLS[4],
                        20,
                        ForceMovement.WEST
                    )
                )
                else if (ticks == 11) {
                    player.setAnimation(HANG)
                    player.setLocation(WALLS[4])
                } else if (ticks == 12) {
                    player.setAnimation(HANG_QUICK)
                    player.setForceMovement(ForceMovement(player.location, 15, WALLS[5], 35, ForceMovement.WEST))
                } else if (ticks == 13) {
                    player.setLocation(WALLS[5])
                } else if (ticks == 14) {
                    player.setAnimation(TURN)
                    player.getAppearance().setRenderAnimation(RENDER)
                    player.addWalkSteps(WALL_END.x, WALL_END.y, -1, false)
                }

                if (player.location.positionHash == WALL_END.positionHash || ticks >= 30) {
                    if (ticks < 30) {
                        player.getAppearance().resetRenderAnimation()
                        player.setFaceLocation(END)
                        player.setAnimation(Animation.JUMP)
                        player.setForceMovement(ForceMovement(player.location, 15, END, 35, ForceMovement.EAST))
                        ticks = 30
                    } else {
                        player.setLocation(END)
                        MarkOfGrace.spawn(player, VarrockRooftopCourse.Companion.MARK_LOCATIONS, 40, 30)
                        stop()
                    }
                }

                ticks++
            }
        }, 0, 0)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 25.0
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 30
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 17
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(WALL_14832)
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START
    }

    companion object {
        private val RUN = Animation(1995)
        private val HANG = Animation(1122)
        private val HANG_QUICK = Animation(1124)
        private val TURN = Animation(753)
        private val RENDER: RenderAnimation = RenderAnimation(757, 757, 756, 756, 756, 756, -1)

        private val START = Location(3194, 3416, 1)
        private val JUMP_SPOT = Location(3193, 3416, 1)
        private val WALL_END = Location(3190, 3407, 1)
        private val END = Location(3192, 3406, 3)

        private val WALLS = arrayOf<Location?>(
            Location(3190, 3414, 1),
            Location(3190, 3413, 1),
            Location(3190, 3412, 1),
            Location(3190, 3411, 1),
            Location(3190, 3410, 1),

            Location(3190, 3409, 1),  // special tile, turn around
        )
    }
}
