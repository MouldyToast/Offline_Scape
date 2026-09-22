package org.jesse.plugins.events

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

/**
 * Backing map for [UnboundEvent] subscribers. Stores a list of actions
 * per event type. Thread-unsafe — all registration must complete before
 * events are published (satisfied by the boot sequence).
 *
 * Adapted from OpenRune's `org.rsmod.events.UnboundEventMap`.
 */
class UnboundEventMap {

    private val events = Object2ObjectOpenHashMap<Class<out UnboundEvent>,
            MutableList<(UnboundEvent) -> Unit>>()

    @Suppress("UNCHECKED_CAST")
    fun <T : UnboundEvent> add(type: Class<out T>, action: (T) -> Unit) {
        val list = events.getOrPut(type) { mutableListOf() }
        list.add(action as (UnboundEvent) -> Unit)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : UnboundEvent> get(type: Class<out T>): List<(T) -> Unit>? {
        return events[type] as? List<(T) -> Unit>
    }
}
