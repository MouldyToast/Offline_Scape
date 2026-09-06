@file:JvmName("SlayerKeys")

package com.zenyte.game.content.skills.slayer

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted slayer state. Saved under attrPersistence["slayer"] as the
 * [Slayer] instance itself; Gson skips the transient player and partner
 * back-refs (and the Assignment's transient player/slayer/task/area), so the
 * current and stored assignments, the banned-task map, the current master and
 * the last assignment name round-trip. Slayer points, unlocks and task
 * counters are varbit/varp/attribute-backed and never lived in this object.
 */
@JvmField
val SLAYER_KEY: AttributeKey<Slayer> = AttributeKey(persistenceKey = "slayer")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new Slayer(player)) on first access, and rehydrates the raw persistence
 * map left by AttributeMap.putAllFromPersistence if this is the first access
 * after a load. A Gson-rehydrated snapshot is never stored directly: its
 * transient back-refs are null — state is copied into a properly-parented
 * instance via copyFrom (the exact body of the legacy load-path initialize,
 * banned-task adoption and the dropped lastAssignmentName quirk included).
 * The fresh instance is stored BEFORE the copy runs: Assignment.initialize
 * re-reads the player's slayer for its back-ref mid-copy, and the legacy
 * path likewise saw the already-assigned live field at that point. Never
 * returns null.
 */
fun Player.slayer(): Slayer {
    val raw = rawSlayerAttr(this)
    if (raw is Slayer) {
        return raw
    }
    val slayer = Slayer(this)
    attr[SLAYER_KEY] = slayer
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), Slayer::class.java)
        if (typed != null) {
            slayer.copyFrom(typed)
        }
    }
    return slayer
}

/**
 * Untyped view of the value stored under [SLAYER_KEY]: the raw Map produced
 * by putAllFromPersistence before rehydration, the typed [Slayer] after, or
 * null. Exists because AttributeMap.get's unchecked cast makes a typed read
 * unsafe before rehydration.
 */
fun rawSlayerAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[SLAYER_KEY as AttributeKey<Any>]
}
