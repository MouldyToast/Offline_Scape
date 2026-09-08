package org.jesse.game.content.boss.nex.`object`.actions

import org.jesse.game.content.commands.DeveloperCommands
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.scripts.`object`.actions.ObjectActionScript
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.*

class AncientDoor1Objectaction : ObjectActionScript() {

    init {
        DOOR_42933 {
            if (!DeveloperCommands.enabledNex) {
                player.dialogue { plain("Nex is currently disabled.") }
            } else {
                obj.isLocked = true
                val destX = player.x + (if (player.x == 2861) +2 else -2)
                player.sendDeveloperMessage("${player.x}, ${obj.x} $destX")
                player.resetWalkSteps()
                player.addWalkSteps(destX, player.y, 2, false)
                val door = Door.handleGraphicalDoor(obj, null)
                WorldTasksManager.schedule({
                    Door.handleGraphicalDoor(door, obj)
                    obj.isLocked = false
                }, 1)
            }
        }

        provideRouteEvent {
            if (player.x <= 2861)
                TileEvent(player, TileStrategy(Location(2861, 5219)), it)
            else
                TileEvent(player, TileStrategy(Location(2863, 5219)), it)
        }
    }
}
