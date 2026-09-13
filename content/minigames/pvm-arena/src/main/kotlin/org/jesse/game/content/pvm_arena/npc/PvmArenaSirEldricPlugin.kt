package org.jesse.game.content.pvm_arena.npc

import org.jesse.game.content.pvm_arena.PvmArenaManager
import org.jesse.game.content.pvm_arena.PvmArenaTeam
import org.jesse.game.world.entity.player.pvmArenaPoints
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.actions.NPCPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import kotlin.time.Duration.Companion.milliseconds

/**
 * Handles the PvM Arena Ghost NPC.
 *
 * @author Stan van der Bend
 */
@Suppress("unused", "SpellCheckingInspection")
class PvmArenaSirEldricPlugin : NPCPlugin() {

    override fun handle() {
        bind("Talk-To") { player, npc ->
            player.dialogue(npc) {
                if (player.variables.pvmArenaBoosterTick > 0) {
                    npc(
                        "Ghost",
                        "Ah, a blessed soul approaches!<br>" +
                                "There are ${Colour.RS_PURPLE.wrap((player.variables.pvmArenaBoosterTick * 600).milliseconds.inWholeMinutes.toString())} minutes of my combat boost left for thee."
                    )
                } else {
                    npc(
                        "Ghost",
                        "Ah, a brave soul approaches!<br>" +
                                "Welcome, traveler, to the remnants of the once-great arena where knights and warriors tested their mettle against fearsome foes.<br>"
                    )
                }
                openOptionsMenu(player, npc)
            }
        }
    }

    private fun Dialogue.openOptionsMenu(player: Player, npc: NPC) {
        options {
            "Join the arena." {
                player.dialogue(npc) {
                    options {
                        "Blue team." { PvmArenaManager.tryJoinTeam(player, PvmArenaTeam.Blue) }
                        "Red team." { PvmArenaManager.tryJoinTeam(player, PvmArenaTeam.Red) }
                        "Nevermind." { }
                    }
                }
            }
            "What is this place?" {
                player.dialogue(npc) {
                    npc(
                        "Ghost",
                        "This hallowed ground, now known as the PvM Arena,<br>" +
                                "is where teams of courageous adventurers clash with a series of formidable bosses in a trial of strength and strategy."
                    )
                    npc(
                        "Ghost",
                        "Each team, armed with their own gear and supplies,<br>" +
                                "will face the same foes in sequence.<br>" +
                                "Coordination and valor are key, for the first team to slay all the monstrosities will be declared the victors."
                    )
                    npc(
                        "Ghost",
                        "Triumph brings not only honor but also Arena Points,<br>" +
                                "which can be exchanged for valuable rewards and a potent blessing, a damage boost against monsters for a short time."
                    )
                    openOptionsMenu(player, npc)
                }
            }
            "What can I do with PVM Arena Points?" {
                player.dialogue(npc) {
                    npc(
                        "Ghost",
                        "The spirits of vanquished foes yield Arena Points, a token of their strength.<br>" +
                                "Through me, you can redeem these points for powerful artifacts and supplies that aid in your quests.<br>"

                    )
                    npc(
                        "Ghost",
                        "The rewards are many, each designed to prepare you for even greater challenges."
                    )
                    openOptionsMenu(player, npc)
                }
            }
            "How do I play?" {
                player.dialogue(npc) {
                    npc(
                        "Ghost",
                        "To enter the fray of the PvM Arena,<br>" +
                                "you must choose your allegiance - simply tell me which team you wish to join."
                    )
                    npc(
                        "Ghost",
                                "I shall send you to an area designated for your team. " +
                            "Prepare well, for once entered, the battle begins posthaste."
                    )
                    npc(
                        "Ghost",
                        "Gather your allies, arm yourselves, and speak to me when you are ready to test your valor."
                    )
                    openOptionsMenu(player, npc)
                }
            }
            "Browse shop" {
                player.openShop("PVM Arena Shop")
                player.sendMessage("You currently have ${Colour.RS_RED.wrap(player.pvmArenaPoints.toString())} PvM Arena Points.")
            }
            "Who are you?" {
                player.dialogue(npc) {
                    npc(
                        "Ghost",
                        "I am Sir Eldric the Bound,<br>" +
                                "once a knight of great renown and now an eternal guardian of this spectral arena."

                    )
                    npc(
                        "Ghost",
                        "In life, I defended the realm's most sacred treasures against dark forces, " +
                                "a quest that ultimately led to my current fate.<br>"
                    )
                    npc(
                        "Ghost",
                        "Bound by an ancient curse during the final battle against the sorcerer Malverath,<br>" +
                                "my spirit was tethered to these ruins."
                    )
                    npc(
                        "Ghost",
                        "Now, I serve as a guide and overseer to those who seek to test their skills in the hallowed trials<br>" +
                                "of the PvM Arena."
                    )
                    openOptionsMenu(player, npc)
                }
            }
        }
    }

    override fun getNPCs(): IntArray =
        intArrayOf(GHOST_3516)
}
