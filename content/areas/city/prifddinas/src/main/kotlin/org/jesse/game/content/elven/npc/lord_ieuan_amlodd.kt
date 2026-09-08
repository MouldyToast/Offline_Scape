package org.jesse.game.content.elven.npc

import org.jesse.game.content.elven.npc.dialogue.LordIeuanAmloddDialogue
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class LordIeuanAmloddNpcaction : NPCActionScript() {

    init {
        npcs(LORD_IEUAN_AMLODD_9119)

        "Talk-To" {
            player.dialogueManager.start(LordIeuanAmloddDialogue(player, npc))
        }
    }
}
