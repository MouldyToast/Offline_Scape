package org.jesse.game.content.gauntlet.interfaces.user

import org.jesse.scripts.interfaces.user.UserInterfaceScript
import org.jesse.game.model.ui.InterfacePosition.*
import org.jesse.game.GameInterface.*

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
