package com.zenyte.plugins.item

import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.model.item.pluginextensions.bindKt

/**
 * Handles the `Ride` option of the [sled item][ItemId.SLED].
 */
@Suppress("UNUSED")
class SledPlugin : ItemPlugin() {

    override fun handle() = bindKt("Ride") { player.equipment.wear(slotId) }

    override fun getItems() = intArrayOf(ItemId.SLED, ItemId.SLED_4084, ItemId.SLED_25282)
}
