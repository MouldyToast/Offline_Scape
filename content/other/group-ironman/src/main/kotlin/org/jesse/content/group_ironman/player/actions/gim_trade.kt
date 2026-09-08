package org.jesse.content.group_ironman.player.actions

import org.jesse.content.group_ironman.player.finalisedIronmanGroup
import org.jesse.content.group_ironman.player.ironmanGroupType
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.scripts.player.actions.PlayerActionScript

class GimTradePlayeraction : PlayerActionScript() {

    init {
        "Trade with" {
            if (player.hasPrivilege(PlayerPrivilege.ADMINISTRATOR) || other.hasPrivilege(PlayerPrivilege.ADMINISTRATOR))
                false
            else {
                val gimType = player.ironmanGroupType
                if (gimType != null) {
                    val group = player.finalisedIronmanGroup
                    if (group != null) {
                        if (group.isMember(other)) {
                            if (group.isMemberAndInGracePeriod(player))
                                false
                            else {
                                player.sendMessage("You cannot trade with other group members any longer.")
                                true
                            }
                        } else {
                            player.sendMessage("You cannot trade with ${other.name} because they are not a member of your group.")
                            true
                        }
                    } else {
                        player.sendMessage("You cannot trade because you are not in a group.")
                        true
                    }
                } else if (other.ironmanGroupType != null) {
                    player.sendMessage("You cannot trade with ${other.name} because they are a group ironman.")
                    true
                } else
                    false
            }
        }
    }
}
