package com.near_reality.game.content.elven.npc

import com.near_reality.game.content.elven.npc.dialogue.AmrodDialogue
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.npc.actions.*

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
