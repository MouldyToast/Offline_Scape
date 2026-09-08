package org.jesse.plugins.interfaces.teleports

import org.jesse.cache.interfaces.teleports.TeleportsList.teleports
import org.jesse.scripts.interfaces.InterfaceHandlerContext
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.player.teleports.TeleportsManager
import org.jesse.scripts.interfaces.InterfaceScript
import org.jesse.game.model.ui.InterfacePosition.*
import org.jesse.game.GameInterface
import org.jesse.game.GameInterface.*
import org.jesse.game.util.AccessMask
import org.jesse.game.util.AccessMask.*
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
