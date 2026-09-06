@file:JvmName("PrayerManagerKeys")

package com.zenyte.game.content.skills.prayer

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted prayer state. Saved under attrPersistence["prayer_manager"] as
 * the [PrayerManager] instance itself; Gson skips the transient player
 * back-ref, active-prayer map and drain bookkeeping, so only the
 * quick-prayer settings int round-trips. Prayer points live in the Prayer
 * skill and are persisted with the skills, untouched by this migration.
 */
@JvmField
val PRAYER_MANAGER_KEY: AttributeKey<PrayerManager> = AttributeKey(persistenceKey = "prayer_manager")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new PrayerManager(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. A Gson-rehydrated snapshot is never stored
 * directly: its transient player back-ref is null — the quick-prayer
 * settings are copied into a properly-parented instance via the
 * pre-existing setPrayer (the same method the legacy setFields load path
 * always used). Never returns null.
 */
fun Player.prayerManager(): PrayerManager {
    val raw = rawPrayerManagerAttr(this)
    if (raw is PrayerManager) {
        return raw
    }
    val manager = PrayerManager(this)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), PrayerManager::class.java)
        if (typed != null) {
            manager.setPrayer(typed)
        }
    }
    attr[PRAYER_MANAGER_KEY] = manager
    return manager
}

/**
 * Untyped view of the value stored under [PRAYER_MANAGER_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed
 * [PrayerManager] after, or null. Exists because AttributeMap.get's
 * unchecked cast makes a typed read unsafe before rehydration.
 */
fun rawPrayerManagerAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[PRAYER_MANAGER_KEY as AttributeKey<Any>]
}
