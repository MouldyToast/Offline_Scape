@file:JvmName("PlayerTickHooks")

package com.zenyte.game.content

import com.google.common.eventbus.Subscribe
import com.zenyte.game.content.skills.farming.farming
import com.zenyte.game.content.skills.hunter.hunter
import com.zenyte.game.content.skills.prayer.prayerManager
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerProcessEvent

/**
 * Registers the per-tick content drivers that used to be hardcoded in
 * Player.processEntity. ONE subscriber keeps the inter-driver order
 * (farming -> hunter -> prayer) identical by construction, and the body
 * deliberately has no try/catch: EventBus.publish propagates a subscriber
 * throw to the publish site, which sits inside the same try/catch the three
 * direct calls shared — a throwing driver still skips the remaining drivers
 * and the rest of that block, logged by the same handler as before.
 */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    val bus = event.worldThread.eventBus
    bus.subscribeUnbound(PlayerProcessEvent::class.java) {
        player.farming().processAll()
        player.hunter().process()
        player.prayerManager().process()
    }
}
