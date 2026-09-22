package org.jesse.plugins

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import org.jesse.plugins.events.EventBus
import org.jesse.plugins.events.UnboundEventMap

/**
 * Guice module binding the event bus infrastructure as singletons.
 *
 * Matches OpenRune's `org.rsmod.server.shared.module.EventModule`.
 */
object EventModule : AbstractModule() {

    override fun configure() {
        bind(UnboundEventMap::class.java).`in`(Scopes.SINGLETON)
        bind(EventBus::class.java).`in`(Scopes.SINGLETON)
        bind(ScriptContext::class.java).`in`(Scopes.SINGLETON)
    }
}
