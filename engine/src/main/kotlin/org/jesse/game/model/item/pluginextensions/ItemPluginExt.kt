package org.jesse.game.model.item.pluginextensions

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemPlugin.OptionHandler
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.Container

/**
 * Context to pass [bindKt] lambda, represents the parameters of [OptionHandler.handle] function.
 */
data class OptionContext(val player: Player, val item: Item, val container: Container, val slotId: Int)

/**
 * Kotlin wrapper for [ItemPlugin.bind] function call.
 */
fun ItemPlugin.bindKt(option: String, handler: OptionContext.() -> Unit) {
    bind(option) { player, item, container, slotId ->
        handler.invoke(OptionContext(player, item, container, slotId))
    }
}