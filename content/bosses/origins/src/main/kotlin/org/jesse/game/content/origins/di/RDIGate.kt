package org.jesse.game.content.origins.di

import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Direction
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

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