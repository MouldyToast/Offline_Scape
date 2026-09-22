package org.jesse.plugins

import jakarta.inject.Inject
import org.jesse.plugins.events.EventBus

/**
 * Provides access to the [EventBus] inside [PluginScript.startup].
 * Singleton — one instance created by Guice and shared across all scripts.
 *
 * Matches OpenRune's `org.rsmod.plugin.scripts.ScriptContext`.
 */
class ScriptContext @Inject constructor(
    val eventBus: EventBus
)
