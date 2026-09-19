/**
 *
 */
package org.jesse.game.content.skills.agility.faladorrooftop

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.jesse.game.content.skills.agility.AgilityCourseObstacle
import org.jesse.game.content.skills.agility.MarkOfGrace
import org.jesse.game.obj.ids.*
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.RenderAnimation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

class Tightrope : AgilityCourseObstacle(FaladorRooftopCourse::class.java, 2) {
    override fun startSuccess(player: Player, `object`: WorldObject) {
        val objectID: Int = `object`.id
        if (ropes.get(objectID) == null) return

        val rope: TightropeInfo = ropes.get(objectID)
        val special = rope.id == TIGHTROPE_14905
        player.setRunSilent(true)
        player.faceObject(`object`)
        WorldTasksManager.schedule(object : WorldTask {
            private var ticks = 0

            override fun run() {
                if (ticks == 0) player.addWalkSteps(`object`.x, if (special) 3362 else `object`.y, -1, false)
                else if (ticks == 1) {
                    player.addWalkSteps(rope.finish!!.x, rope.finish.y, -1, false)
                    player.getAppearance().setRenderAnimation(WALK)
                }

                if (player.location.positionHash == rope.finish!!.positionHash) {
                    player.getAppearance().resetRenderAnimation()
                    MarkOfGrace.spawn(player, FaladorRooftopCourse.Companion.MARK_LOCATIONS, 50, 50)
                    player.setRunSilent(false)
                    stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(TIGHTROPE_14899, TIGHTROPE_14905, TIGHTROPE_14911)
    }

    override fun getLevel(`object`: WorldObject?): Int {
        return 50
    }

    private enum class TightropeInfo(val id: Int, val xp: Int, val delay: Int, start: Location, finish: Location) {
        FIRST(TIGHTROPE_14899, 17, 10, Location(3039, 3343, 3), Location(3047, 3343, 3)),
        SECOND(TIGHTROPE_14905, 45, 9, Location(3035, 3362, 3), Location(3027, 3354, 3)),
        THIRD(TIGHTROPE_14911, 40, 7, Location(3027, 3353, 3), Location(3020, 3353, 3)),
        ;

        val start: Location?
        val finish: Location?

        init {
            this.start = start
            this.finish = finish
        }

        companion object {
            val values = entries.toTypedArray()
        }
    }

    override fun getDuration(success: Boolean, `object`: WorldObject): Int {
        val rope: TightropeInfo? = ropes.get(`object`.id)
        return if (rope == null) 8 else rope.delay
    }

    override fun getSuccessXp(`object`: WorldObject): Double {
        val rope: TightropeInfo? = ropes.get(`object`.id)
        return if (rope == null) 10.0 else rope.xp.toDouble()
    }

    override fun getRouteEvent(player: Player?, `object`: WorldObject): Location? {
        val rope: TightropeInfo? = ropes.get(`object`.id)
        return if (rope == null) `object` else rope.start
    }

    companion object {
        private val ropes
                : Int2ObjectMap<TightropeInfo> =
            Int2ObjectOpenHashMap<TightropeInfo>(TightropeInfo.Companion.values.size)
        private val WALK: RenderAnimation = RenderAnimation(
            763, 762, 762, 762, 762, 762, -1
        )

        init {
            for (entry in TightropeInfo.Companion.values) ropes.put(
                entry.id, entry
            )
        }
    }
}