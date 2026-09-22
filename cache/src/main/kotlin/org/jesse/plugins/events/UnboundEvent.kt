package org.jesse.plugins.events

/**
 * Marker interface for the new event bus system. All events that can be
 * published through [org.jesse.plugins.events.EventBus] implement this.
 *
 * The existing [org.jesse.plugins.Event] interface extends this, so all
 * legacy events are automatically compatible.
 */
interface UnboundEvent
