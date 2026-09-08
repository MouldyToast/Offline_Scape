package org.jesse.plugins.item

import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.model.item.pluginextensions.bindKt

/**
 * Handles the `Ride` option of the [sled item][SLED].
 */
@Suppress("UNUSED")
class SledPlugin : ItemPlugin() {

    override fun handle() = bindKt("Ride") { player.equipment.wear(slotId) }

    override fun getItems() = intArrayOf(SLED, SLED_4084, SLED_25282)
}
