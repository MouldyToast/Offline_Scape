package com.zenyte.plugins.item

import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.model.item.pluginextensions.bindKt

/**
 * Handles the `Ride` option of the [sled item][SLED].
 */
@Suppress("UNUSED")
class SledPlugin : ItemPlugin() {

    override fun handle() = bindKt("Ride") { player.equipment.wear(slotId) }

    override fun getItems() = intArrayOf(SLED, SLED_4084, SLED_25282)
}
