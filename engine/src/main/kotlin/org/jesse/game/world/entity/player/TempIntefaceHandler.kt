package org.jesse.game.world.entity.player

import org.jesse.game.GameInterface
import org.jesse.game.model.ui.Interface
import org.jesse.game.model.ui.Interface.BasicHandler
import org.jesse.game.model.ui.Interface.Handler
import org.jesse.game.world.entity.player.Player
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * Represents a map of bit-packed interface components to [handlers][Handler].
 *
 * These override the 'default' handlers defined in [Interface.handlers].
 */
@Suppress("UNCHECKED_CAST")
val Player.tempInterfaceHandlers : Int2ObjectOpenHashMap<Int2ObjectOpenHashMap<Handler>>
    get() {
        var handlers = temporaryAttributes["tempInterfaceHandlers"] as? Int2ObjectOpenHashMap<Int2ObjectOpenHashMap<Handler>>
        if (handlers == null) {
            handlers = Int2ObjectOpenHashMap()
            temporaryAttributes["tempInterfaceHandlers"] = handlers
        }
        return handlers
    }

/* BIND */

fun Player.tempInterfaceBind(widget: GameInterface, bitPacked: Int, handler: Handler) {
    tempInterfaceBind(widget.plugin.get(), bitPacked, handler)
}

fun Player.tempInterfaceBind(widget: GameInterface, bitPacked: Int, handler: BasicHandler) {
    tempInterfaceBind(widget.plugin.get(), bitPacked, handler)
}

fun Player.tempInterfaceBind(widget: GameInterface, name: String, handler: Handler) {
    tempInterfaceBind(widget.plugin.get(), name, handler)
}

fun Player.tempInterfaceBind(widget: GameInterface, name: String, handler: BasicHandler) {
    tempInterfaceBind(widget.plugin.get(), name, handler)
}

fun Player.tempInterfaceBind(widget: Interface, name: String, handler: Handler) {
    tempInterfaceBind(widget, widget.getComponentBitpacked(name), handler)
}

fun Player.tempInterfaceBind(widget: Interface, name: String, handler: BasicHandler) {
    tempInterfaceBind(widget, widget.getComponentBitpacked(name), handler)
}

fun Player.tempInterfaceBind(widget: Interface, bitPacked: Int, handler: Handler) {
    var handlers = tempInterfaceHandlers[widget.id]
    if (handlers == null) {
        handlers = Int2ObjectOpenHashMap()
        tempInterfaceHandlers[widget.id] = handlers
    }

    handlers[bitPacked] = handler
}

fun Player.tempInterfaceBind(widget: Interface, bitPacked: Int, handler: BasicHandler) {
    var handlers = tempInterfaceHandlers[widget.id]
    if (handlers == null) {
        handlers = Int2ObjectOpenHashMap()
        tempInterfaceHandlers[widget.id] = handlers
    }

    handlers[bitPacked] = handler
}

/* UNBIND */

fun Player.removeTempInterfaceBind(widget: GameInterface, bitPacked: Int) {
    removeTempInterfaceBind(widget.plugin.get(), bitPacked)
}

fun Player.removeTempInterfaceBind(widget: GameInterface, name: String) {
    removeTempInterfaceBind(widget.plugin.get(), name)
}

fun Player.removeTempInterfaceBind(widget: Interface, name: String) {
    removeTempInterfaceBind(widget, widget.getComponentBitpacked(name))
}

fun Player.removeTempInterfaceBind(widget: Interface, bitPacked: Int) {
    val handlers = tempInterfaceHandlers[widget.id]?: return
    handlers.remove(bitPacked)
}
