package org.jesse.game.net.packet

import org.jesse.game.model.ui.cape_customizer.CapeCustomizerInterfacePlugin.Companion.onResumeString
import org.jesse.game.GameInterface
import org.jesse.game.content.skills.magic.Magic
import org.jesse.game.content.skills.magic.SpellDefinitions
import org.jesse.game.content.skills.magic.spells.ItemSpell
import org.jesse.game.item.Item
import org.jesse.game.model.item.ItemOnItemHandler
import org.jesse.game.model.ui.ButtonAction
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.model.ui.NewInterfaceHandler
import org.jesse.game.model.ui.SubMenuAction
import org.jesse.game.model.ui.SwitchPlugin
import org.jesse.game.model.ui.testinterfaces.DropViewerInterface
import org.jesse.plugins.ListenerType
import org.jesse.plugins.MethodicPluginHandler
import org.jesse.plugins.dialogue.CountDialogue
import org.jesse.plugins.dialogue.ItemDialogue
import org.jesse.plugins.dialogue.NameDialogue
import org.jesse.plugins.dialogue.StringDialogue
import org.jesse.plugins.handlers.InterfaceSwitchHandler
import mgi.utilities.StringFormatUtil
import net.rsprot.protocol.game.incoming.buttons.If1Button
import net.rsprot.protocol.game.incoming.buttons.If3Button
import net.rsprot.protocol.game.incoming.buttons.IfButtonD
import net.rsprot.protocol.game.incoming.buttons.IfButtonT
import net.rsprot.protocol.game.incoming.buttons.IfSubOp
import net.rsprot.protocol.game.incoming.misc.user.CloseModal
import net.rsprot.protocol.game.incoming.resumed.ResumePCountDialog
import net.rsprot.protocol.game.incoming.resumed.ResumePNameDialog
import net.rsprot.protocol.game.incoming.resumed.ResumePObjDialog
import net.rsprot.protocol.game.incoming.resumed.ResumePStringDialog
import net.rsprot.protocol.game.incoming.resumed.ResumePauseButton
import org.slf4j.LoggerFactory

private class IfHandler

private val log = LoggerFactory.getLogger(IfHandler::class.java)


internal fun PacketConsumer.if1() {
    addListener<If1Button> {
        val interfaceId = it.interfaceId
        val componentId = it.componentId
        ButtonAction.handleComponentAction(player, interfaceId, componentId, -1, -1, 1, 1)
    }
}

internal fun PacketConsumer.if3() {
    addListener<If3Button> {
        val interfaceId = it.interfaceId
        val componentId = it.componentId
        var slotId = it.sub
        var itemId = it.obj
        val option = it.op

        if (itemId == 65535) {
            itemId = -1
        }
        if (slotId == 65535) {
            slotId = -1
        }
        ButtonAction.handleComponentAction(player, interfaceId, componentId, slotId, itemId, option, 3)
    }
}

internal fun PacketConsumer.ifSubOp() {
    addListener<IfSubOp> {
        val interfaceId = it.interfaceId
        val componentId = it.componentId
        val slotId = it.sub
        val itemId = it.obj
        val option = it.op
        val subOption = it.subop

        SubMenuAction.handleSubMenuAction(player, slotId, itemId, option, subOption)
//        log.debug("IfSubOp: interfaceId=$interfaceId, component=$componentId, slot=$slotId, itemId=$itemId, option=$option, subOption=$subOption")
    }
}

internal fun PacketConsumer.ifButtonD() {
    addListener<IfButtonD> {
        val player = player
        val fromInterfaceId = it.selectedInterfaceId
        val toInterfaceId = it.targetInterfaceId
        val fromComponentId = it.selectedComponentId
        val toComponentId = it.targetComponentId
        val fromSlotId = it.selectedSub
        val toSlotId = it.targetSub

        /** Close all input dialogues when switching, to prevent potential dupes in vulnerable code. */
        /** Close all input dialogues when switching, to prevent potential dupes in vulnerable code.  */
        player.interfaceHandler.closeInput()
        player.interfaceHandler.closeInterface(InterfacePosition.DIALOGUE)
        if (fromInterfaceId == toInterfaceId) {
            val optionalGameInterface = GameInterface.get(fromInterfaceId)
            if (optionalGameInterface.isPresent) {
                val gameInterface = optionalGameInterface.get()
                val optionalPlugin = gameInterface.getPlugin()
                if (optionalPlugin.isPresent) {
                    val plugin = optionalPlugin.get()
                    if (plugin is SwitchPlugin) {
                        if (plugin.switchItem(
                                player,
                                fromComponentId,
                                toComponentId,
                                fromSlotId,
                                toSlotId
                            )
                        ) return@addListener
                    }
                }
            }
        }
        val plugin = InterfaceSwitchHandler.interfaces[fromInterfaceId]
        /** If a full-script plugin exists for the interface, execute it and prevent code from going further. */
        /** If a full-script plugin exists for the interface, execute it and prevent code from going further.  */
        if (plugin != null) {
            plugin.switchItem(
                player,
                fromInterfaceId,
                toInterfaceId,
                fromComponentId,
                toComponentId,
                fromSlotId,
                toSlotId
            )
            return@addListener
        }
        MethodicPluginHandler.invokePlugins(
            ListenerType.INTERFACE_SWITCH,
            player,
            fromInterfaceId,
            toInterfaceId,
            fromComponentId,
            toComponentId,
            fromSlotId,
            toSlotId
        )

    }
}

