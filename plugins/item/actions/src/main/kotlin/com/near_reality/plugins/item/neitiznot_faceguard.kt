package com.near_reality.plugins.item

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

class NeitiznotFaceguardItemaction : ItemActionScript() {

    init {
        items(NEITIZNOT_FACEGUARD)

        "Dismantle" {
            player.dialogueManager.start(NeitiznotFaceguardDismantleDialogue(player))
        }
    }
}
