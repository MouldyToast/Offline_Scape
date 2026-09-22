package org.jesse.plugins

/**
 * Base class for event-driven plugin scripts. Subclasses are discovered
 * automatically at boot via ClassGraph — no pre-scan step needed.
 *
 * Matches OpenRune's `org.rsmod.plugin.scripts.PluginScript`.
 */
abstract class PluginScript {

    /**
     * Called once at boot. Register event handlers here using
     * [ScriptContext.onEvent].
     */
    abstract fun ScriptContext.startup()
}
