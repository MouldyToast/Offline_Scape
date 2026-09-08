package org.jesse.plugins.area.osrs_home.npc

import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class MonkNpcaction : NPCActionScript() {

    init {
        npcs(MONK, MONK_1159, MONK_1171, MONK_2579, MONK_4068)

        "Talk-To" {
            player.dialogueManager.start(MonkDialogue(player, npc))
        }
    }
}
