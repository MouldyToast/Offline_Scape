@file:JvmName("FarmingKeys")

package com.zenyte.game.content.skills.farming

import com.google.common.eventbus.Subscribe
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerLoginEvent

/**
 * Persisted farming state. Saved under attrPersistence["farming"] as the
 * [Farming] instance itself; Gson skips the transient player back-ref and the
 * transient refreshed-patches set, so the spot list and the tool-storage item
 * list round-trip. FarmingSpot keeps its custom Gson deserializer
 * (Farming.deserializer, registered on LoginManager's builder): serialization
 * stays reflective ({"map": {...}} — FarmingSpot's only non-transient field),
 * and the typed rehydration pass below re-enters that adapter because the
 * declared element type is FarmingSpot.
 */
@JvmField
val FARMING_KEY: AttributeKey<Farming> = AttributeKey(persistenceKey = "farming")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new Farming(player)) on first access, and rehydrates the raw persistence
 * map left by AttributeMap.putAllFromPersistence if this is the first access
 * after a load. A Gson-rehydrated snapshot is never stored directly: its
 * transient player back-refs are null — it is adopted through the
 * pre-existing copy constructor Farming(player, other) instead (the exact
 * body of the legacy wholesale setter-replace: spots adopted by reference
 * and re-parented, storage list adopted into a player-parented
 * FarmingStorage). Never returns null.
 */
fun Player.farming(): Farming {
    val raw = rawFarmingAttr(this)
    if (raw is Farming) {
        return raw
    }
    var farming = Farming(this)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), Farming::class.java)
        if (typed != null) {
            farming = Farming(this, typed)
        }
    }
    attr[FARMING_KEY] = farming
    return farming
}

/**
 * Untyped view of the value stored under [FARMING_KEY]: the raw Map produced
 * by putAllFromPersistence before rehydration, the typed [Farming] after, or
 * null. Exists because AttributeMap.get's unchecked cast makes a typed read
 * unsafe before rehydration.
 */
fun rawFarmingAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[FARMING_KEY as AttributeKey<Any>]
}

/** T2-a: lobby-close farming refresh at login; the old site's own try/catch preserved. */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    event.worldThread.eventBus.subscribeUnbound(PlayerLoginEvent::class.java) {
        try {
            player.farming().refresh()
        } catch (e: Exception) {
            org.slf4j.LoggerFactory.getLogger("FarmingKeys").error("farming not working", e)
        }
    }
}
