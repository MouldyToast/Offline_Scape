@file:JvmName("ClanLifecycleHooks")

package com.zenyte.game.content.clans

import com.google.common.eventbus.Subscribe
import com.zenyte.game.GameConstants
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerLoginEvent
import org.rsmod.game.events.PlayerLogoutEvent

/**
 * T2-a: clan channel lifecycle moved off Player. leave() already ran
 * after setFinished today; it now also runs after the LOGOUT plugins,
 * which is safe because the only competing member-remover in that flow
 * (the provably dead ClanChannel.onLogout LOGOUT listener) was deleted
 * first — leave() is the sole remover and therefore order-independent.
 * The LOGOUT plugins and LogoutEvent subscribers were audited for
 * clan-channel/raid/instance reads: none. join() sends one message and
 * defers all real work onto a WorldTask, so its earlier position within
 * the login sequence (the PlayerLoginEvent publish vs. the old inline
 * branch ~40 lines later) is immaterial; nothing between the two sites
 * touches the "registered" attribute or the channel owner.
 */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    val bus = event.worldThread.eventBus
    bus.subscribeUnbound(PlayerLogoutEvent::class.java) {
        ClanManager.leave(player, false)
    }
    bus.subscribeUnbound(PlayerLoginEvent::class.java) {
        if (!player.getBooleanAttribute("registered") && player.settings.channelOwner == null) {
            ClanManager.join(player, GameConstants.SERVER_CHANNEL_NAME)
        }
    }
}
