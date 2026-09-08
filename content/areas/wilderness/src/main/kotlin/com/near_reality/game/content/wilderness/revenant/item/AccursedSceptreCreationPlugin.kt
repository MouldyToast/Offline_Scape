package com.near_reality.game.content.wilderness.revenant.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Andys1814
 */
@Suppress("unused")
class AccursedSceptreCreationPlugin : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        if (player.skills.getLevel(SkillConstants.CRAFTING) < 85) {
            player.sendMessage("You need 85 Crafting in order to combine these items.")
            return
        }

        val thammarons = if (from.id == SKULL_OF_VETION) to else from
        player.inventory.deleteItem(Item(SKULL_OF_VETION))
        player.inventory.deleteItem(thammarons)

        val accursed = Item(
            when (thammarons.id) {
                THAMMARONS_SCEPTRE_U -> ACCURSED_SCEPTRE_U_27662
                THAMMARONS_SCEPTRE_AU -> ACCURSED_SCEPTRE_AU_27676
                THAMMARONS_SCEPTRE -> ACCURSED_SCEPTRE_27665
                THAMMARONS_SCEPTRE_A -> ACCURSED_SCEPTRE_A_27679
                else -> {
                    player.sendDeveloperMessage("Invalid item id: ${thammarons.id}")
                    return
                }
            }
        )
        accursed.charges = thammarons.charges
        player.inventory.addItem(accursed)

        player.dialogue {
            item(
                accursed,
                "You combine the Thammaron's sceptre and the Skull of Vet'ion together to form the Accursed Sceptre."
            )
        }
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(THAMMARONS_SCEPTRE_U, SKULL_OF_VETION),
            ItemOnItemAction.ItemPair.of(THAMMARONS_SCEPTRE_AU, SKULL_OF_VETION),
            ItemOnItemAction.ItemPair.of(THAMMARONS_SCEPTRE, SKULL_OF_VETION),
            ItemOnItemAction.ItemPair.of(THAMMARONS_SCEPTRE_A, SKULL_OF_VETION)
        )
    }
}
