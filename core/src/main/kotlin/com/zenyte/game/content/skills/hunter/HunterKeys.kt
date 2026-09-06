@file:JvmName("HunterKeys")

package com.zenyte.game.content.skills.hunter

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted hunter state. Saved under attrPersistence["hunter"] as the
 * [Hunter] instance itself; Gson skips the transient player back-ref and the
 * transient trap lists, so only the built-birdhouse list round-trips. Laid
 * traps are dismantled at logout by the existing LogoutEvent subscriber and
 * never persist.
 */
@JvmField
val HUNTER_KEY: AttributeKey<Hunter> = AttributeKey(persistenceKey = "hunter")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new Hunter(player)) on first access, and rehydrates the raw persistence
 * map left by AttributeMap.putAllFromPersistence if this is the first access
 * after a load. A Gson-rehydrated snapshot is never stored directly: its
 * transient WeakReference back-ref and trap lists are null — birdhouse state
 * is copied into a properly-parented instance via copyFrom instead (which
 * also re-parents each Birdhouse, exactly as the legacy load path did).
 * Never returns null.
 */
fun Player.hunter(): Hunter {
    val raw = rawHunterAttr(this)
    if (raw is Hunter) {
        return raw
    }
    val hunter = Hunter(this)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), Hunter::class.java)
        if (typed != null) {
            hunter.copyFrom(typed)
        }
    }
    attr[HUNTER_KEY] = hunter
    return hunter
}

/**
 * Untyped view of the value stored under [HUNTER_KEY]: the raw Map produced
 * by putAllFromPersistence before rehydration, the typed [Hunter] after, or
 * null. Exists because AttributeMap.get's unchecked cast makes a typed read
 * unsafe before rehydration.
 */
fun rawHunterAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[HUNTER_KEY as AttributeKey<Any>]
}
