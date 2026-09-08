package org.jesse.game.content.tormented_demon.items

import org.jesse.game.content.seq
import org.jesse.game.item.Item
import org.jesse.game.model.item.ItemOnObjectAction
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.Skills
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.obj.ids.*
import org.jesse.game.item.ids.HAMMER
import org.jesse.game.world.`object`.WorldObject
import org.jesse.plugins.item.Arclight.MAX_CHARGES
import org.jesse.game.item.ids.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-17
 */
class Emberlight : ItemOnObjectAction {
    override fun handleItemOnObjectAction(player: Player?, item: Item?, slot: Int, anvil: WorldObject?) {
        player ?:return; item ?: return
        val arclight = player.inventory.getItemById(ARCLIGHT)
        val arclightCharges = arclight?.charges ?: 0
        val tormentedSynapse = player.inventory.getItemById(TORMENTED_SYNAPSE)
        val hammer = player.inventory.getItemById(HAMMER)
        val hasEnoughCharges = arclightCharges == MAX_CHARGES
        val chargesNeeded = MAX_CHARGES - arclightCharges

        if (arclight == null) {
            player.sendMessage("You need an Arclight to smith the Emberlight.")
            return
        }
        if (tormentedSynapse == null) {
            player.sendMessage("You need a Tormented Synapse to smith the Emberlight.")
            return
        }
        if (player.skills.getLevel(Skills.SMITHING) < 74) {
            player.sendMessage("You need a smithing level of 74 to smith the Emberlight.")
            return
        }
        if (hammer == null) {
            player.sendMessage("You need a Hammer to smith the Purging Staff.")
            return
        }

        if (!hasEnoughCharges) {
            player.dialogue {
                item(ARCLIGHT, "Your Arclight needs $chargesNeeded more charges to become Emberlight.")
            }
            return
        }
        if (player.inventory.deleteItem(arclight).result == RequestResult.SUCCESS &&
            player.inventory.deleteItem(tormentedSynapse).result == RequestResult.SUCCESS) {
            player seq Animation.SMITH.id
            player.inventory.addItem(Item(EMBERLIGHT, 1))
            player.skills.addXp(Skills.SMITHING, 730.0)
            player.dialogue {
                item(
                    EMBERLIGHT,
                    "The synapse writhes as you hammer it into the sword, flaring fire throughout it. Shards of crystal break off, but what remains hardens into a burning, obsidian-like material."
                )
            }
        }
    }

    override fun getItems(): Array<Any> =
        arrayOf(ARCLIGHT, TORMENTED_SYNAPSE)

    override fun getObjects(): Array<Any> =
        arrayOf("Anvil")
}