package org.jesse.plugins.events

/**
 * Type-safe event bus for [UnboundEvent] subscribers. Events are registered
 * via [subscribe] during boot, and dispatched via [publish] at runtime.
 *
 * Adapted from OpenRune's `org.rsmod.events.EventBus` (UnboundEvent portion only).
 */
class EventBus(val unbound: UnboundEventMap = UnboundEventMap()) {

    fun <T : UnboundEvent> subscribe(type: Class<T>, action: (T) -> Unit) {
        unbound.add(type, action)
    }

    fun <T : UnboundEvent> publish(event: T): Boolean {
        val actions = unbound[event::class.java] ?: return false
        for (action in actions) {
            action(event)
        }
        return true
    }
}
