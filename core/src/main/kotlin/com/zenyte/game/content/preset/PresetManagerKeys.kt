@file:JvmName("PresetManagerKeys")

package com.zenyte.game.content.preset

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted preset state. Saved under attrPersistence["preset_manager"] as the
 * [PresetManager] instance itself; Gson skips the transient WeakReference
 * back-ref, so only presets/defaultPreset/unlockedSlots round-trip.
 */
@JvmField
val PRESET_MANAGER_KEY: AttributeKey<PresetManager> = AttributeKey(persistenceKey = "preset_manager")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new PresetManager(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. A Gson-rehydrated snapshot is never stored
 * directly: its WeakReference back-ref is null, which would silently break
 * addPreset/getMaximumPresets — state is copied into a properly-parented
 * instance via copyFrom instead. Never returns null.
 */
fun presetManager(player: Player): PresetManager {
    val raw = rawPresetManagerAttr(player)
    if (raw is PresetManager) {
        return raw
    }
    val manager = PresetManager(player)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), PresetManager::class.java)
        if (typed != null) {
            manager.copyFrom(typed)
        }
    }
    player.attr[PRESET_MANAGER_KEY] = manager
    return manager
}

/**
 * Untyped view of the value stored under [PRESET_MANAGER_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed
 * [PresetManager] after, or null. Exists because AttributeMap.get's unchecked
 * cast makes a typed read unsafe before rehydration.
 */
fun rawPresetManagerAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[PRESET_MANAGER_KEY as AttributeKey<Any>]
}
