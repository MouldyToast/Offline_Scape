package com.near_reality.content.group_ironman.npc.actions

import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.world.entity.npc.actions.*

class D3ad1iFi5herNpcaction : NPCActionScript() {

    init {
        npcs(D3AD1I_F15HER)

        "Talk-to" {
            player.dialogue(npc) {
                player("How's the fishing going?")
                npc("It's going well, got a bunch of fish stocked up for the " +
                        "group. Now I just need someone to cook them.")
            }
        }
        "Trade" {
            player.dialogue(npc) {
                npc("You can only trade with people in your group.")
            }
        }
    }
}
