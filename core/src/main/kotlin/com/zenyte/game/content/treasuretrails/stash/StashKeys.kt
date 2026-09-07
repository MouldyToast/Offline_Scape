@file:JvmName("StashKeys")

package com.zenyte.game.content.treasuretrails.stash

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted STASH-unit state. Saved under attrPersistence["stash"] as the
 * [Stash] instance itself; Gson skips the transient player back-reference,
 * so only the stashes map round-trips.
 */
@JvmField
val STASH_KEY: AttributeKey<Stash> = AttributeKey(persistenceKey = "stash")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new Stash(player)) on first access — including the legacy
 * onInitialization guarantee that the stashes map is non-null — and
 * rehydrates the raw persistence map left by
 * AttributeMap.putAllFromPersistence if this is the first access after a
 * load. Never returns null.
 */
fun stash(player: Player): Stash {
    val raw = rawStashAttr(player)
    if (raw is Stash) {
        return raw
    }
    val stash = Stash(player)
    var saved: Stash? = null
    if (raw != null) {
        val gson = LoginManager.gson.get()
        saved = gson.fromJson(gson.toJsonTree(raw), Stash::class.java)
    }
    stash.adopt(saved)
    player.attr[STASH_KEY] = stash
    return stash
}

/**
 * Untyped view of the value stored under [STASH_KEY]: the raw Map produced
 * by putAllFromPersistence before rehydration, the typed [Stash] after, or
 * null. Exists because AttributeMap.get's unchecked cast makes a typed read
 * unsafe before rehydration.
 */
fun rawStashAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[STASH_KEY as AttributeKey<Any>]
}
