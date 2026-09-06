@file:JvmName("DwarfMultiCannonKeys")

package com.zenyte.game.content.multicannon

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted dwarf multicannon state. Saved under
 * attrPersistence["dwarf_multicannon"] as the [DwarfMultiCannon] instance
 * itself; Gson skips the transient player back-ref and the transient placed
 * [Multicannon] world object, so only cannonballs, graniteballs, setupTime,
 * setupStage and type round-trip. The placed world cannon is re-bound at
 * login by the existing LOGIN listener via the placedCannons registry.
 */
@JvmField
val DWARF_MULTICANNON_KEY: AttributeKey<DwarfMultiCannon> = AttributeKey(persistenceKey = "dwarf_multicannon")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new DwarfMultiCannon(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. A Gson-rehydrated snapshot is never stored
 * directly: its transient player back-ref is null — state is copied into a
 * properly-parented instance via copyFrom instead. Note setupTime is a long:
 * the raw path preserves it because whole-number narrowing only affects
 * values that fit in Int, and JSON numbers deserialize by declared field
 * type either way. Never returns null.
 */
fun dwarfMulticannon(player: Player): DwarfMultiCannon {
    val raw = rawDwarfMulticannonAttr(player)
    if (raw is DwarfMultiCannon) {
        return raw
    }
    val cannon = DwarfMultiCannon(player)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), DwarfMultiCannon::class.java)
        if (typed != null) {
            cannon.copyFrom(typed)
        }
    }
    player.attr[DWARF_MULTICANNON_KEY] = cannon
    return cannon
}

/**
 * Untyped view of the value stored under [DWARF_MULTICANNON_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed
 * [DwarfMultiCannon] after, or null. Exists because AttributeMap.get's
 * unchecked cast makes a typed read unsafe before rehydration.
 */
fun rawDwarfMulticannonAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[DWARF_MULTICANNON_KEY as AttributeKey<Any>]
}
