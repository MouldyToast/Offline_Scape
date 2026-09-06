@file:JvmName("SeedVaultKeys")

package com.zenyte.game.content.skills.farming.seedvault

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted seed vault state. Saved under attrPersistence["seed_vault"] as the
 * [SeedVault] instance itself; Gson skips its transient player back-reference
 * and the container's transient caches, so only the container's type, policy
 * and items round-trip.
 */
@JvmField
val SEED_VAULT_KEY: AttributeKey<SeedVault> = AttributeKey(persistenceKey = "seed_vault")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new SeedVault(player)) on first access, and rehydrates the raw persistence
 * map left by AttributeMap.putAllFromPersistence if this is the first access
 * after a load. Never returns null.
 */
fun seedVault(player: Player): SeedVault {
    val raw = rawSeedVaultAttr(player)
    if (raw is SeedVault) {
        return raw
    }
    val vault = SeedVault(player)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), SeedVault::class.java)
        val savedContainer = typed?.container
        if (savedContainer?.items != null) {
            vault.container.setContainer(savedContainer)
        }
    }
    player.attr[SEED_VAULT_KEY] = vault
    return vault
}

/**
 * Untyped view of the value stored under [SEED_VAULT_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed [SeedVault]
 * after, or null. Exists because AttributeMap.get's unchecked cast makes a
 * typed read unsafe before rehydration.
 */
fun rawSeedVaultAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[SEED_VAULT_KEY as AttributeKey<Any>]
}
