package org.jesse.game.content.elven.npc

import org.jesse.game.content.elven.npc.dialogue.NiaDialogue
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class NiaNpcaction : NPCActionScript() {

    init {
        npcs(NIA)

        "Talk-To" {
            player.dialogueManager.start(NiaDialogue(player, npc))
        }

        "Trade" {
            // TODO: open shop
        }
    }
}
