package org.jesse.game.content.elven.npc

import org.jesse.game.content.elven.npc.dialogue.ElganDialogue
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class ElganNpcaction : NPCActionScript() {

    init {
        npcs(ELGAN)

        "Talk-To" {
            player.dialogueManager.start(ElganDialogue(player, npc))
        }

        "Trade" {
            // TODO: open shop
        }
    }
}
