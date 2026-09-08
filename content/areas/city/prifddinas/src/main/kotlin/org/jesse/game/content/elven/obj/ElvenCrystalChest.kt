package org.jesse.game.content.elven.obj

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Analytics
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import mgi.utilities.StringFormatUtil

/**
 * Represents an [ObjectAction] plugin for the elven crystal key chest.
 *
 * @author Stan van der Bend (mostly taken from [CrystalChest])
 */
@Suppress("UNUSED")
class ElvenCrystalChest : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        chestObject: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        when(option) {
            "Open" -> {
                if (!tryHandle(player, CRYSTAL_KEY) && !tryHandle(player, ENHANCED_CRYSTAL_KEY))
                    player.sendMessage("This chest is securely locked shut.")
            }
            "Check" -> player.showTimesOpenedDialogue()
        }
    }

    private fun Player.showTimesOpenedDialogue() = dialogue {
        val times = player.variables.timesOpenedEnhancedCrystalChest

        plain("You have opened the Elven Crystal Chest " +
                "${StringFormatUtil.formatNumberUS(times)} time${if(times != 1) "s" else ""}.")
    }

    private fun tryHandle(player: Player, itemId: Int): Boolean {
        if (!player.inventory.containsItem(itemId, 1))
            return false
        if (itemId == ENHANCED_CRYSTAL_KEY)
            player.variables.timesOpenedEnhancedCrystalChest++
        player.animation = animation
        player.lock(2)
        player.inventory.deleteItem(itemId, 1)
        rollLoot(itemId, player)
        player.inventory.addOrDrop(Item(995, Utils.random(25_000, 100_000)))
        Analytics.flagInteraction(player, Analytics.InteractionType.ELVEN_CRYSTAL_CHEST)
        return true
    }

    private fun rollLoot(itemId: Int, player: Player) {
        if (itemId == CRYSTAL_KEY) {
            NewCrystalChestLoot.rollTable(player, false).forEach { player.inventory.addOrDrop(it) }
        } else {
            NewCrystalChestLoot.rollTable(player, true).forEach { player.inventory.addOrDrop(it) }
        }
    }

    override fun getObjects(): Array<Any> =
        arrayOf(ELVEN_CRYSTAL_CHEST_36582)

    private companion object {
        val animation = Animation(832)
    }
}
