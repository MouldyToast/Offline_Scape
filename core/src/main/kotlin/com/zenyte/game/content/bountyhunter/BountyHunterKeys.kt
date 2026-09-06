@file:JvmName("BountyHunterKeys")

package com.zenyte.game.content.bountyhunter

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted bounty hunter state. Saved under attrPersistence["bounty_hunter"]
 * as the [BountyHunter] instance itself; Gson skips the transient player
 * back-ref and transient target, so only the lastSkips set round-trips.
 * Points and the teleport unlock live in persistent varps, untouched by this
 * migration.
 */
@JvmField
val BOUNTY_HUNTER_KEY: AttributeKey<BountyHunter> = AttributeKey(persistenceKey = "bounty_hunter")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new BountyHunter(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. A Gson-rehydrated snapshot is never stored
 * directly: its transient player back-ref is null — skip entries are copied
 * into a properly-parented instance via copyFrom instead. Never returns null.
 */
fun Player.bountyHunter(): BountyHunter {
    val raw = rawBountyHunterAttr(this)
    if (raw is BountyHunter) {
        return raw
    }
    val bounty = BountyHunter(this)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), BountyHunter::class.java)
        if (typed != null) {
            bounty.copyFrom(typed)
        }
    }
    attr[BOUNTY_HUNTER_KEY] = bounty
    return bounty
}

/**
 * Untyped view of the value stored under [BOUNTY_HUNTER_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed
 * [BountyHunter] after, or null. Exists because AttributeMap.get's unchecked
 * cast makes a typed read unsafe before rehydration.
 */
fun rawBountyHunterAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[BOUNTY_HUNTER_KEY as AttributeKey<Any>]
}
