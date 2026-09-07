@file:JvmName("RetrievalServiceKeys")

package com.zenyte.game.content

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted item-retrieval-service state. Saved under
 * attrPersistence["item_retrieval"] as the [ItemRetrievalService] instance
 * itself; Gson skips the transient player back-reference and the container's
 * transient caches, so only the container contents, type, and locked flag
 * round-trip.
 */
@JvmField
val ITEM_RETRIEVAL_KEY: AttributeKey<ItemRetrievalService> = AttributeKey(persistenceKey = "item_retrieval")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new ItemRetrievalService(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. Never returns null.
 */
fun retrievalService(player: Player): ItemRetrievalService {
    val raw = rawRetrievalServiceAttr(player)
    if (raw is ItemRetrievalService) {
        return raw
    }
    val service = ItemRetrievalService(player)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), ItemRetrievalService::class.java)
        service.adopt(typed)
    }
    player.attr[ITEM_RETRIEVAL_KEY] = service
    return service
}

/**
 * Untyped view of the value stored under [ITEM_RETRIEVAL_KEY]; see
 * SeedVaultKeys.rawSeedVaultAttr for why this exists.
 */
fun rawRetrievalServiceAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[ITEM_RETRIEVAL_KEY as AttributeKey<Any>]
}

/**
 * Read-only snapshot for OFFLINE scans over parser players
 * (LoginManager.deserializePlayerFromFile — WealthScanner and friends).
 * Parser players are Unsafe-allocated, so their transient attr map is null
 * and [retrievalService] cannot be used on them. Reads the raw
 * attrPersistence["item_retrieval"] shape; returns null when the save
 * carries none (offline callers should then fall back to the legacy
 * top-level "retrievalService" field for pre-migration saves). The snapshot
 * is unparented (transient player is null) — container reads only; never
 * store it in a live player's attr. Mirrors GravestoneKeys.scanGravestone.
 */
fun scanRetrievalService(parser: Player): ItemRetrievalService? {
    val raw = parser.attrPersistenceRaw?.get(ITEM_RETRIEVAL_KEY.persistenceKey) ?: return null
    val gson = LoginManager.gson.get()
    return gson.fromJson(gson.toJsonTree(raw), ItemRetrievalService::class.java)
}
