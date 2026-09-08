package org.jesse.api.service.user

import org.jesse.api.service.vote.totalVoteCredits
import org.jesse.game.GameInterface
import org.jesse.game.model.ui.Interface
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.player.Player


/**
 * Updates the player info tab with their current member rank and total donated amount.
 */
internal fun Player.updateDonationInfoOnPlayerDetailsTab() {
    GameInterface.GAME_NOTICEBOARD.plugin.ifPresent { plugin: Interface ->
        packetDispatcher.run {
            sendComponentText(
                GameInterface.GAME_NOTICEBOARD,
                plugin.getComponent("Member Rank"),
                buildString {
                    append("Member: ")
                    append(memberCrown.crownTag)
                    append(Colour.WHITE.wrap(memberName))
                }
            )
            sendComponentText(
                GameInterface.GAME_NOTICEBOARD,
                plugin.getComponent("Total donated"),
                buildString {
                    append("Total donated: ")
                    append(Colour.WHITE.wrap("$${storeTotalSpent}"))
                }
            )
        }
    }
}

/**
 * Updates the player info tab with their current vote credits.
 */
fun Player.updateVoteStatisticOnPlayerDetailsTab() {
    GameInterface.GAME_NOTICEBOARD.plugin.ifPresent { plugin: Interface ->
        packetDispatcher.sendComponentText(
            GameInterface.GAME_NOTICEBOARD,
            plugin.getComponent("Vote credits"),
            buildString {
                append("Vote credits: ")
                append(Colour.WHITE.wrap(totalVoteCredits))
            }
        )
    }
}
