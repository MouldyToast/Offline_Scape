@file:JvmName("DuelKeys")

package com.zenyte.game.content.minigame.duelarena

import com.zenyte.game.world.entity.player.Player
import org.rsmod.api.attr.AttributeKey

/**
 * Transient duel session state. No persistenceKey — the duel never
 * persisted (the legacy Player field was transient) and still doesn't.
 * Null means the player is not in (or arranging) a duel.
 */
@JvmField
val DUEL_KEY: AttributeKey<Duel> = AttributeKey()

/**
 * The player's current duel, perspective-normalized. The two duelists
 * share ONE Duel instance; like the legacy Player.getDuel(), reading this
 * property re-orients the shared instance toward the caller (swapping
 * player/opponent) before returning it. Setting null clears the key.
 * Java callers use DuelKeys.getDuel(player) / DuelKeys.setDuel(player, d).
 */
var Player.duel: Duel?
    get() {
        val duel = attr[DUEL_KEY] ?: return null
        if (duel.player != this) {
            val opponent = duel.player
            duel.player = this
            duel.opponent = opponent
        }
        return duel
    }
    set(value) {
        if (value == null) {
            attr.remove(DUEL_KEY)
        } else {
            attr[DUEL_KEY] = value
        }
    }
