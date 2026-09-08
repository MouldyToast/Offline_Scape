package org.jesse.game.content.dt2.plugins.objects.duke

import org.jesse.game.content.commands.DeveloperCommands
import org.jesse.game.content.dt2.area.DukeSucellusInstance
import org.jesse.game.content.dt2.npc.DT2BossDifficulty
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

@Suppress("unused")
class DukeEntranceObject : ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (optionId == 1) {
            val awakenersOrb = Item(AWAKENERS_ORB, 1)
            if (player.mapInstance == null) {
                if (!player.inventory.containsItem(awakenersOrb))
                    DukeSucellusInstance.createInstance(DT2BossDifficulty.NORMAL, player).constructRegion()
                else {
                    player.dialogue {
                        options("What difficulty would you like to challenge?") {
                            "Awakened" {
                                if (player.inventory.deleteItem(awakenersOrb).succeededAmount == 1)
                                    DukeSucellusInstance.createInstance(DT2BossDifficulty.AWAKENED, player).constructRegion()
                                else if (player.inventory.deleteItem(awakenersOrb).isFailure)
                                    player.dialogue{ plain("You do not have an Awakener's Orb to begin this challenge") }
                            }
                            "Regular" {
                                DukeSucellusInstance.createInstance(DT2BossDifficulty.NORMAL, player).constructRegion()
                            }
                        }
                    }
                }
            }
            else
                player.sendMessage("The gate remains firmly shut. Perhaps I can teleport out...")
        }
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(49138)
    }
}