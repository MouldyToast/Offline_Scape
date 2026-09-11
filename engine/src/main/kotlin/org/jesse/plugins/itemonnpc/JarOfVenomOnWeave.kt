package org.jesse.plugins.itemonnpc

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnNPCAction
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue

class JarOfVenomOnWeave : ItemOnNPCAction {
    override fun handleItemOnNPCAction(player: Player?, item: Item?, slot: Int, npc: NPC?) {
        player ?: return; item ?: return; npc ?: return

        val shown = player.getBooleanAttribute("shown_jar_of_venom_to_weave")

        player.dialogueManager.start(object : Dialogue(player, npc) {
            override fun buildDialogue() {
                if (!shown) {
                    player("I found this Jar, I think it's full of venom.")
                    npc("Oh wow, can I see it?.")
                    plain("You show Weave the Jar of Venom.")
                    npc("That is really fascinating. Thank you for sharing that with me.")
                    npc("Next I'll need to find something a little more potent to progress my research...")
                    player("I'll keep my eyes open!").executeAction {
                        player.addAttribute("shown_jar_of_venom_to_weave", true)
                    }
                } else {
                    player("Did you want to see the Jar again?")
                    npc("No that's quite alright, I've nothing else to learn from that particular sample. I need to find something else to progress my research...")
                    player("No problem.")
                }
            }
        })
    }

    override fun getItems(): Array<Any> = arrayOf(JAR_OF_VENOM)
    override fun getObjects(): Array<Any> = arrayOf(WEAVE_13677)
}