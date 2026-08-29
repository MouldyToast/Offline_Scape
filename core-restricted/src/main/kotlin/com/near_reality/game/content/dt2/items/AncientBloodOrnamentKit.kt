package com.near_reality.game.content.dt2.items

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import net.runelite.api.ItemID.TORVA_FULL_HELM

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-11
 */
class AncientBloodOrnamentKit : ItemOnItemAction{

    private val bloodRuneRequirement : Item =
        Item(BLOOD_RUNE, 20_000)

    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val kit = if (from.id == ANCIENT_BLOOD_ORNAMENT_KIT) from else to
        val pieceItem = if (from.id == ANCIENT_BLOOD_ORNAMENT_KIT) to else from
        if (!player.inventory.containsItem(bloodRuneRequirement)) {
            player.sendMessage("You don't have enough blood runes.")
            return
        }
        player.dialogue {
            doubleItem(pieceItem, kit,
                "Are you sure you want to use the Ancient blood ornament kit and 20,000 blood runes on your ${pieceItem.name} to create a ${pieceItem.converted()?.name}?")
            options("Are you sure you want to combine these items?") {
                "Yes." { player.convertBloodTorvaItem(pieceItem) }
                "No." {}
            }
        }
    }

    private fun Player.convertBloodTorvaItem(armourPiece: Item) {
        if (inventory.deleteItem(armourPiece).result == RequestResult.SUCCESS &&
            inventory.deleteItem(bloodRuneRequirement).result == RequestResult.SUCCESS) {
            lock()
            performThunderClouds()
            WorldTasksManager.schedule(4) {
                unlock()
                inventory.addItem(armourPiece.converted()!!)
                dialogue {
                    item(armourPiece.converted(), "You successfully create a ${armourPiece.converted()?.name}.")
                }
            }

        }
    }

    private fun Player.performThunderClouds() {
        // Animate
        animation = Animation(6294)
        // Since we can't have the player perform 2 graphics
        World.sendGraphics(Graphics(2294), location)
        graphics = Graphics(2288)
        World.sendSoundEffect(location, SoundEffect(3614, 5, 86))
    }


    private fun Item.converted() : Item? {
        return when(this.id) {
            TORVA_FULL_HELM -> return Item(SANGUINE_TORVA_FULL_HELM)
            TORVA_PLATEBODY -> return Item(SANGUINE_TORVA_PLATEBODY)
            TORVA_PLATELEGS -> return Item(SANGUINE_TORVA_PLATELEGS)
            else -> null
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ANCIENT_BLOOD_ORNAMENT_KIT, TORVA_FULL_HELM, TORVA_PLATEBODY, TORVA_PLATELEGS)


}