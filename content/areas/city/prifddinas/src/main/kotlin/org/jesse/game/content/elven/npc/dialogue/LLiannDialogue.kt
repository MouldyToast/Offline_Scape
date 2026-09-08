package org.jesse.game.content.elven.npc.dialogue

import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Represents the [Dialogue] for Lliann.
 */
class LLiannDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        val gender = player.appearance.gender
        npc("You look like a $gender that could use an overly expensive crown. " +
                "Finest clothes around! Want to take a look?")
        options{
            "Yes, please." {
                player.dialogue {
                    player("Yes, please.").executeAction { player.openShop("Lliann's Wares") }
                }
            }
            "No thanks." {
                player.dialogue {
                    player("No thanks.")
                    npc("Your loss, I suppose.")
                }
            }
        }
    }
}
