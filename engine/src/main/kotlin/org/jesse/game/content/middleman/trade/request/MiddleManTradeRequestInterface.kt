package org.jesse.game.content.middleman.trade.request

import org.jesse.game.content.middleman.*
import org.jesse.game.GameInterface
import org.jesse.game.model.ui.Interface
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.net.packet.PacketDispatcher
import org.jesse.game.util.component
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.utils.TextUtils
import mgi.types.config.items.ItemDefinitions
import java.util.*

/**
 * Represents the [Interface] that opens when a [Player] creates a [MiddleManTradeRequest].
 *
 * @author Stan van der Bend
 */
class MiddleManTradeRequestInterface : Interface() {

    override fun attach() {
        put(7, "Enter name")

        put(25, "Minus 1")
        put(26, "Plus 1")
        put(30, "Set quantity")

        put(15, "Confirm")
        put(36, "Cancel")
    }

    override fun open(player: Player) {
        player.interfaceHandler.sendInterface(InterfacePosition.CENTRAL, `interface`.id, false)
        player.packetDispatcher.apply {
            sendClientScript(10532, player.middleManStaffOptions.joinToString("|"))
            sendComponentText(`interface`, 11, player.middleManTargetName?:"")
            sendComponentText(`interface`, 17, ItemDefinitions.nameOf(player.middleManOfferItemId))
            sendClientScript(10589, player.middleManOfferItemId, player.middleManOfferAmount, 1602 component 22)
            updateOfferAmount(player.middleManOfferAmount)
        }
    }

    override fun close(player: Player, replacement: Optional<GameInterface>) {
        if (replacement.filter { it == GameInterface.MIDDLE_MAN_OFFER }.isEmpty)
            player.middleManController.cancelRequestOrTrade()
    }

    override fun build() {
        bind("Enter name") { player ->
            player.sendInputString("Enter name of player") {
                val nameToSearch = TextUtils.formatNameForProtocol(it)
                val optionalTarget = World.getPlayer(nameToSearch)
                player.middleManTargetName = optionalTarget.map(Player::getName).orElse("")
                player.packetDispatcher.sendComponentText(`interface`, 11, player.middleManTargetName)
                if (optionalTarget.isEmpty)
                    player.dialogue { plain("Did not find a player online by the name of `$it`") }
                player.middleManController.awaitStaffOptionPick()
            }
        }
        bind("Minus 1") { player ->
            player.packetDispatcher.updateOfferAmount(--player.middleManOfferAmount )
        }
        bind("Plus 1") { player ->
            player.packetDispatcher.updateOfferAmount(++player.middleManOfferAmount )
        }
        bind("Set quantity") { player ->
            player.awaitInputInt {
                player.middleManOfferAmount = it
                player.packetDispatcher.updateOfferAmount(player.middleManOfferAmount)
            }
        }
        bind("Confirm") { player -> player.middleManController.confirmRequestOrTrade() }
        bind("Cancel") { player -> player.middleManController.cancelRequestOrTrade() }
    }

    private fun PacketDispatcher.updateOfferAmount(amount: Int) {
        sendComponentText(`interface`, 18, amount)
        sendComponentText(`interface`, 30, amount)
    }

    override fun getInterface() = GameInterface.MIDDLE_MAN_REQUEST
}
