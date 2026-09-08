package org.jesse.game.content.dt2.plugins.objects.duke

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

@Suppress("unused")
class FermentationVatCollectObject : ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        player.collectPoison(`object`)
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(47539)
    }

    private fun Player.collectPoison(loc: WorldObject) {
        if (!inventory.hasFreeSlots()) {
            dialogue {
                plain("Your inventory is too full to carry any more.")
            }
            return
        }

        val replacement = WorldObject(47536, loc.type, loc.rotation, loc.position)
        World.spawnObject(replacement)
        sendMessage("You collect some poison from the vat.")
        inventory.addItem(Item(ARDERMUSCA_POISON))
    }
}
