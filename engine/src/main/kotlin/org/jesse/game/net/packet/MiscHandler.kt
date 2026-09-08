package org.jesse.game.net.packet

import org.jesse.tools.BotPrevention
import org.jesse.game.GameConstants
import org.jesse.game.GameInterface
import org.jesse.game.model.ui.PaneType
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.GameCommands
import org.jesse.game.world.entity.player.LogLevel
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.logger.NearRealityPrintStream
import org.jesse.plugins.Event
import org.jesse.plugins.PluginManager
import net.rsprot.protocol.game.incoming.events.EventAppletFocus
import net.rsprot.protocol.game.incoming.events.EventCameraPosition
import net.rsprot.protocol.game.incoming.events.EventKeyboard
import net.rsprot.protocol.game.incoming.events.EventMouseClick
import net.rsprot.protocol.game.incoming.events.EventMouseMove
import net.rsprot.protocol.game.incoming.events.EventMouseScroll
import net.rsprot.protocol.game.incoming.events.EventNativeMouseClick
import net.rsprot.protocol.game.incoming.events.EventNativeMouseMove
import net.rsprot.protocol.game.incoming.misc.client.Idle
import net.rsprot.protocol.game.incoming.misc.client.NoTimeout
import net.rsprot.protocol.game.incoming.misc.user.BugReport
import net.rsprot.protocol.game.incoming.misc.user.ClickWorldMap
import net.rsprot.protocol.game.incoming.misc.user.ClientCheat
import net.rsprot.protocol.game.incoming.misc.user.OculusLeave
import net.rsprot.protocol.game.incoming.misc.user.SendSnapshot
import net.rsprot.protocol.game.incoming.misc.user.Teleport

private const val ENABLE_IDLE_LOGOUTS = false


internal fun PacketConsumer.bugReport() {
    addListener<BugReport> {
        val instructions = it.instructions
        val description = it.description
        val type = it.type

        player.log(LogLevel.HIGH_PACKET, "Instructions: $instructions, description: $description, type: $type")
    }
}

data class PlayerReport(
    val source: Player,
    val type: Int,
    val name: String
) : Event

internal fun PacketConsumer.playerReport() {
    addListener<SendSnapshot> {
        val player = player
        val rule = it.ruleId
        val name = it.name
        val mute = it.mute

        if (rule == 69) {
            PluginManager.post(PlayerReport(player, rule, name))
        }

        player.log(LogLevel.HIGH_PACKET, "Name: $name, rule: $rule, mute: $mute")
    }
}

internal fun PacketConsumer.oculusLeave() {
    addListener<OculusLeave> {
        val player = player
        if (player.temporaryAttributes["tournament_spectating"] != null) {
            player.interfaceHandler.closeInterfaces()
            return@addListener
        }
        val pane: PaneType = player.interfaceHandler.pane
        val loc = player.temporaryAttributes["oculusStart"]
        player.interfaceHandler.sendPane(pane, pane)
        player.packetDispatcher.sendClientScript(2221, 1)
        GameInterface.EMOTE_TAB.open(player)
        GameInterface.MUSIC_TAB.open(player)
        player.interfaceHandler.openJournal()
        player.interfaceHandler.visible.remove(PaneType.ORB_OF_OCULUS.id shl 16)
        if (loc !is Location || !player.privilege.eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
            return@addListener
        }
        player.setLocation(loc)
    }
}


internal fun PacketConsumer.teleport() {
    addListener<Teleport> {
        val player = player
        if (!player.eligibleForShiftTeleportation()) {
            return@addListener
        }
        player.setLocation(Location(it.x, it.z, it.level))
    }
}

internal fun PacketConsumer.clickWorldMap() {
    addListener<ClickWorldMap> {
        val player = player
        if (!player.eligibleForShiftTeleportation()) {
            return@addListener
        }
        player.setLocation(Location(it.x, it.z, it.level))
    }
}

internal fun PacketConsumer.clientCheat() {
    addListener<ClientCheat> {
        val command = it.command
        val player = player

        player.log(LogLevel.HIGH_PACKET, "Command: $command")
        try {
            GameCommands.process(player, command)
        } catch (e: Exception) {
            player.packetDispatcher
                .sendGameMessage("Error processing command %s: %s (%s).", true, command, e.javaClass, e.message)
            e.printStackTrace(NearRealityPrintStream.getErrorStream())
        }
    }
}

internal fun PacketConsumer.noTimeout() {
    addListener<NoTimeout> {}
}

internal fun PacketConsumer.eventAppletFocus() {
    addListener<EventAppletFocus> {
        if (!it.inFocus) {
            return@addListener
        }

        player.temporaryAttributes.remove("User deemed inactive")
    }
}

internal fun PacketConsumer.eventCameraPosition() {
    addListener<EventCameraPosition> {
        val player = player
        player.temporaryAttributes.remove("User deemed inactive")

        player.temporaryAttributes[BotPrevention.CAMERA_X_ATTRIBUTE_KEY] = player.x
        player.temporaryAttributes[BotPrevention.CAMERA_Y_ATTRIBUTE_KEY] = player.y
    }
}

internal fun PacketConsumer.eventMouseScroll() {
    addListener<EventMouseScroll> {
        player.temporaryAttributes.remove("User deemed inactive")
    }
}

internal fun PacketConsumer.eventMouseMove() {
    addListener<EventMouseMove> {
        player.temporaryAttributes.remove("User deemed inactive")
    }
    addListener<EventNativeMouseMove> {
        player.temporaryAttributes.remove("User deemed inactive")
    }
}

internal fun PacketConsumer.eventMouseClick() {
    addListener<EventMouseClick> {
        player.incrementNumericTemporaryAttribute(BotPrevention.MOUSE_CLICKS_ATTRIBUTE_KEY, 1)
    }
    addListener<EventNativeMouseClick> {
        player.incrementNumericTemporaryAttribute(BotPrevention.MOUSE_CLICKS_ATTRIBUTE_KEY, 1)
    }
}

internal fun PacketConsumer.eventKeyboard() {
    addListener<EventKeyboard> {
        player.incrementNumericTemporaryAttribute(BotPrevention.MOUSE_CLICKS_ATTRIBUTE_KEY, 1)
    }
}

internal fun PacketConsumer.eventMouseIdle() {
    addListener<Idle> {
        val player = player

        player.temporaryAttributes["User deemed inactive"] = true

        if (!ENABLE_IDLE_LOGOUTS) {
            return@addListener
        }

        if (!player.privilege.inherits(PlayerPrivilege.SUPPORT) || GameConstants.WORLD_PROFILE.isDevelopment()) {
            return@addListener
        }

        if (player.isLocked || player.isUnderCombat || player.getNumericTemporaryAttribute(
                "staff timeout " + "disabled"
            ).toInt() == 1
        ) {
            return@addListener
        }
        player.logout(false)
    }
}
