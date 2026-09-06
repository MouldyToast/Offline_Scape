@file:JvmName("PrayerLifecycleHooks")

package com.zenyte.game.content.skills.prayer

import com.google.common.eventbus.Subscribe
import com.zenyte.game.world.entity.player.PrayerVarbits
import com.zenyte.plugins.events.PlayerResetEvent
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerDeathStartEvent
import org.rsmod.game.events.PlayerLoginEvent
import org.rsmod.game.events.PlayerPostDamageEvent

/**
 * Prayer effects lifted out of Player (T2-a). Each subscriber preserves
 * the exact inline logic it replaced:
 * - retribution: sendDeath position, varbit-gated (post-T3.1a truth),
 *   source may be any Entity or null (custom death path).
 * - redemption: removeHitpoints !isDead() position — the event is only
 *   published for surviving players with post-hit hitpoints, so the
 *   subscriber re-checks nothing about death.
 * - reset: deactivateActivePrayers at the PlayerResetEvent post, wrapped
 *   by the post site's existing try/catch.
 * - login: quick-prayer varbit refresh (was onLobbyClose; E6 world-entry
 *   timing precedent).
 * Retribution vs death-charge cross-module order is indeterminate and
 * irrelevant: different resources (AoE damage vs spec restore).
 */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    val bus = event.worldThread.eventBus
    bus.subscribeUnbound(PlayerDeathStartEvent::class.java) {
        if (player.varManager.getBitValue(PrayerVarbits.RETRIBUTION) == 1) {
            player.prayerManager().applyRetributionEffect(source)
        }
    }
    bus.subscribeUnbound(PlayerPostDamageEvent::class.java) {
        if (player.hitpoints < player.maxHitpoints * 0.1F &&
            player.varManager.getBitValue(PrayerVarbits.REDEMPTION) == 1
        ) {
            player.prayerManager().applyRedemptionEffect()
        }
    }
    bus.subscribeUnbound(PlayerLoginEvent::class.java) {
        player.prayerManager().refreshQuickPrayers()
    }
}

@Subscribe
fun onPlayerReset(event: PlayerResetEvent) {
    event.player.prayerManager().deactivateActivePrayers()
}
