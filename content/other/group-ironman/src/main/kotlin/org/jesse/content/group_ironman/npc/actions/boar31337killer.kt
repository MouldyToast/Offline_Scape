package org.jesse.content.group_ironman.npc.actions

import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.scripts.npc.actions.NPCActionScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.world.entity.npc.actions.*

class Boar31337killerNpcaction : NPCActionScript() {

    init {
        npcs(BOAR31337KILLER)

        "Talk-to"{
            player.dialogue(npc) {
                player("Why do you keep killing boars?")
                npc("Because they drop bones which I am going to use to " +
                        "train my prayer. The meat is also a nice addition.")
                player("Wouldn't it be best to go steal cakes for food?")
                npc("And then go to Wintertodt? I do that every time I " +
                        "make an Iron person account. About time I try something new.")
                player("Are you at least killing the boars on task?")
                npc("No, maybe I should go get a slayer task now you " +
                        "mention it. Thanks for the reminder.")
            }
        }

        "Trade" {
            player.dialogue(npc) {
                npc("You can only trade with people in your group.")
            }
        }
    }
}
