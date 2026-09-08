package org.jesse.plugins.item

import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class NeitiznotFaceguardItemaction : ItemActionScript() {

    init {
        items(NEITIZNOT_FACEGUARD)

        "Dismantle" {
            player.dialogueManager.start(NeitiznotFaceguardDismantleDialogue(player))
        }
    }
}
