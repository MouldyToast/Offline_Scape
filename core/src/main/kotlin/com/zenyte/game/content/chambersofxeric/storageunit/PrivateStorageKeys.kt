@file:JvmName("PrivateStorageKeys")

package com.zenyte.game.content.chambersofxeric.storageunit

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted CoX private-storage state. Saved under
 * attrPersistence["private_storage"] as the [PrivateStorage] instance
 * itself; Gson skips the transient player back-reference and the
 * container's transient caches, so only the container round-trips.
 */
@JvmField
val PRIVATE_STORAGE_KEY: AttributeKey<PrivateStorage> = AttributeKey(persistenceKey = "private_storage")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new PrivateStorage(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is
 * the first access after a load. Never returns null.
 */
fun privateStorage(player: Player): PrivateStorage {
    val raw = rawPrivateStorageAttr(player)
    if (raw is PrivateStorage) {
        return raw
    }
    val storage = PrivateStorage(player)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), PrivateStorage::class.java)
        storage.adopt(typed)
    }
    player.attr[PRIVATE_STORAGE_KEY] = storage
    return storage
}

/**
 * Untyped view of the value stored under [PRIVATE_STORAGE_KEY]; see
 * SeedVaultKeys.rawSeedVaultAttr for why this exists.
 */
fun rawPrivateStorageAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[PRIVATE_STORAGE_KEY as AttributeKey<Any>]
}