internal fun PacketConsumer.ifButtonT() {
    addListener<IfButtonT> {
        val player = player
        val fromInterfaceId = it.selectedInterfaceId
        val toInterfaceId = it.targetInterfaceId
        val fromComponentId = it.selectedComponentId
        val fromSlot = it.selectedSub
        val toSlot = it.targetSub

        /* Hard-code the couple exceptions for now. */
        if (fromInterfaceId == 218 && toInterfaceId == 149) {
            val item: Item = player.getInventory().getItem(toSlot) ?: return@addListener
            val spell = Magic.getSpell<ItemSpell>(
                player.getCombatDefinitions().getSpellbook(), SpellDefinitions.getSpellName(fromComponentId),
                ItemSpell::class.java
            ) ?: return@addListener
            spell.execute(player, item, toSlot)
        } else if (fromInterfaceId == 149 && toInterfaceId == 149) {
            val from = player.getInventory().getItem(fromSlot)
            val to = player.getInventory().getItem(toSlot)
            if (from == null || to == null || player.isLocked) {
                return@addListener
            }
            ItemOnItemHandler.handleItemOnItem(player, from, to, fromSlot, toSlot)
        }
    }
}


internal fun PacketConsumer.resumePauseButton() {
    addListener<ResumePauseButton> {
        val player = player
        val interfaceId = it.interfaceId
        val componentId = it.componentId
        val slotId = it.sub

        if (!player.interfaceHandler.isVisible(interfaceId)) {
            return@addListener
        }
        val plugin = NewInterfaceHandler.getInterface(interfaceId)
        if (plugin != null) {
            var opt = plugin.getComponentName(componentId, slotId)
            if (opt.isEmpty) {
                opt = plugin.getComponentName(componentId, -1)
            }
            log.debug("[" + plugin.javaClass.getSimpleName() + "] Dialogue: " + opt.orElse("Absent") + "(" + interfaceId + "::" + componentId + ") | Slot: " + slotId)
            plugin.click(player, componentId, slotId, -1, -1)
            return@addListener
        }
        val script = ButtonAction.interfaces[interfaceId]
        if (script == null) {
            log.debug("Unhandled Dialogue Interface: interfaceId=$interfaceId, component=$componentId, slot=$slotId")
            return@addListener
        }
        log.debug("Dialogue Interface: " + script.javaClass.getSimpleName() + ", interfaceId=" + interfaceId + ", component=" + componentId + ", slot=" + slotId)
        script.handleComponentClick(player, interfaceId, componentId, slotId, -1, -1, "")
    }
}

internal fun PacketConsumer.resumePCount() {
    addListener<ResumePCountDialog> {
        val player = player
        val value = it.count

        val input = player.temporaryAttributes["interfaceInput"]
        if (input is CountDialogue) {
            input.execute(player, value)
        }
    }
}

internal fun PacketConsumer.resumePName() {
    addListener<ResumePNameDialog> {
        val player = player
        var name = it.name
        name = StringFormatUtil.convertToNameFormat(name)
        if (name.isEmpty()) {
            return@addListener
        }
        val input = player.temporaryAttributes["interfaceInput"]
        if (input is NameDialogue) {
            input.execute(player, name)
        }
    }
}

internal fun PacketConsumer.resumePObj() {
    addListener<ResumePObjDialog> {
        val player = player
        val itemId = it.obj

        if (itemId == -1) {
            return@addListener
        }
        val input = player.temporaryAttributes["interfaceInput"]
        if (input is ItemDialogue) {
            input.execute(player, itemId)
        }
    }
}

internal fun PacketConsumer.resumePString() {
    addListener<ResumePStringDialog> {
        val player = player
        val string = it.string

        if (player.interfaceHandler.isPresent(GameInterface.DROP_VIEWER)) {
            DropViewerInterface.search(player, string)
            return@addListener
        }
        val input = player.temporaryAttributes["interfaceInput"]
        (input as? StringDialogue)?.execute(player, string)
        if (player.interfaceHandler.isPresent(GameInterface.CAPE_CUSTOMIZER)) {
            onResumeString(player, string)
            return@addListener
        }
    }
}

internal fun PacketConsumer.closeModal() {
    addListener<CloseModal> {
        player.interfaceHandler.closeInterfaces()
    }
}
