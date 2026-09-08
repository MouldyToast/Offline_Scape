package org.jesse.game.content.elven.npc.dialogue

import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Represents the [Dialogue] for Caerwyn.
 */
class CaerwynDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        val gender = player.appearance.gender
        npc("Are you interested in buying or selling spice?")
        options{
            "Yes please." {
                player.dialogue {
                    player("Yes please.").executeAction {
                        // TODO: open shop
                    }
                }
            }
            "No thanks." {
                player.dialogue {
                    player("No thanks.")
                }
            }
        }
    }
}
