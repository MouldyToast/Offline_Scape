package com.zenyte.plugins.itemonnpc

import com.near_reality.game.world.entity.player.*
import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.ItemOnNPCAction
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.npc.ids.*
import com.zenyte.game.world.entity.player.Player

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