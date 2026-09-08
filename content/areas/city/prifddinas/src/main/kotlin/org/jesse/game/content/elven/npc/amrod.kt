package org.jesse.game.content.elven.npc

import org.jesse.game.content.elven.npc.dialogue.AmrodDialogue
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class AmrodNpcaction : NPCActionScript() {

    init {
        npcs(AMROD)

        "Talk-To" {
            player.dialogueManager.start(AmrodDialogue(player, npc))
        }

        "Trade" {
            openSeedTradeWindow(player)
        }
    }
}
