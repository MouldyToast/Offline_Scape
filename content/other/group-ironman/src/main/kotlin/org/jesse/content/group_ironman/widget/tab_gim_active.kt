package org.jesse.content.group_ironman.widget

import org.jesse.content.group_ironman.player.*
import org.jesse.game.model.ui.chat_channel.chatChannelInterfaceType
import org.jesse.scripts.interfaces.InterfaceScript
import org.jesse.game.model.ui.InterfacePosition.*
import org.jesse.game.GameInterface
import org.jesse.game.GameInterface.*
import org.jesse.game.util.AccessMask
import org.jesse.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class TabGimActiveInterface : InterfaceScript() {

    init {
        ACTIVE_GIM_TAB {
            "Refresh"(2) {
                val group = player.finalisedIronmanGroup
                if (group != null) {
                    player.sendChatChannelSettingsPacket(group)
                    player.sendChatChannelPacket(group)
                }
            }
            "Inviting"(7) {
                val group = player.finalisedIronmanGroup
                if (group != null) {
                    if (group.leaderUsername == player.username) {
                        player.ironmanGroupInvitingMode = !player.ironmanGroupInvitingMode
                        if (!player.ironmanGroupInvitingMode) {
                            player.sendMessage("You've closed applications to your group.")
                        }
                        player.updateInviteButton()
                        player.trySetApplyOrInvitePlayerOption()
                    } else {
                        player.sendMessage("Only the group leader can do that.")
                    }
                }
            }
            "Settings"(8) {
                val group = player.finalisedIronmanGroup
                if (group != null) {
                    SETTINGS_GIM.open(player)
                }
            }
            opened {
                chatChannelInterfaceType.sendTabInterface(this, getInterface())
            }
        }
    }
}
