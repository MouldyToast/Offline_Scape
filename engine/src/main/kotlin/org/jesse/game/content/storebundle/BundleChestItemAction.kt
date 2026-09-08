package org.jesse.game.content.storebundle

import org.jesse.api.model.CreditStoreBundleItem
import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.privilege.GameMode

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.6.2025
 */
@Suppress("unused")
class BundleChestItemAction : ItemPlugin() {
    override fun handle() {
        bind("Unpack") { player, bundleChest, container, slotId ->
            val bundle = BundleChest.getForItemOrNull(bundleChest.id) ?: return@bind
            val items = BundleUpdater.bundleItemsByBundleChest[bundle] ?: return@bind
            val bundleSize = items.size
            if(player.gameMode == GameMode.ULTIMATE_IRON_MAN && player.inventory.freeSlots < bundleSize) {
                player.sendMessage("You need at least $bundleSize inventory slots to open this.")
                return@bind
            }
            if(player.inventory.deleteItem(bundleChest).result == RequestResult.SUCCESS) {
                player.sendMessage(Colour.TURQOISE.wrap("You have unboxed your ${bundleChest.name} and received:"))
                for(bundleItem in items) {
                    val item = bundleItem.item()
                    player.sendMessage(Colour.TURQOISE.wrap(" " + item.amount + " x " + item.name))
                    player.tryAddInventoryThenBank(bundleItem.item())
                }
            }
        }
    }

    fun CreditStoreBundleItem.item() = Item(this.itemId, this.amount)

    override fun getItems(): IntArray = BundleChest.entries.map { it.itemId }.toIntArray()
}