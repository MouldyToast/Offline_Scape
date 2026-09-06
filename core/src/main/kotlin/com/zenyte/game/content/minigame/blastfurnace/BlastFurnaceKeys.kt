@file:JvmName("BlastFurnaceKeys")

package com.zenyte.game.content.minigame.blastfurnace

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted blast furnace state. Saved under attrPersistence["blast_furnace"]
 * as the [BlastFurnace] instance itself; Gson skips the transient player/
 * smeltTask/flags, so only the ore and bar tally maps round-trip. The coffer
 * lives in the legacy string-attribute map ("blast_furnace_coffer") and is
 * untouched by this migration.
 */
@JvmField
val BLAST_FURNACE_KEY: AttributeKey<BlastFurnace> = AttributeKey(persistenceKey = "blast_furnace")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new BlastFurnace(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. A Gson-rehydrated snapshot is never stored
 * directly: its transient player back-ref is null — state is copied into a
 * properly-parented instance via copyFrom instead. Never returns null.
 */
fun blastFurnace(player: Player): BlastFurnace {
    val raw = rawBlastFurnaceAttr(player)
    if (raw is BlastFurnace) {
        return raw
    }
    val furnace = BlastFurnace(player)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), BlastFurnace::class.java)
        if (typed != null) {
            furnace.copyFrom(typed)
        }
    }
    player.attr[BLAST_FURNACE_KEY] = furnace
    return furnace
}

/**
 * Untyped view of the value stored under [BLAST_FURNACE_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed
 * [BlastFurnace] after, or null. Exists because AttributeMap.get's unchecked
 * cast makes a typed read unsafe before rehydration.
 */
fun rawBlastFurnaceAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[BLAST_FURNACE_KEY as AttributeKey<Any>]
}
