package org.jesse.game.content.elven.npc.dialogue

import org.jesse.game.content.elven.npc.openSeedTradeWindow
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.npc.actions.option
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Represents the [Dialogue] for Caerwyn.
 */
class AmrodDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        player("Hello there.")
        npc("Adventurer.")
        player("What do you do?")
        npc("I'm a trader of sorts, but only to those who don't ask too many questions.")
        player("Interesting, what do you offer?")
        npc("I can give you crystal shards in return for crystal seeds.")
        options {
            "Yes." {
                openSeedTradeWindow(player)
            }
            "No." {
                player.dialogue(npc) {
                    player("I'll pass, maybe another time.")
                }
            }
        }
    }
}
