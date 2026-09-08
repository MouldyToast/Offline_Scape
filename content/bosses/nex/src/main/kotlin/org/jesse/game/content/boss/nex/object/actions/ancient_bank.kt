package org.jesse.game.content.boss.nex.`object`.actions

import org.jesse.game.GameInterface
import org.jesse.game.content.ItemRetrievalService
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.scripts.`object`.actions.ObjectActionScript
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.*

class AncientBankObjectaction : ObjectActionScript() {

    init {
        CHEST_42854 {
            when(option) {
                "Claim" -> {
                    val service = player.retrievalService
                    if (service.type != ItemRetrievalService.RetrievalServiceType.ANCIENT_PRISON || service.container.size == 0) {
                        player.dialogue {
                            plain("The chest seems to be empty. If it did have any of your items, but<br><br>you died before collecting them, they'll now be lost.")
                        }
                    } else
                        GameInterface.ITEM_RETRIEVAL_SERVICE.open(player)
                }
            }
        }
    }
}
