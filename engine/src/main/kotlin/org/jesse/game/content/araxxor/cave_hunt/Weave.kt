package org.jesse.game.content.araxxor.cave_hunt

import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.ForceTalk
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.actions.NPCPlugin
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.Dialogue.DialogueOption
import org.jesse.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-15
 */
class Weave: NPCPlugin() {
    override fun handle() {
        bind("Talk-to") { player, npc ->
            val caveHunt = player.mapInstance as? AraxyteCaveHunt
            if (caveHunt != null) {
                if (caveHunt.roomCompleted) {
                    player.dialogue { npc("Go.") }
                    return@bind
                }
                if (player.equipment.containsAnyOf(ARAXYTE_SLAYER_HELMET, ARAXYTE_SLAYER_HELMET_I)) {
                    caveHunt.roomCompleted = true
                    npc.forceTalk = ForceTalk("It's a step in the right direction, proceed.")
                } else {
                    npc.forceTalk = ForceTalk("Boots alone do not make an Araxyte.")
                }
                return@bind
            }
            val mmiiComplete = player.varManager.getBitValue(5027) >= 195
            val slayerMaster = if (mmiiComplete) "nephew, Steve" else "niece, Nieve"
            player.dialogueManager.start(object : Dialogue(player, npc) {
                override fun buildDialogue() {
                    npc("Greetings, stranger. How can I help you?")
                    options(
                        DialogueOption("What is this place?", key(10)),
                        DialogueOption("Discovered anything interesting?", key(20)),
                        DialogueOption("Goodbye.", key(30))
                    )
                    player(10, "What is this place?")
                    npc("It's a cave filled with araxytes, a rare form of arachnid. I've been here researching them for some time.")
                    npc("My $slayerMaster, is one of those Slayer Masters. You know, they tell people what to go kill. I also look after this cave for people on assignments like that.").executeAction(key(1))
                    player(20, "Discovered anything interesting?")
                    npc("Hmm... Not really.")
                    npc("However, the araxytes here are rather venomous... and when I say venomous, I mean venomous.")
                    player("Is that really noteworthy?")
                    npc("Well, I didn't think so by itself, but lots of other people seem to find it very interesting.")
                    npc("For example, there were some particularly pale wanderers doing their own research in here. They didn't bother me though.")
                    player("Hmm...").executeAction(key(1))
                    player(30, "Goodbye.")
                    npc("Bye bye.")
                }
            })
        }
    }

    override fun getNPCs(): IntArray = intArrayOf(WEAVE)
}