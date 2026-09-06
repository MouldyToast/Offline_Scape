@file:JvmName("AvasDeviceHooks")

package com.zenyte.game.content

import com.google.common.eventbus.Subscribe
import com.zenyte.game.world.WorldThread
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerLoginEvent
import org.rsmod.game.events.PlayerTimerEvent
import org.rsmod.game.timer.PlayerTimers

/**
 * T2-a: Ava's device metal collection on a soft timer (id reserved in
 * T3.2). The cape null-guard mirrors the old call site; collectMetal is
 * internally attribute-guarded and timing-randomized, so the position
 * shift within the tick is inert (verified). Bare body like the old
 * inline call — no per-driver try/catch existed.
 */
@Subscribe
fun onServerLaunchAvasDevice(event: ServerLaunchEvent) {
    val bus = event.worldThread.eventBus
    bus.subscribeUnbound(PlayerLoginEvent::class.java) {
        player.softTimers.schedule(PlayerTimers.AVAS_DEVICE, WorldThread.getCurrentCycle().toInt(), interval = 1)
    }
    bus.subscribeKeyed(PlayerTimerEvent.Soft::class.java, PlayerTimers.AVAS_DEVICE.toLong()) {
        if (player.cape != null) {
            AvasDevice.collectMetal(player)
        }
    }
}
