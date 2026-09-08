package org.jesse.game.content.araxxor

import org.jesse.game.content.araxxor.rewards.Reward
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.actions.NPCPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class AraxxorCorpse: NPCPlugin() {

    override fun handle() {
        bind("Harvest") { player, corpse ->
            run {
                if (player.mapInstance !is AraxxorInstance) return@run
                if (!((player.mapInstance as AraxxorInstance).hasLootAvailable))
                    return@run
                else
                    (player.mapInstance as AraxxorInstance).hasLootAvailable = false
                player.lock(2)
                player.animation = Animation.GRAB

                schedule(2) {
                    val rewards = Reward().rollForItems(player)
                    if (rewards.isNotEmpty())
                        rewards.forEach { reward -> player.inventory.addOrDrop(reward) }
                    removeAraxxorCorpseAndResetRoom(player, corpse)
                    player.unlock()
                }
            }
        }

        bind("Destroy") { player, corpse ->
            run {
                if (player.mapInstance !is AraxxorInstance) return@run


                player.dialogue {
                    options("This will forfeit the potential loot that comes with harvesting?",
                        Dialogue.DialogueOption("Yes, I'm sure.") {
                            if (!((player.mapInstance as AraxxorInstance).hasLootAvailable))
                                return@DialogueOption
                            else
                                (player.mapInstance as AraxxorInstance).hasLootAvailable = false
                            player.lock(2)
                            player.animation = Animation.GRAB
                            schedule(2) {
                                Reward().rollForNid(player, 1/1500.0)
                                removeAraxxorCorpseAndResetRoom(player, corpse)
                                player.unlock()
                            }
                        },
                        Dialogue.DialogueOption("Nevermind.")
                    )
                }
            }
        }
    }

    private fun removeAraxxorCorpseAndResetRoom(player: Player, corpse: NPC) {
        // TODO: Animation
        // TODO: Sound
        schedule(2) {
            player.mapInstance ?: return@schedule
            corpse.remove()
        }
        schedule(15) {
            player.mapInstance ?: return@schedule
            val araxxorInstance = player.mapInstance as AraxxorInstance
            araxxorInstance.araxxor?.remove()
            araxxorInstance.araxxor?.resetInstance()
            araxxorInstance.spawnAraxxorAndEggs()
        }
    }

    override fun getNPCs(): IntArray = intArrayOf(ARAXXOR_CORPSE)
}