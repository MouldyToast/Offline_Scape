@file:JvmName("GravestoneKeys")

package com.zenyte.game.content.gravestones

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted gravestone state. Saved under attrPersistence["gravestone"] as the
 * [Gravestone] instance itself; Gson skips the transient player back-ref, the
 * transient placed [GravestoneNPC] and the transient tick interval, so only
 * gravestoneLocation, the retrieval container and coinsInCoffer round-trip.
 * The placed gravestone NPC is re-spawned at login by the existing LoginEvent
 * subscriber from the persistent timer varbit.
 */
@JvmField
val GRAVESTONE_KEY: AttributeKey<Gravestone> = AttributeKey(persistenceKey = "gravestone")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new Gravestone(player)) on first access, and rehydrates the raw persistence
 * map left by AttributeMap.putAllFromPersistence if this is the first access
 * after a load. A Gson-rehydrated snapshot is never stored directly: its
 * transient player back-ref is null — state is copied into a properly-parented
 * instance via copyFrom instead. Declared as an extension so Kotlin call sites
 * keep their implicit-receiver ergonomics; Java sees the same
 * GravestoneKeys.gravestone(player) static facade as the other D/E-series
 * keys. Never returns null.
 */
fun Player.gravestone(): Gravestone {
    val raw = rawGravestoneAttr(this)
    if (raw is Gravestone) {
        return raw
    }
    val gravestone = Gravestone(this)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), Gravestone::class.java)
        if (typed != null) {
            gravestone.copyFrom(typed)
        }
    }
    attr[GRAVESTONE_KEY] = gravestone
    return gravestone
}

/**
 * Untyped view of the value stored under [GRAVESTONE_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed [Gravestone]
 * after, or null. Exists because AttributeMap.get's unchecked cast makes a
 * typed read unsafe before rehydration.
 */
fun rawGravestoneAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[GRAVESTONE_KEY as AttributeKey<Any>]
}

/**
 * Read-only snapshot for OFFLINE scans over parser players
 * (LoginManager.deserializePlayerFromFile — WealthScanner and friends).
 * Parser players are Unsafe-allocated, so their transient attr map is null
 * and [gravestone] cannot be used on them. Reads the legacy top-level field
 * first (pre-migration saves), then the raw attrPersistence["gravestone"]
 * shape (post-migration saves). Returns null when the save carries neither.
 * The snapshot is unparented (transient player is null) — container reads
 * only; never store it in a live player's attr.
 */
fun scanGravestone(parser: Player): Gravestone? {
    @Suppress("DEPRECATION")
    val legacy = parser.gravestone
    if (legacy != null) {
        return legacy
    }
    val raw = parser.attrPersistenceRaw?.get(GRAVESTONE_KEY.persistenceKey) ?: return null
    val gson = LoginManager.gson.get()
    return gson.fromJson(gson.toJsonTree(raw), Gravestone::class.java)
}
