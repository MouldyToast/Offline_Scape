package com.near_reality.game.content.elven.npc

import com.near_reality.game.content.elven.npc.dialogue.ConwennaDialogue
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.npc.actions.*

class ConwennaNpcaction : NPCActionScript() {

    init {
        npcs(9240)

        "Talk-To" {
            player.dialogueManager.start(ConwennaDialogue(player, npc))
        }
    }
}
