package com.near_reality.game.content.remnantpets

import com.near_reality.game.item.CustomNpcId.DRIFTER
import com.near_reality.game.item.CustomObjectId
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.GameToggles
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.`var`.VarCollection
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.CharacterLoop
import com.zenyte.plugins.dialogue.SkillDialogue
import kotlin.math.min

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.25.2025
 */
class PrimalComponentsBench : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if(!player.hasSpokenToDrifter) {
            if(DrifterNPC.instance != null) {
                val drifter = DrifterNPC.instance as DrifterNPC
                drifter.faceEntity(player)
                drifter.forceTalk = ForceTalk("Oi!")
                WorldTasksManager.schedule( {
                    player.dialogue {
                        npc(DRIFTER, "You think you can just come in here and<br>know what to do? I remember a time when<br>a boi named Glab thought 'ey knew what<br>to do, then they mucked it all up.")
                        npc(DRIFTER, "Whenever you're ready for a little chat, just<br>know I'll be waiting here for ya.")
                    }
                }, 1)
            }
            return
        }
        if(!GameToggles.ORIGINS_PRIMAL_WORKBENCH_ENABLED) {
            player.sendMessage("This has been disabled temporarily for game integrity.")
            player.sendMessage("Please check discord for updates.")
            return
        }
        if(option.equals("harvest components", ignoreCase = true)) {
            val components = player.getComponents()
            if(components.isNotEmpty()) {
                player.startPrimalBreakdown(components.toTypedArray())
            } else {
                player.dialogue { player("I don't have anything to use here.<br>Perhaps I should speak with Drifter again.") }
            }
            return
        }


    }

    override fun getObjects() = arrayOf(CustomObjectId.PRIMAL_WORKBENCH)

    private fun Player.getComponents() = this.inventory.container.itemsAsList.filter { PrimalComponentsValues.valueMap.containsKey(it.id) }.map { it }

    private fun Player.startPrimalBreakdown(primals: Array<Item>) {
        dialogueManager.start(object: SkillDialogue(this, "Which would you like to breakdown?", *primals) {
            override fun run(slotId: Int, amount: Int) {
                val key = primals[slotId]
                val amountToMake = min(amount, inventory.getAmountOf(key.id))
                dialogue {
                    player.packetDispatcher.sendComponentPosition(219, 1, -4, -4)
                    options("Are you sure you want to do this?") {
                        "Yes (destroy my item for components)" {
                            val range = PrimalComponentsValues.valueMap[key.id]
                            if(range == null) {
                                player.sendMessage("Cannot locate breakdown information. Please report.")
                            } else {
                                if(player.inventory.deleteItem(key.id, amountToMake).result == RequestResult.SUCCESS) {
                                    repeat(amountToMake) {
                                        val amt = range.random()
                                        player.inventory.addOrDrop(Item(ItemId.PRIMAL_COMPONENTS, amt))
                                        GameLogger.log {
                                            GameLogMessage.PrimalExchange(
                                                username = player.dbUsername,
                                                item = Item(key.id, 1),
                                                value = amt
                                            )
                                        }
                                    }
                                    player.sendMessage("You have harvested some primal components from your items.")

                                } else {
                                    player.sendMessage("Something went wrong when breaking down the items.")
                                }
                            }

                        }
                        "No"{}

                    }
                }
            }
        })
    }
}