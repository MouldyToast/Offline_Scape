package org.jesse.plugins

import org.jesse.plugins.events.UnboundEvent

/**
 * DSL extension for registering event handlers inside [PluginScript.startup].
 *
 * Usage:
 * ```
 * override fun ScriptContext.startup() {
 *     onEvent<LoginEvent> { event ->
 *         event.player.sendMessage("Welcome!")
 *     }
 * }
 * ```
 *
 * Matches OpenRune's `org.rsmod.api.script.ScriptEventExtensions.onEvent`.
 */
inline fun <reified T : UnboundEvent> ScriptContext.onEvent(
    noinline action: (T) -> Unit
): Unit = eventBus.subscribe(T::class.java, action)
