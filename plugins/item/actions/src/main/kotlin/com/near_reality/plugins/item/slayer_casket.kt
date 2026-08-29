package com.near_reality.plugins.item

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

class SlayerCasketItemaction : ItemActionScript() {

    init {
        items(CASKET_7956)

        "Open" {
            with(player.inventory) {
                ifDeleteItem(Item(item.id, 1)) {
                    val (item, descriptor) = if (Utils.randomBoolean(30))
                        Item(CustomItemId.SUPERIOR_BELL, 1) to "a superior bell"
                    else if (Utils.randomBoolean(15))
                        Item(setOf(SLAYER_TASK_RESET_SCROLL, SLAYER_TASK_PICKER_SCROLL).random(), 1) to "a scroll"
                    else {
                        val coinAmount = (25_000..75_000).random()
                        Item(COINS_995, coinAmount) to "some coins"
                    }
                    addOrDrop(item)
                    player.dialogueManager.start(object : Dialogue(player) {
                        override fun buildDialogue() {
                            doubleItem(item, item, "You open the casket and find $descriptor!")
                        }
                    })
                }
            }
        }
    }
}
