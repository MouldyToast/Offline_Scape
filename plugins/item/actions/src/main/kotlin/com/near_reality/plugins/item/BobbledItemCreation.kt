package com.near_reality.plugins.item

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.BossDropItem
import com.zenyte.game.model.item.ItemOnItemAction.ItemPair
import com.zenyte.game.model.item.PairedItemOnItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.plugins.dialogue.ItemChat

class BobbledItemCreation : PairedItemOnItemPlugin {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val item = BossDropItem.getItemByMaterials(from, to)
        if (item == null) {
            player.sendMessage("Nothing interesting happens.")
            return
        }

        player.dialogueManager.start(object : Dialogue(player) {
            override fun buildDialogue() {
                item(item.item, "You're about to attach the Malevolent energy to this armour set.")
                options(
                    "Attach the energy to this set?",
                    DialogueOption("Yes.") {
                        player.inventory.deleteItemsIfContains(item.materials) {
                            player.collectionLog.add(item.item)
                            player.inventory.addOrDrop(item.item)
                            player.dialogueManager.start(ItemChat(player, item.item, "You successfully combine all the materials into a pet."))
                        }
                    },
                    DialogueOption("No.")
                )
            }
        })
    }

    override fun getMatchingPairs(): Array<ItemPair> = arrayOf(
        ItemPair.of(BossDropItem.AHRIM_THE_BOBBLED.materials[0], BossDropItem.AHRIM_THE_BOBBLED.materials[1]),
        ItemPair.of(BossDropItem.DHAROK_THE_BOBBLED.materials[0], BossDropItem.DHAROK_THE_BOBBLED.materials[1]),
        ItemPair.of(BossDropItem.GUTHAN_THE_BOBBLED.materials[0], BossDropItem.GUTHAN_THE_BOBBLED.materials[1]),
        ItemPair.of(BossDropItem.KARIL_THE_BOBBLED.materials[0], BossDropItem.KARIL_THE_BOBBLED.materials[1]),
        ItemPair.of(BossDropItem.TORAG_THE_BOBBLED.materials[0], BossDropItem.TORAG_THE_BOBBLED.materials[1]),
        ItemPair.of(BossDropItem.VERAC_THE_BOBBLED.materials[0], BossDropItem.VERAC_THE_BOBBLED.materials[1]),
    )
}