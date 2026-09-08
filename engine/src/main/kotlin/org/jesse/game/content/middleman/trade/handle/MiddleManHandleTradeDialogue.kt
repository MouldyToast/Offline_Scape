package org.jesse.game.content.middleman.trade.handle

import org.jesse.game.content.middleman.MiddleManManager
import org.jesse.game.content.middleman.middleManConfirmedTradeListView
import org.jesse.util.capitalize
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.plugins.dialogue.CountDialogue

/**
 * Represents a [CountDialogue] that opens up when a staff member handles a confirmed middle-man trade.
 *
 * @author Stan van der Bend
 */
class MiddleManHandleTradeDialogue : CountDialogue {

    override fun run(index: Int) = Unit

    override fun execute(player: Player, amount: Int) {

        val tradeIndex = amount.div(65536)
        val buttonIndex = amount.minus(tradeIndex * 65536)

        val trade = player.middleManConfirmedTradeListView.getOrNull(tradeIndex)

        if (trade == null) {
            player.dialogue { plain("No trade was found at tradeIndex $tradeIndex") }
            return
        }

        if (MiddleManManager.isHandled(trade)){
            player.dialogue { plain("This trade has already been handled by another staff member.") }
            return
        }

        val (canAccept, reasonNot) = MiddleManManager.canAcceptTrade(trade.accepter, trade.requester)

        if (!canAccept) {
            player.dialogue { plain("Both players must be $reasonNot in order to handle the trade") }
            return
        }

        val handleAction = when(buttonIndex) {
            1 -> MiddleManHandleTradeAction.Accept
            0 -> MiddleManHandleTradeAction.Decline
            else -> null
        }

        player.dialogue {
            if (handleAction == null)
                plain("Unexpected values, please send a screenshot to a developer! tradeIndex=$tradeIndex, buttonIndex=$buttonIndex")
            else {
                plain("You are about to $handleAction the trade between " +
                        "${Colour.RS_GREEN.wrap(trade.requesterUsername)} " +
                        "and ${Colour.RS_GREEN.wrap(trade.accepterUsername)}," +
                        Colour.RED.wrap("this is irreversible")
                )
                options("${handleAction.toString().capitalize()} the trade?") {
                    "Yes." { MiddleManManager.handleTrade(player, trade, handleAction) }
                    "No."  { /* closes the interface */ }
                }
            }
        }
    }
}
