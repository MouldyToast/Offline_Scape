package org.jesse.plugins.itemonnpc

import org.jesse.game.world.entity.player.*
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnNPCAction
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.npc.ids.TUMEKENS_GUARDIAN
import org.jesse.game.npc.ids.TUMEKENS_DAMAGED_GUARDIAN
import org.jesse.game.npc.ids.ELIDINIS_DAMAGED_GUARDIAN
import org.jesse.game.npc.ids.ELIDINIS_GUARDIAN
import org.jesse.game.npc.ids.AKKHITO
import org.jesse.game.npc.ids.BABI
import org.jesse.game.npc.ids.KEPHRITI
import org.jesse.game.npc.ids.ZEBO
import org.jesse.game.world.entity.player.Player

/**
 * @author John J. Woloszyk / Kryeus
 */
@Suppress("unused")
class ItemOnTOAPetAction : ItemOnNPCAction {

    override fun handleItemOnNPCAction(player: Player, item: Item, slot: Int, npc: NPC) {
        when(item.id) {
            REMNANT_OF_BABA -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the ability to transform your pet into Babi!"))
                player.toaPetBabi = true
            }
            REMNANT_OF_AKKHA -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the ability to transform your pet into Akkhito!"))
                player.toaPetAkkhito = true
            }
            REMNANT_OF_KEPHRI -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the ability to transform your pet into Kephriti!"))
                player.toaPetKephriti = true
            }
            REMNANT_OF_ZEBAK -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the ability to transform your pet into Zebo!"))
                player.toaPetZebo = true
            }
            ANCIENT_REMNANT -> {
                player.sendMessage(Colour.RS_GREEN.wrap("You have unlocked the damaged transformations of both Guardian's!"))
                player.toaPetRemnant = true
            }
        }
        player.inventory.deleteItem(item)
    }

    override fun getItems() = arrayOf(REMNANT_OF_BABA, REMNANT_OF_AKKHA, REMNANT_OF_KEPHRI, REMNANT_OF_ZEBAK, ANCIENT_REMNANT)

    override fun getObjects() = arrayOf(
        TUMEKENS_GUARDIAN,
        TUMEKENS_DAMAGED_GUARDIAN,
        ELIDINIS_DAMAGED_GUARDIAN,
        ELIDINIS_GUARDIAN,
        AKKHITO,
        BABI,
        KEPHRITI,
        ZEBO
    )
}