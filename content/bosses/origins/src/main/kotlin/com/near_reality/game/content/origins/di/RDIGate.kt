package com.near_reality.game.content.origins.di

import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

class RDIGate : ObjectAction{
    override fun handleObjectAction(
        player: Player,
        wo: WorldObject,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        if(option.equals("open", ignoreCase = true)){
            World.removeObject(wo)
            player.lock(1)
            player.addWalkSteps(getMovingDirection(wo.id, wo.location, player.location), 2, 2, false)
            WorldTasksManager.schedule(1) {
                World.spawnObject(wo)
            }
        }
    }

    override fun getObjects(): Array<Int> = arrayOf(12617, 11987, 12719, 12639)

    private fun getMovingDirection(obj: Int, pLoc: Location, objLoc: Location): Direction {
        if(pLoc.equals(objLoc.x, objLoc.y, objLoc.plane)){
            when(obj) {
                12639 -> return Direction.SOUTH
                12617 -> return Direction.WEST
                11987 -> return Direction.NORTH
                12719 -> return Direction.EAST
            }
        } else {
            val x = pLoc.x - objLoc.x
            val y = pLoc.y - objLoc.y

            if (x < 0 && y == 0)
                return Direction.WEST
            else if (x > 0 && y == 0)
                return Direction.EAST

            else if (x == 0 && y < 0)
                return Direction.SOUTH
            else if (x == 0 && y > 0)
                return Direction.NORTH
            else {
                System.err.println("Failed to determine direction")
                return Direction.NORTH
            }
        }
        return Direction.NORTH
    }

}