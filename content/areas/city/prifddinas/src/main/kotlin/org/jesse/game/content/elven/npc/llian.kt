package org.jesse.game.content.elven.npc

import org.jesse.game.content.elven.npc.dialogue.LLiannDialogue
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class LlianNpcaction : NPCActionScript() {

    init {
        npcs(LLIANN)

        "Talk-To" {
            player.dialogueManager.start(LLiannDialogue(player, npc))
        }

        "Trade" {
            player.openShop("Lliann's Wares")
        }
    }
}
