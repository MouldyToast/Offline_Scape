package com.near_reality.game.content.elven.npc

import com.near_reality.game.content.elven.npc.dialogue.PrifddinasRandomDialogue
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.npc.actions.*

class PrifddinasNpcaction : NPCActionScript() {

    init {
        npcs(
            FINDUILAS, MITHRELLAS, ENERDHIL, TATIE, IMINYE, CURUFIN,
            GELMIR, MAHTAN, FINGOLFIN, INDIS, ANAIRE, CELEBRIAN, OROPHER,
            MIRIEL, IDRIL
        )

        "Talk-To" {
            player.dialogueManager.start(PrifddinasRandomDialogue(player, npc))
        }
    }
}
