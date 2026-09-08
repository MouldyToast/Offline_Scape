package org.jesse.game.content.wilderness.event.chest

import com.google.common.eventbus.Subscribe
import org.jesse.plugins.events.ServerLaunchEvent

/**
 * The module that initializes the wilderness chest event.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object WildernessChestModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
//        WildernessEventManager.registerEvent(WildernessChestEvent)
    }
}
