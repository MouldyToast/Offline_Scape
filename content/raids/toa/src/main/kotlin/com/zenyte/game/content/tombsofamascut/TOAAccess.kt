@file:JvmName("TOAAccess")

package com.zenyte.game.content.tombsofamascut

import com.google.common.eventbus.Subscribe
import com.zenyte.game.content.tombsofamascut.npc.AbstractTOANPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.events.ServerLaunchEvent
import com.zenyte.game.content.tombsofamascut.raid.TOAPlayerLogoutState
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerPolicy
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey
import org.rsmod.game.events.PlayerDamageReceivedEvent
import org.rsmod.game.events.PlayerLoginEvent
import org.rsmod.game.events.PlayerPreSaveEvent
import java.util.Optional

/**
 * Transient session key for the player's ToA manager. No persistenceKey:
 * the manager never persisted (it was a transient Player field). ToA's
 * durable data lives under [TOA_PLAYER_DATA_KEY].
 */
@JvmField
val TOA_MANAGER_KEY: AttributeKey<TOAManager> = AttributeKey()

/**
 * Lazy accessor mirroring the old always-present field-initializer
 * semantics. Java callers use TOAAccess.getToaManager(player).
 */
val Player.toaManager: TOAManager
    get() = attr.getOrPut(TOA_MANAGER_KEY) { TOAManager(this) }

/**
 * Persisted durable ToA data. Saved under attrPersistence["toa_player_data"]
 * as the [TOAPlayerData] instance itself; its per-raid scratch fields are
 * transient, so only partySettingData (invocation presets),
 * toaPlayerLogoutState (X-log rejoin) and rewardContainer round-trip.
 */
@JvmField
val TOA_PLAYER_DATA_KEY: AttributeKey<TOAPlayerData> = AttributeKey(persistenceKey = "toa_player_data")

/**
 * Typed accessor. Mirrors new TOAPlayerData() on first access, and
 * rehydrates the raw persistence map left by
 * AttributeMap.putAllFromPersistence if this is the first access after a
 * load. Java callers use TOAAccess.toaPlayerData(player). Never returns
 * null.
 */
fun toaPlayerData(player: Player): TOAPlayerData {
    val raw = rawToaPlayerDataAttr(player)
    if (raw is TOAPlayerData) {
        return raw
    }
    var data = TOAPlayerData()
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), TOAPlayerData::class.java)
        if (typed != null) {
            data = typed
            // Q1: a raw-deserialized Container lacks its six transient fields
            // and NPEs on use. Re-hydrate defensively via the module idiom
            // (TOAExt.kt) — guarded on the items map directly, never through
            // Container methods. Dead at runtime (the live reward system uses
            // pendingTOARewards) but pre-existing saves could contain items.
            val srcReward = typed.rewardContainer
            if (srcReward?.items?.isEmpty() == false) {
                val fresh = Container(ContainerType.TOA_REWARD, ContainerPolicy.ALWAYS_STACK, 6, Optional.of(player))
                fresh.setContainer(srcReward)
                data.rewardContainer = fresh
            } else {
                data.rewardContainer = null
            }
        }
    }
    player.attr[TOA_PLAYER_DATA_KEY] = data
    return data
}

/**
 * Untyped view of the value stored under [TOA_PLAYER_DATA_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed
 * [TOAPlayerData] after, or null. Exists because AttributeMap.get's
 * unchecked cast makes a typed read unsafe before rehydration.
 */
fun rawToaPlayerDataAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[TOA_PLAYER_DATA_KEY as AttributeKey<Any>]
}

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

    // ── Save half: sync live TOAManager state → the persisted attr ──
    // Published BEFORE refreshAttrPersistence() on the save pool thread, so
    // a first-time put lands in the snapshot. Skips sync when no manager was
    // lazily created this session (the attr value — raw or typed — already
    // holds what was loaded, and round-trips unchanged).
    bus.subscribeUnbound(PlayerPreSaveEvent::class.java) {
        val manager = player.attr[TOA_MANAGER_KEY] ?: return@subscribeUnbound
        val data = toaPlayerData(player)

        // partySettingData: reconstruct from live TOAPartySettings state.
        // Cannot delegate — TOAPartySettings shadows the data fields and
        // they diverge after construction (v1 investigation, unchanged).
        val settings = manager.partySettings as? TOAPartySettings
        if (settings != null) {
            data.partySettingData = TOAPartySettingData(
                settings.raidLevel,
                settings.activeInvocations,
                settings.kcRequirement,
                settings.invocationBitmaps
            )
        }

        // toaPlayerLogoutState: direct reference (set on logout, cleared on
        // login; null is the correct save value after a clean login).
        data.toaPlayerLogoutState = manager.toaPlayerLogoutState as? TOAPlayerLogoutState

        // rewardContainer: NOT synced — never populated at runtime (the live
        // reward system uses the transient pendingTOARewards attribute).
        // The accessor's defensive rehydration already normalized it.
    }
}
