package org.jesse.game.content.skills.agility.alkharidrooftop

import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.TROPICAL_TREE_14404
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class TropicalTree : AgilityCourseObstacle(AlKharidRooftopCourse::class.java, 5) {
    override fun getRouteEvent(player: Player?, `object`: WorldObject?): Location {
        return START_LOC
    }

    override fun startSuccess(player: Player, `object`: WorldObject) {
        player.faceObject(`object`)
        player.setForceMovement(ForceMovement(Location(player.location), 55, TREE_LOC, 90, ForceMovement.NORTH))
        WorldTasksManager.schedule(object : WorldTask {
            var ticks: Int = 0

            override fun run() {
                when (ticks++) {
                    1 -> player.setLocation(TREE_LOC)
                    2 -> {
                        player.setFaceLocation(FACE_LOC)
                        player.setAnimation(TREE_HANGING_ANIM)
                    }

                    3 -> {
                        player.setFaceLocation(TREE_LOC)
                        player.setAnimation(TREE_HANGING_2_ANIM)
                        player.setForceMovement(
                            ForceMovement(
                                Location(player.location),
                                34,
                                END_LOC,
                                52,
                                ForceMovement.NORTH
                            )
                        )
                    }

                    4 -> {
                        player.setAnimation(JUMP_ANIM)
                        player.setLocation(END_LOC)
                        MarkOfGrace.spawn(player, AlKharidRooftopCourse.MARK_LOCATIONS, 40, 20)
                        stop()
                    }
                }
            }
        }, 0, 1)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 20
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(TROPICAL_TREE_14404)
    }

    override fun getSuccessXp(`object`: WorldObject?): Double {
        return 10.0
    }

    override fun getDuration(success: Boolean, `object`: WorldObject?): Int {
        return 9
    }

    companion object {
        private val START_LOC = Location(3318, 3165, 1)
        private val END_LOC = Location(3317, 3174, 2)
        private val TREE_LOC = Location(3318, 3170, 1)
        private val FACE_LOC = Location(3320, 3170, 1)
        private val TREE_HANGING_ANIM = Animation(1122)
        private val TREE_HANGING_2_ANIM = Animation(1124)
        private val JUMP_ANIM = Animation(2588)
    }
}
