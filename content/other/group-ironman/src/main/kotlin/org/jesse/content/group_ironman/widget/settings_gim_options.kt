package org.jesse.content.group_ironman.widget

import org.jesse.scripts.interfaces.InterfaceScript
import org.jesse.game.model.ui.InterfacePosition.*
import org.jesse.game.GameInterface
import org.jesse.game.GameInterface.*
import org.jesse.game.util.AccessMask
import org.jesse.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class SettingsGimOptionsInterface : InterfaceScript() {

    init {
        SETTINGS_GIM_OPTIONS {
            "Back"(5) {
                SETTINGS_GIM.open(player)
            }
        }
    }
}
