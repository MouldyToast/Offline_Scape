@file:Suppress("unused")

package org.jesse.api

import com.google.common.eventbus.Subscribe
import org.jesse.plugins.events.ServerLaunchEvent

/**
 * Represents the game hooks for API related activities.
 *
 * @author Stan van der Bend
 */
object APIHooks {

    /**
     * Registers a server launch event hook that will start the api callback server.
     */
    @Subscribe
    @JvmStatic
    fun onServerLaunched(event: ServerLaunchEvent) {
        if (event.worldProfile.isDevelopment()) return

        APICallbackServer.start()
    }

}
