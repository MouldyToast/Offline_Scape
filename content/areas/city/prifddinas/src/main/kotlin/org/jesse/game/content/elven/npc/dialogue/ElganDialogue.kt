package org.jesse.game.content.elven.npc.dialogue

import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Represents the [Dialogue] for Elgan.
 */
class ElganDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        val gender = player.appearance.gender
        npc("Welcome, adventurer. Are you in need of a staff?")
        options{
            "What do you have?" {
                player.dialogue {
                    player("What do you have?").executeAction {
                        // TODO: open shop
                    }
                }
            }
            "No thanks." {
                player.dialogue {
                    player("No thanks.")
                    npc("Don't be a stranger. Come on by any time you need anything.")
                }
            }
        }
    }
}
