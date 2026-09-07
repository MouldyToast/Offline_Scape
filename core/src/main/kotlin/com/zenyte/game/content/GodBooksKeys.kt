@file:JvmName("GodBooksKeys")

package com.zenyte.game.content

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted god-book page/claim state. Saved under
 * attrPersistence["god_books"] as the [GodBooks] instance itself.
 */
@JvmField
val GOD_BOOKS_KEY: AttributeKey<GodBooks> = AttributeKey(persistenceKey = "god_books")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new GodBooks()) on first access, and rehydrates the raw persistence map
 * left by AttributeMap.putAllFromPersistence if this is the first access
 * after a load. Never returns null.
 */
fun godBooks(player: Player): GodBooks {
    val raw = rawGodBooksAttr(player)
    if (raw is GodBooks) {
        return raw
    }
    val books = GodBooks()
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), GodBooks::class.java)
        books.adopt(typed)
    }
    player.attr[GOD_BOOKS_KEY] = books
    return books
}

/**
 * Untyped view of the value stored under [GOD_BOOKS_KEY]; see
 * SeedVaultKeys.rawSeedVaultAttr for why this exists.
 */
fun rawGodBooksAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[GOD_BOOKS_KEY as AttributeKey<Any>]
}
