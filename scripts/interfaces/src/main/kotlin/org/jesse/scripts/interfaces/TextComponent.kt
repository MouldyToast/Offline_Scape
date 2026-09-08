package org.jesse.scripts.interfaces

import org.jesse.game.model.ui.Interface
import org.jesse.game.world.entity.attribute
import org.jesse.game.world.entity.player.Player
import kotlin.reflect.KProperty

/**
 * Keep track of component text values.
 */
var Player.textComponentsMap by attribute<Player, MutableMap<Int, MutableMap<Int, String>>>("text-components", mutableMapOf())

/**
 * Delegate property for handling interface component text updates.
 *
 * @author Stan van der Bend
 */
class TextComponent(
    private val widget: Interface,
    private val componentId: Int,
    private val player: Player,
    private val defaultValue: String = ""
) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        player.textComponentsMap.getOrPut(widget.id) { mutableMapOf() }[componentId]?:defaultValue

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        player.textComponentsMap.getOrPut(widget.id) { mutableMapOf() }[componentId] = value
        player.packetDispatcher.sendComponentText(widget.id, componentId, value)
    }
}
