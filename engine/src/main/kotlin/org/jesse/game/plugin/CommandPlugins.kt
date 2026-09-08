package org.jesse.game.plugin

import org.jesse.game.world.entity.player.GameCommands.Command
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege

fun seniorModeratorCommand(
    name: String,
    description: String = "The $name command.",
    action: (Player, Array<String>) -> Unit
) = Command(PlayerPrivilege.SENIOR_MODERATOR, name, description, action)

fun administratorCommand(
    name: String,
    description: String = "The $name command.",
    action: (Player, Array<String>) -> Unit
) = Command(PlayerPrivilege.ADMINISTRATOR, name, description, action)

fun developerCommand(
    name: String,
    description: String = "The $name command.",
    action: (Player, Array<String>) -> Unit
) = Command(PlayerPrivilege.DEVELOPER, name, description, action)
