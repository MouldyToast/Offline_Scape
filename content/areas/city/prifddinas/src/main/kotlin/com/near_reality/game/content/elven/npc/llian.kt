package com.near_reality.game.content.elven.npc

import com.near_reality.game.content.elven.npc.dialogue.LLiannDialogue
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.npc.actions.*

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
