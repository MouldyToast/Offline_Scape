package org.jesse.plugins.area.osrs_home.npc

import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class BrotherAlthricNpcaction : NPCActionScript() {

    init {
        npcs(BROTHER_ALTHRIC)

        "Talk-To" {
            player.dialogue(npc) {
                player("Very nice rosebushes you have here.")
                npc("Yes, it has taken me many long hours in this garden to bring them to this state of near-perfection.")
            }
        }
    }
}
