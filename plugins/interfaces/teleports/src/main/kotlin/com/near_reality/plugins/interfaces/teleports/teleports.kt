package com.near_reality.plugins.interfaces.teleports

import com.near_reality.cache.interfaces.teleports.TeleportsList.teleports
import com.near_reality.scripts.interfaces.InterfaceHandlerContext
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.player.teleports.TeleportsManager
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class TeleportsInterface : InterfaceScript() {

    private val categories = teleports.categories

    private val categoriesChildStep = 9

    val InterfaceHandlerContext.teleportsManager: TeleportsManager get() = player.teleportsManager

    init {
        /**
         * @author Jire
         */

        TELEPORTS {
            val selectCategory = "Select Category"(9) {
                teleportsManager.searchSelected = false
                if (slotID % categoriesChildStep == 0) {
                    val category = categories[slotID / categoriesChildStep]
                    teleportsManager.selectedCategory = category
                    player.awaitInputInt(TeleportInterfaceDialog())
                }
            }
            "Search"(25) {
                teleportsManager.searchSelected = !teleportsManager.searchSelected
                if (teleportsManager.searchSelected) {
                    player.packetDispatcher.sendClientScript(10527)
                }

                WorldTasksManager.schedule {
                    player.awaitInputInt(TeleportInterfaceDialog())
                }
            }

            opened {
                teleportsManager.updateFavorites()
                teleportsManager.searchSelected = false

                if (teleportsManager.selectedCategory == null) {
                    val firstCategory = categories.first()
                    teleportsManager.selectedCategory = firstCategory
                }

                sendInterface()

                val child = categories.lastIndex * categoriesChildStep
                selectCategory.sendComponentSettings(this, 0, child, CLICK_OP1)

                awaitInputInt(TeleportInterfaceDialog())
            }

            closed {
                temporaryAttributes.remove("interfaceInput")
                temporaryAttributes.remove("interfaceInputNoCloseOnButton")
                interfaceHandler.forceCloseInput()
            }
        }
    }
}
