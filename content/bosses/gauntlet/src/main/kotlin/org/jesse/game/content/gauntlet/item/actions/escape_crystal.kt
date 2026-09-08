package org.jesse.game.content.gauntlet.item.actions

import org.jesse.game.content.gauntlet.gauntlet
import org.jesse.game.content.gauntlet.rewards.GauntletRewardType
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class EscapeCrystalItemaction : ItemActionScript() {

    init {
        items(ESCAPE_CRYSTAL, CORRUPTED_ESCAPE_CRYSTAL)

        "Activate" {
            player.gauntlet
                ?.end(GauntletRewardType.NONE, true, false)
                ?:run { player.sendDeveloperMessage("Gauntlet attribute is missing.") }
        }
    }
}
