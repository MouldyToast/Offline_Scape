package org.jesse.game.content.middleman

import org.jesse.game.GameConstants
import org.jesse.game.GameInterface
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.GameCommands
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege

/**
 * Registers the `::mm` command staff can use to handle ongoing middle-man trades.
 *
 * @author Stan van der Bend
 */
object MiddleManCommands {

    /**
     * Creates and registers the `::mm` command.
     */
    fun register() {
        GameCommands.Command(PlayerPrivilege.TRUE_DEVELOPER, "mm") { player, args ->
            if (player.hasPrivilege(PlayerPrivilege.TRUE_DEVELOPER)) {
                if(!GameConstants.isOwner(player))
                    return@Command
                player.options {
                    "View Sessions" {
                        GameInterface.MIDDLE_MAN_MONITORING.open(player)
                    }
                    (if (MiddleManManager.enabled) "disable" else "enable") {
                        MiddleManManager.enabled = !MiddleManManager.enabled
                        player.dialogue {
                            plain("You ${if (MiddleManManager.enabled) "enabled" else "disabled"} the middle man system")
                        }
                    }
                }
            } else
                GameInterface.MIDDLE_MAN_MONITORING.open(player)
        }
        GameCommands.Command(PlayerPrivilege.SUPPORT, "mma", "Teleport to middle man area") { player, _ ->
            player.teleport(Location(3035, 7267, 0))
        }
    }
}
