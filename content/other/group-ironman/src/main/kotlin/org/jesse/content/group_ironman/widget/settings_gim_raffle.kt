package org.jesse.content.group_ironman.widget

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

class SettingsGimRaffleInterface : InterfaceScript() {

    init {
        SETTINGS_GIM_RAFFLE {
            "Close"(14) {

            }
            "Join Raffle"(15) {

            }
            opened {
                interfaceHandler.sendInterface(id, 21, PaneType.IRON_GROUP_SETTINGS, true)
                packetDispatcher.sendClientScript(3651, 730 component 21, 730 component 1, 1)
            }
        }
    }
}
