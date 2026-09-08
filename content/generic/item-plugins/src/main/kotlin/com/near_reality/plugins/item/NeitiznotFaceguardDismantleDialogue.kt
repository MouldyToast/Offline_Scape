package com.near_reality.plugins.item

import com.zenyte.game.item.ids.*
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Handles removal of basilik jaw from neitiznot faceguard.
 *
 * @author Stan van der Bend
 *
 * @param player the [Player] who is dismantling their faceguard.
 */
class NeitiznotFaceguardDismantleDialogue(player: Player) : Dialogue(player) {
    override fun buildDialogue() {
        options("Remove the jaw from the helmet?") {
            "Yes." {
                val inventory = player.inventory
                if (inventory.deleteItem(NEITIZNOT_FACEGUARD, 1).result == RequestResult.SUCCESS) {
                    inventory.addOrDrop(HELM_OF_NEITIZNOT)
                    inventory.addOrDrop(BASILISK_JAW)
                    player.dialogue {
                        doubleItem(
                            HELM_OF_NEITIZNOT,
                            BASILISK_JAW,
                            "You remove the Basilisk Jaw from the Neitiznot Faceguard."
                        )
                    }
                }
            }
            "No." {}
        }
    }
}
