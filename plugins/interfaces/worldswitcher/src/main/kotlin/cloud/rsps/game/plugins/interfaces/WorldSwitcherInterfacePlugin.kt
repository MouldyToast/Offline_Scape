package cloud.rsps.game.plugins.interfaces

import cloud.rsps.worlds.WorldSwitchTarget
import com.near_reality.api.model.WorldType
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameConstants
import com.zenyte.game.GameInterface
import com.zenyte.game.util.AccessMask

/**
 * @author Jire
 */
class WorldSwitcherInterfacePlugin : InterfaceScript() {

    init {
        GameInterface.WORLD_SWITCHER {
            opened {
                sendInterface()

                packetDispatcher.sendComponentSettings(
                    gameInterface,
                    worldList.componentID,

                    0,
                    600,

                    AccessMask.CLICK_OP1,
                    AccessMask.CLICK_OP2,
                )
            }
        }
    }

    private val configureButton = "Configure Button"(4) {
        player.sendMessage("This feature is not yet available.")
    }

    private val closeButton = "Close Button"(5) {
        GameInterface.LOGOUT.open(player)
    }

    private val worldList = "World List"(18) {
        when (option) {
            1 -> {
                if (GameConstants.WORLD_PROFILE.number == slotID) {
                    player.sendMessage("You are already on this world.")
                } else {
                    val worldProfile = GameConstants.WORLD_CONFIG.worlds.values.firstOrNull { it.number == slotID }
                    if (worldProfile == null) {
                        player.sendMessage("That world does not exist.")
                    } else {
                        val settings = WorldType.toMask(worldProfile.types.toTypedArray())
                        val worldSwitchTarget = WorldSwitchTarget(worldProfile.number, worldProfile.host, settings)
                        player.clickedLogoutButton(worldSwitchTarget)
                    }
                }
            }

            else -> {
                player.sendMessage("This feature is not yet available.")
            }
        }
    }

    private val logoutDoor = "Logout Door"(25) {
        player.clickedLogoutButton(null)
    }

}
