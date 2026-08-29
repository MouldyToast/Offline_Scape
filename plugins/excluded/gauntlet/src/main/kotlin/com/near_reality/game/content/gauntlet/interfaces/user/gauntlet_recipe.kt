package com.near_reality.game.content.gauntlet.interfaces.user

import com.near_reality.scripts.interfaces.user.UserInterfaceScript
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface.*

class GauntletRecipeUserinterface : UserInterfaceScript() {

    init {
        /**
         * @author Andys1814
         * @author Jire
         */
        640 {
            option["Close"] = {
                player.interfaceHandler.closeInterface(CENTRAL)
            }
        }
    }
}
