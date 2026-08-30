package com.near_reality.content.group_ironman.widget

import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.AccessMask.*
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
