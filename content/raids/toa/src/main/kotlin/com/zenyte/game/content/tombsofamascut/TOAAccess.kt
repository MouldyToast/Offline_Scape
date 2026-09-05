@file:JvmName("TOAAccess")

package com.zenyte.game.content.tombsofamascut

import com.google.common.eventbus.Subscribe
import com.zenyte.game.content.tombsofamascut.npc.AbstractTOANPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.api.attr.AttributeKey
import org.rsmod.game.events.PlayerDamageReceivedEvent
import org.rsmod.game.events.PlayerLoginEvent

/**
 * Transient session key for the player's ToA manager. No persistenceKey:
 * the manager never persisted (it was a transient Player field), and ToA's
 * durable data lives in Player.toaPlayerData.
 */
@JvmField
val TOA_MANAGER_KEY: AttributeKey<TOAManager> = AttributeKey()

/**
 * Lazy accessor mirroring the old always-present field-initializer
 * semantics. Java callers use TOAAccess.getToaManager(player).
 */
val Player.toaManager: TOAManager
    get() = attr.getOrPut(TOA_MANAGER_KEY) { TOAManager(this) }

@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    val bus = event.worldThread.eventBus

    // Replaces Player.java's `toaManager.onLogin()` — the PlayerLoginEvent
    // publish was relocated to that exact spot, preserving timing.
    bus.subscribeUnbound(PlayerLoginEvent::class.java) {
        player.toaManager.onLogin()
    }

    // Replaces Player.java's pre-removeHitpoints damage tracking. The
    // engine publishes BEFORE removeHitpoints (B.5), so player.hitpoints
    // here is the pre-hit value, matching the original
    // Math.min(hitpoints, damage).
    bus.subscribeUnbound(PlayerDamageReceivedEvent::class.java) {
        if (source is AbstractTOANPC) {
            val manager = player.toaManager
            manager.damageTaken += minOf(player.hitpoints, damage)
        }
    }
}
