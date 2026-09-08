package org.jesse.plugins.item

import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

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
