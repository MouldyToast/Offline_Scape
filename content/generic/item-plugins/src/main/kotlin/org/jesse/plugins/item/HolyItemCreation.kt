package org.jesse.plugins.item

import org.jesse.game.item.Item
import org.jesse.game.model.item.BossDropItem
import org.jesse.game.model.item.ItemOnItemAction.ItemPair
import org.jesse.game.model.item.PairedItemOnItemPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.plugins.dialogue.ItemChat

class HolyItemCreation : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val item = BossDropItem.getItemByMaterials(from, to)
        if (item == null) {
            player.sendMessage("Nothing interesting happens.")
            return
        }

        player.dialogueManager.start(object : Dialogue(player) {
            override fun buildDialogue() {
                item(item.item, "You're about to attach an Angelic artifact to this weapon. You'll also need 2,500 Degraded essence.")
                options(
                    "Attach the kit to this item?",
                    DialogueOption("Yes.") {
                        player.inventory.deleteItemsIfContains(item.materials) {
                            player.inventory.addOrDrop(item.item)
                            player.dialogueManager.start(ItemChat(player, item.item, "You successfully combine all the materials into one."))
                        }
                    },
                    DialogueOption("No.")
                )
            }
        })
    }

    override fun getMatchingPairs(): Array<ItemPair> = arrayOf(
        ItemPair.of(BossDropItem.HOLY_GREAT_HAMMER.materials[0], BossDropItem.HOLY_GREAT_HAMMER.materials[1]),
        ItemPair.of(BossDropItem.HOLY_GREAT_LANCE.materials[0], BossDropItem.HOLY_GREAT_LANCE.materials[1]),
    )
}