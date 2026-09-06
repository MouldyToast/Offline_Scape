@file:JvmName("ConstructionKeys")

package com.zenyte.game.content.skills.construction

import com.google.common.eventbus.Subscribe
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerLogoutEvent

/**
 * Persisted construction (player-owned house) state. Saved under
 * attrPersistence["construction"] as the [Construction] instance itself; Gson
 * skips the transient player back-ref, house-session state (allocated area,
 * chunk coordinates, building mode, house viewer, cats) and the transient
 * TipJar player ref, so the room references, costume-room boxes, display
 * jars, house style, decoration, servant's cash and tip jar contents
 * round-trip.
 */
@JvmField
val CONSTRUCTION_KEY: AttributeKey<Construction> = AttributeKey(persistenceKey = "construction")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new Construction(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. A Gson-rehydrated snapshot is never stored
 * directly: its transient player back-refs are null — state is copied into a
 * properly-parented instance via the pre-existing setFields (the same
 * copy-into the Phase 0 load path always used, costume-box re-parenting
 * included). Never returns null.
 */
fun Player.construction(): Construction {
    val raw = rawConstructionAttr(this)
    if (raw is Construction) {
        return raw
    }
    val construction = Construction(this)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), Construction::class.java)
        if (typed != null) {
            construction.setFields(typed)
        }
    }
    attr[CONSTRUCTION_KEY] = construction
    return construction
}

/**
 * Untyped view of the value stored under [CONSTRUCTION_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed
 * [Construction] after, or null. Exists because AttributeMap.get's unchecked
 * cast makes a typed read unsafe before rehydration.
 */
fun rawConstructionAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[CONSTRUCTION_KEY as AttributeKey<Any>]
}

/** T2-a: tip-jar bank-out moved off the Player logout block. Pure bank mutation, no packets (verified); persisted outcome identical. */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    event.worldThread.eventBus.subscribeUnbound(PlayerLogoutEvent::class.java) {
        player.construction().tipJar.onLogout()
    }
}
