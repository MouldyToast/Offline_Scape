package org.jesse.content.group_ironman.widget

import org.jesse.content.group_ironman.getUnlockedSpaces
import org.jesse.content.group_ironman.player.finalisedIronmanGroup
import org.jesse.game.model.ui.PaneType
import org.jesse.game.util.component
import org.jesse.scripts.interfaces.InterfaceScript
import org.jesse.game.model.ui.InterfacePosition.*
import org.jesse.game.GameInterface
import org.jesse.game.GameInterface.*
import org.jesse.game.util.AccessMask
import org.jesse.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class SettingsGimStorageInterface : InterfaceScript() {

    init {
        SETTINGS_GIM_STORAGE {
            "Close"(8) {
                if (player.interfaceHandler.isPresent(SETTINGS_GIM)) {
                    player.packetDispatcher.closeInterface(730 component 21)
                } else {
                    player.packetDispatcher.closeInterface(724 component 47)
                }
            }
            opened {
                val group = finalisedIronmanGroup?: return@opened
                varManager.sendVarInstant(261, group.tasksCompleted[0])
                varManager.sendVarInstant(262, group.tasksCompleted[1])
                varManager.sendVarInstant(263, group.getUnlockedSpaces())
                varManager.sendVarInstant(264, 200)
                varManager.sendVarInstant(265, 1)//how many weeks until storage unlocks
                if (interfaceHandler.isPresent(SETTINGS_GIM)) {
                    interfaceHandler.sendInterface(id, 21, PaneType.IRON_GROUP_SETTINGS, true)
                    packetDispatcher.sendClientScript(3127, 730 component 21, 730 component 1, 1)
                } else {
                    interfaceHandler.sendInterface(id, 47, PaneType.IRON_BANK, true)
                    packetDispatcher.sendClientScript(3127, 724 component 47, 724 component 1, 1)
                }
            }
        }
    }
}
