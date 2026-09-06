@file:JvmName("BarrowsKeys")

package com.zenyte.game.content.minigame.barrows

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted barrows state. Saved under attrPersistence["barrows"] as the
 * [Barrows] instance itself; Gson skips the transient player/container/timer/
 * puzzle/currentWight, so the persisted surface is skipTunnels, hiddenWight,
 * slainWights, shutDoorways, openDoorway, corner, potential, looted and
 * puzzleSolved — of which only the six the legacy load path ever copied are
 * rehydrated (see Barrows.copyFrom).
 */
@JvmField
val BARROWS_KEY: AttributeKey<Barrows> = AttributeKey(persistenceKey = "barrows")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new Barrows(player)) on first access, and rehydrates the raw persistence
 * map left by AttributeMap.putAllFromPersistence if this is the first access
 * after a load. A Gson-rehydrated snapshot is never stored directly: its
 * transient player/container/puzzle are null — state is copied into a
 * properly-constructed instance via copyFrom instead. Never returns null.
 */
fun barrows(player: Player): Barrows {
    val raw = rawBarrowsAttr(player)
    if (raw is Barrows) {
        return raw
    }
    val barrows = Barrows(player)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), Barrows::class.java)
        if (typed != null) {
            barrows.copyFrom(typed)
        }
    }
    player.attr[BARROWS_KEY] = barrows
    return barrows
}

/**
 * Untyped view of the value stored under [BARROWS_KEY]: the raw Map produced
 * by putAllFromPersistence before rehydration, the typed [Barrows] after, or
 * null. Exists because AttributeMap.get's unchecked cast makes a typed read
 * unsafe before rehydration.
 */
fun rawBarrowsAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[BARROWS_KEY as AttributeKey<Any>]
}
