@file:Suppress("unused")

package org.jesse.content.group_ironman.player

import com.google.common.eventbus.Subscribe
import org.jesse.game.net.packet.ChatChannelVar
import org.jesse.game.world.entity.player.GameCommands
import org.jesse.game.world.entity.player.MessageType
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.plugins.events.ServerLaunchEvent

object IronmanGroupCommands {

    @Subscribe
    @JvmStatic
    fun onServerLaunch(event: ServerLaunchEvent?) {
        register()
    }

    private fun register() {
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "message", "Send a test GIM chat message.") {p, args ->
            p.sendMessage("bla | bla", MessageType.CLAN_GIM_FORM_GROUP, p.username)
        }
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "varclan", "Set a varclan value. Args: varId value") { p, args ->
            val group = p.finalisedIronmanGroup
            if (group == null) {
                p.sendMessage("No group.")
                return@Command
            }

            val v = ChatChannelVar.find(args[0].toInt())
            if (v == null) {
                p.sendMessage("Invalid varclan id entered.")
                return@Command
            }

            group.channel.setVariableInt(v, args[1].toInt())
        }
    }
}
