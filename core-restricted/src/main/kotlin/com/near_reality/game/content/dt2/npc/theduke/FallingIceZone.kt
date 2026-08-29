package com.near_reality.game.content.dt2.npc.theduke

import com.near_reality.game.content.dt2.area.DukeSucellusInstance
import com.near_reality.game.content.dt2.npc.*
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-01-09
 */
class FallingIceZone(
    private val arena: DukeSucellusInstance,
    private val side: ExtremitySide
) {

    private fun isLeft(): Boolean =
        side == ExtremitySide.LEFT


    private fun getFallingIce(): Location {
        val candidates = if (isLeft()) LEFT_SIDE_POSITIONS else RIGHT_SIDE_POSITIONS
        return candidates.shuffled().mapNotNull { staticPosition ->
            val basePosition = arena[staticPosition]
            val position = basePosition.randomSafe(radius = 1) { candidate ->
                val staticPosition = arena[candidate]
                if (staticPosition in SAFE_SHADOW_POSITIONS) {
                    return@randomSafe false
                }

                return@randomSafe basePosition.hasLineOfSightTo(candidate)
            }

            if (position in masterIceList) null else position
        }.firstOrNull() ?: candidates.random()

    }

    private val masterIceList: MutableList<Location> = mutableListOf()

    private fun getFallingIcicle(): List<Location> =
        List(3) { getFallingIce() }

    fun executeFallingIce() {
        val masterIceList = getFallingIcicle().toList()
        masterIceList.forEach {
            fallingIceEvent(it, Utils.random(3, 5))
        }
    }

    private fun fallingIceEvent(ice: Location, delay: Int) {
        if (masterIceList.size > 6 || !arena.slumbering) {
            return
        }
        masterIceList.add(ice)

        val player = arena.player

        ice.spotanim(id = 1447, height = 20)
        WorldTasksManager.schedule(object : TickTask() {
            override fun run() {
                if (player.location == ice) {
                    player.stun(2)
                    player.graphics = Graphics(254, 0, 92)
                    player.applyHit(Hit(Utils.random(3, 18), HitType.DEFAULT))
                    player.sendMessage("<col=ff3045>You've been frozen in place!")
                }
                ice.spotanim(id = 538, height = 20)
                ice.playSound(id = 7170, delay = 0, radius = 2)
                masterIceList.remove(ice)

                fallingIceEvent(getFallingIce(), 1)
                stop()
            }
        }, delay + 2)
    }

    companion object {

        val LEFT_SIDE_POSITIONS = listOf(
            Location(3030, 6443, 0),
            Location(3030, 6442, 0),
            Location(3030, 6436, 0),
            Location(3030, 6450, 0),
            Location(3032, 6437, 0),
            Location(3032, 6452, 0),
            Location(3032, 6451, 0),
            Location(3032, 6439, 0),
            Location(3032, 6440, 0),
            Location(3032, 6443, 0),
            Location(3032, 6445, 0),
            Location(3031, 6435, 0),
            Location(3031, 6441, 0),
            Location(3031, 6450, 0),
            Location(3032, 6435, 0),
            Location(3032, 6443, 0),
            Location(3032, 6448, 0)
        )

        val RIGHT_SIDE_POSITIONS = listOf(
            Location(3047, 6440, 0),
            Location(3048, 6441, 0),
            Location(3046, 6445, 0),
            Location(3047, 6451, 0),
            Location(3046, 6439, 0),
            Location(3046, 6437, 0),
            Location(3047, 6436, 0),
            Location(3047, 6444, 0),
            Location(3048, 6437, 0),
            Location(3046, 6436, 0),
            Location(3047, 6439, 0),
            Location(3046, 6445, 0),
            Location(3047, 6448, 0),
            Location(3048, 6444, 0),
            Location(3047, 6437, 0),
            Location(3046, 6439, 0),
            Location(3046, 6440, 0),
            Location(3048, 6440, 0),
            Location(3047, 6449, 0)
        )
    }

    val SAFE_SHADOW_POSITIONS = setOf(
        Location(3030, 6452, 0),
        Location(3031, 6452, 0),
        Location(3048, 6452, 0),
        Location(3047, 6452, 0)
    )
}