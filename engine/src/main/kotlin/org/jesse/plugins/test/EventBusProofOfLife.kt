package org.jesse.plugins.test

import org.jesse.plugins.PluginScript
import org.jesse.plugins.ScriptContext
import org.jesse.plugins.onEvent
import org.jesse.plugins.events.ServerLaunchEvent
import org.slf4j.LoggerFactory

/**
 * Proof-of-life test for the new event bus system.
 * If this fires at boot, the full pipeline works:
 *   ClassGraph discovery → Guice creation → startup() → onEvent → bridge dispatch
 *
 * Delete this file after verifying.
 */
class EventBusProofOfLife : PluginScript() {

    private val log = LoggerFactory.getLogger(EventBusProofOfLife::class.java)

    override fun ScriptContext.startup() {
        onEvent<ServerLaunchEvent> {
            log.info(">>> EventBus proof of life: ServerLaunchEvent received through new PluginScript system! <<<")
        }
    }
}
