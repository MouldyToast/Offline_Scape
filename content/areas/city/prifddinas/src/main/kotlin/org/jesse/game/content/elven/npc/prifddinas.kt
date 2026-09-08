package org.jesse.game.content.elven.npc

import org.jesse.game.content.elven.npc.dialogue.PrifddinasRandomDialogue
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

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
