package com.near_reality.plugins.interfaces.death

import com.zenyte.game.content.gravestone.GravestoneExt.getItemValue
import com.zenyte.game.content.gravestones.gravestone
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class DeathsOfficeSacrificeInvInterface : InterfaceScript() {

    fun Player.selectItem(slot: Int) {
        val itemInSlot = inventory.container.get(slot)
        if (itemInSlot == null || !itemInSlot.definitions.isGrandExchange) {
            sendMessage("You cannot trade that item to Death.")
            sendSound(2277)
            packetDispatcher.sendComponentItem(DEATHS_OFFICE_SACRIFICE.id, 8, 6512, 400)
            varManager.sendVarInstant(262, -1)
            varManager.sendVarInstant(261, gravestone().coinsInCoffer.coerceIn(0..Int.MAX_VALUE.toLong()).toInt())
            varManager.sendVarInstant(263, 0)
            varManager.sendVarInstant(264, 0)
            return
        }
        varManager.sendVarInstant(262, slot)
        varManager.sendVarInstant(261, gravestone().coinsInCoffer.coerceIn(0..Int.MAX_VALUE.toLong()).toInt())
        val sellPrice = getItemValue(itemInSlot)
        packetDispatcher.sendComponentItem(DEATHS_OFFICE_SACRIFICE.id, 8, itemInSlot.id, 400)
        varManager.sendVarInstant(263, 1)
        varManager.sendVarInstant(264, (sellPrice * 1.05).toInt())
    }

    init {
        /**
         * @author Kris | 14/06/2022
         */
        DEATHS_OFFICE_SACRIFICE_INV {
            val itemLayer = "Item layer"(0) {
                player.selectItem(slotID)
            }
            opened {
                itemLayer.sendComponentSettings(this, 27, CLICK_OP1, CLICK_OP10)
                packetDispatcher.sendClientScript(
                    149,
                    id shl 16 or itemLayer.componentID,
                    ContainerType.INVENTORY.id,
                    4,
                    7,
                    0,
                    -1,
                    "Select",
                    "",
                    "",
                    "",
                    ""
                )
                sendInterface()
            }
        }
    }
}
