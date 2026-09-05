package com.near_reality.game.content.dt2.npc.leviathan

import com.near_reality.game.content.dt2.npc.get
import com.near_reality.game.content.dt2.npc.leviathan.LeviathanConstants.ABYSSAL_PATHFINDER_SPAWN
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId

class LeviathanAbyssalPathfinder(
    position: Location,
    private val leviathan: Leviathan,
    private val instance: LeviathanInstance
) : NPC(
    NpcId.ABYSSAL_PATHFINDER, position, Direction.NORTH, 0
) {

    private lateinit var patrolPath: List<Location>
    private var stepCounter: Int = 0

    override fun spawn(): NPC {
        val result = super.spawn()
        result.animation = ABYSSAL_PATHFINDER_SPAWN

        patrolPath = reorderPathByNearest(position, instance)
        stepCounter = 0
        return result
    }

    override fun processNPC() {
        moveThroughPath()

        super.processNPC()

        leviathan.run {
            instance.players.forEach {
                it.checkAbyssalPathfinder(this@LeviathanAbyssalPathfinder)
            }
        }
    }

    private fun moveThroughPath() {
        val next = patrolPath[stepCounter % patrolPath.size]
        if (!location.equals(next)) {
            addWalkSteps(next.x, next.y, size, false)
        } else {
            stepCounter++
        }
    }

    companion object {
        val POSITIONS = listOf(
            Location(2086, 6377, 0), // Northeast (NE) - top-right
            Location(2086, 6366, 0), // Southeast (SE) - bottom-right
            Location(2074, 6366, 0), // Southwest (SW) - bottom-left
            Location(2074, 6377, 0)  // Northwest (NW) - top-left
        )

        fun reorderPathByNearest(startPosition: Location, instance: LeviathanInstance?) : List<Location> {
            val positions = POSITIONS.map { instance[it] }
            val nearestIndex = positions.indices.minByOrNull { positions[it].getDistance(startPosition) } ?: 0
            return positions.subList(nearestIndex, positions.size) + positions.subList(0, nearestIndex)
        }

        fun findNearestPosition(position: Location, instance: LeviathanInstance?) : Location {
            val positions = POSITIONS.map { instance[it] }
            return positions.minBy { it.getDistance(position) }
        }
    }
}
