@file:JvmName("GrandExchangeKeys")

package com.zenyte.game.content.grandexchange

import com.google.common.eventbus.Subscribe
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerLoginEvent

/**
 * Persisted grand exchange state. Saved under
 * attrPersistence["grand_exchange"] as the [GrandExchange] instance itself;
 * Gson skips the transient player back-ref, so only the exchange history list
 * round-trips. Live offers are world-level state, untouched by this
 * migration.
 */
@JvmField
val GRAND_EXCHANGE_KEY: AttributeKey<GrandExchange> = AttributeKey(persistenceKey = "grand_exchange")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new GrandExchange(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. A Gson-rehydrated snapshot is never stored
 * directly: its transient player back-ref is null — history is copied into a
 * properly-parented instance via the pre-existing initialize (the same method
 * the legacy setFields load path always used, history cleanup included).
 * Never returns null.
 */
fun Player.grandExchange(): GrandExchange {
    val raw = rawGrandExchangeAttr(this)
    if (raw is GrandExchange) {
        return raw
    }
    val exchange = GrandExchange(this)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), GrandExchange::class.java)
        if (typed != null) {
            exchange.initialize(typed)
        }
    }
    attr[GRAND_EXCHANGE_KEY] = exchange
    return exchange
}

/**
 * Untyped view of the value stored under [GRAND_EXCHANGE_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed
 * [GrandExchange] after, or null. Exists because AttributeMap.get's unchecked
 * cast makes a typed read unsafe before rehydration.
 */
fun rawGrandExchangeAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[GRAND_EXCHANGE_KEY as AttributeKey<Any>]
}

/** T2-a: GE offer resend at login (was onLobbyClose; E6 timing precedent). */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    event.worldThread.eventBus.subscribeUnbound(PlayerLoginEvent::class.java) {
        player.grandExchange().updateOffers()
    }
}
