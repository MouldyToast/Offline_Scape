package org.jesse.api.service.store

import com.google.common.eventbus.Subscribe
import org.jesse.plugins.events.LoginEvent
import org.jesse.plugins.events.ServerLaunchEvent

/**
 * Represents the hooks for store related events.
 */
@Suppress("unused")
internal object StoreHooks {

    @Subscribe
    @JvmStatic
    fun onLogin(loginEvent: LoginEvent) {
        val player = loginEvent.player
        val claimedOrdersToMention = player.storeClaimedOrdersToMentionOnNextLogin
        if (claimedOrdersToMention.isNotEmpty()) {
            val totalCredits = claimedOrdersToMention.sumOf { it.products.second }
            player.notify("Your bond(s) and $totalCredits bonus credits have been added to your account!", schedule = false)
            player.storeClaimedOrdersToMentionOnNextLogin.clear()
        }
    }

    @Subscribe
    @JvmStatic
    fun onServerLaunch(serverLaunchEvent: ServerLaunchEvent) {
        if (!serverLaunchEvent.worldProfile.isMainDatabaseEnabled()) {
            return
        }
        StorePlayerHandler.populateOrderData()
    }
}
