@file:Suppress("UNUSED", "UNUSED_PARAMETER")

package org.jesse.game.model.item.protection

import com.google.common.eventbus.Subscribe
import org.jesse.plugins.events.ServerLaunchEvent

object ItemProtectionModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunch(serverLaunchEvent: ServerLaunchEvent) {
        ItemProtectionValueManager.loadProtectionValues()
        ItemProtectionCommands.loadCommands()
    }
}
