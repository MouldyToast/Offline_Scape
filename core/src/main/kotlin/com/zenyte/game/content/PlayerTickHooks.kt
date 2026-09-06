@file:JvmName("PlayerTickHooks")

package com.zenyte.game.content

import com.google.common.eventbus.Subscribe
import com.zenyte.game.content.skills.farming.farming
import com.zenyte.game.content.skills.hunter.hunter
import com.zenyte.game.content.skills.prayer.prayerManager
import com.zenyte.game.world.WorldThread
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerLoginEvent
import org.rsmod.game.events.PlayerTimerEvent
import org.rsmod.game.timer.PlayerTimers

/**
 * Per-tick content drivers on OpenRune-shape soft timers (T3.2), replacing
 * the transitional PlayerProcessEvent broadcast. Scheduling farming ->
 * hunter -> prayer in ONE login subscriber fixes the fire order: the timer
 * map iterates in insertion order, the expired-key buffer preserves it,
 * and fastutil's linked map keeps a key's position on value overwrite, so
 * lobby-hop re-scheduling (schedule() overwrites by key) never duplicates
 * or reorders. Handlers deliberately have no try/catch: a throw propagates
 * through PlayerTimerProcessor into the same processEntity try/catch the
 * direct calls shared — the thrown timer is not re-armed, stays expired,
 * and retries next tick; timers after it are skipped for the tick,
 * matching the old skip-remaining-drivers behavior.
 */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    val bus = event.worldThread.eventBus
    bus.subscribeUnbound(PlayerLoginEvent::class.java) {
        val clock = WorldThread.getCurrentCycle().toInt()
        player.softTimers.schedule(PlayerTimers.FARMING, clock, interval = 1)
        player.softTimers.schedule(PlayerTimers.HUNTER, clock, interval = 1)
        player.softTimers.schedule(PlayerTimers.PRAYER_DRAIN, clock, interval = 1)
    }
    bus.subscribeKeyed(PlayerTimerEvent.Soft::class.java, PlayerTimers.FARMING.toLong()) {
        player.farming().processAll()
    }
    bus.subscribeKeyed(PlayerTimerEvent.Soft::class.java, PlayerTimers.HUNTER.toLong()) {
        player.hunter().process()
    }
    bus.subscribeKeyed(PlayerTimerEvent.Soft::class.java, PlayerTimers.PRAYER_DRAIN.toLong()) {
        player.prayerManager().process()
    }
}
