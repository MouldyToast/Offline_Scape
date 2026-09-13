package org.jesse.game.content.boss.nex.`object`.actions

import org.jesse.game.content.commands.DeveloperCommands
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.cutscene.FadeScreen
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.scripts.`object`.actions.ObjectActionScript
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.*

class FrozenDoorObjectaction : ObjectActionScript() {

    fun Int.handleFrozenDoor() = invoke {
        if (!DeveloperCommands.enabledNex) {
            player.dialogue { plain("Nex is currently disabled.") }
        } else {
            FadeScreen(player) {
                player.teleport(player.targetLocation())
                WorldTasksManager.scheduleOrExecute({
                    player.interfaceHandler.sendInterface(InterfacePosition.OVERLAY, 406)
                }, 1)
            }.fade(2)
        }
    }

    private val ancientLocation = Location(2856, 5227, 0)

    private val godwarsLocation = Location(2883, 5280, 2)

    fun Player.targetLocation() = if (ancientLocation.getDistance(location) > 10)
        ancientLocation
    else
        godwarsLocation

    init {
        FROZEN_DOOR.handleFrozenDoor()
        FROZEN_DOOR_42841.handleFrozenDoor()
        FROZEN_DOOR_42932.handleFrozenDoor()
        FROZEN_DOOR_42931.handleFrozenDoor()
    }
}