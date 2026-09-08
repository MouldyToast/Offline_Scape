package org.jesse.game.content.wilderness.event.ganodermic_beast

import com.google.common.eventbus.Subscribe
import org.jesse.game.content.wilderness.event.WildernessEventManager
import org.jesse.plugins.events.ServerLaunchEvent

@Suppress("unused")
object GanodermicBeastModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        WildernessEventManager.registerEvent(GanodermicBeastEvent)
    }
}
