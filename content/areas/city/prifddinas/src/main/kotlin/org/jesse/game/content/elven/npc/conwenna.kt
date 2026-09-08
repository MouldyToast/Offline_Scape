package org.jesse.game.content.elven.npc

import org.jesse.game.content.elven.npc.dialogue.ConwennaDialogue
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class ConwennaNpcaction : NPCActionScript() {

    init {
        npcs(9240)

        "Talk-To" {
            player.dialogueManager.start(ConwennaDialogue(player, npc))
        }
    }
}
