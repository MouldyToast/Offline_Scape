package com.near_reality.game.content.dt2.plugins.objects.duke

import com.near_reality.game.content.commands.DeveloperCommands
import com.near_reality.game.content.dt2.area.DukeSucellusInstance
import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

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
            val awakenersOrb = Item(ItemId.AWAKENERS_ORB, 1)
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