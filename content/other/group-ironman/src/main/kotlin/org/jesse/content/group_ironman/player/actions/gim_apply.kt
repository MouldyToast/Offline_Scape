package org.jesse.content.group_ironman.player.actions

import org.jesse.content.group_ironman.player.finalisedIronmanGroup
import org.jesse.content.group_ironman.player.pendingIronmanGroup
import org.jesse.content.group_ironman.player.pendingIronmanGroupApplication
import org.jesse.game.world.entity.player.MessageType
import org.jesse.scripts.player.actions.PlayerActionScript

class GimApplyPlayeraction : PlayerActionScript() {

    init {
        "Apply" {
            if (player.name == other.pendingIronmanGroupApplication) {
                val otherGroup = other.finalisedIronmanGroup ?: other.pendingIronmanGroup
                if (otherGroup != null) {
                    player.sendMessage("${other.name} has already joined a group.")
                } else {
                    val myGroup = player.finalisedIronmanGroup ?: other.pendingIronmanGroup
                    if (myGroup == null)
                        player.sendMessage("You do not currently have a group.")
                    else if (myGroup.join(other)) {
                        player.sendMessage("You accept ${other.name}'s application.")
                        other.sendMessage("You join the group creation process")
                    }
                }
            } else {
                val group = other.pendingIronmanGroup
                if (group == null) {
                    player.sendMessage("${other.name} is not currently creating a group")
                } else {
                    player.sendMessage("Applying to create a group with ${other.name}.")
                    other.packetDispatcher.sendMessage("|${player.name} wishes to help create your group.", MessageType.CLAN_GIM_FORM_GROUP, player.username)
                }
            }
            true
        }
    }
}
