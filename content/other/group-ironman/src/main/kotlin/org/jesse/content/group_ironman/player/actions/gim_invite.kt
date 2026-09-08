package org.jesse.content.group_ironman.player.actions

import org.jesse.content.group_ironman.player.finalisedIronmanGroup
import org.jesse.content.group_ironman.player.pendingIronmanGroup
import org.jesse.game.GameInterface
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.scripts.player.actions.PlayerActionScript

class GimInvitePlayeraction : PlayerActionScript() {

    init {
        "Invite" {
            if (player.gameMode != other.gameMode) {
                player.sendMessage("${other.name}'s Hardcore status is different to yours. " +
                        "They can change their mode by collecting a different iron helmet from the crate near the GIM tutor.")
            } else {
                val group = player.finalisedIronmanGroup ?: player.pendingIronmanGroup
                if (group == null)
                    player.sendDeveloperMessage("Cannot invite players because you are not currently in a group")
                else if (!group.isLeader(player))
                    player.sendMessage("Only group leaders can invite players to their group.")
                else if (other.finalisedIronmanGroup != null || other.pendingIronmanGroup != null)
                    player.sendMessage("${other.name} is already creating a group.")
                else {
                    val name = player.name
                    val newGroup = group == player.pendingIronmanGroup
                    player.sendMessage("Inviting to ${if(newGroup) "create" else "join"} a group.")
                    other.dialogue {
                        plain(
                            "You have been invited to join $name's group. Once the group is " +
                                    "final you cannot change your group for a while, and your hardcore " +
                                    "status will be lost upon leaving the group."
                        )
                        options("Really accept invitation to group?") {
                            dialogueOption("Yes.", true) {
                                if (group.join(other)) {
                                    if (newGroup) {
                                        other.pendingIronmanGroup = group
                                        group.allMembers.forEach {
                                            it.ifOnline {
                                                GameInterface.FORM_GIM_TAB.open(this)
                                            }
                                        }
                                    }
                                }
                            }
                            dialogueOption("No.", true)
                        }
                    }
                }
            }
            true
        }
    }
}
