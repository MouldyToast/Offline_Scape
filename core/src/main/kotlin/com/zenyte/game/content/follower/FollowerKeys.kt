@file:JvmName("FollowerKeys")

package com.zenyte.game.content.follower

import com.zenyte.game.world.entity.player.Player
import org.rsmod.api.attr.AttributeKey

/**
 * Transient session key for the player's follower (pet). No persistenceKey:
 * the follower was never persisted directly — the plain-int Player.petId is
 * the persisted datum, and the onLobbyClose spawn path rebuilds the Follower
 * from it. Null means no pet is out.
 */
@JvmField
val FOLLOWER_KEY: AttributeKey<Follower> = AttributeKey()

/**
 * Nullable accessor mirroring the legacy transient Player.follower field.
 * Java callers use FollowerKeys.follower(player).
 */
fun Player.follower(): Follower? = attr[FOLLOWER_KEY]

/**
 * Replaces Player.setFollower with identical semantics: clearing an existing
 * follower finishes it, petId and varp 447 are maintained, and a new follower
 * is spawned on assignment.
 */
fun Player.setFollower(follower: Follower?) {
    val current = attr[FOLLOWER_KEY]
    if (current != null && follower == null) {
        // Legacy early-return branch: clearing an existing follower finishes
        // it and resets petId WITHOUT touching varp 447 — preserved as-is.
        current.finish()
        petId = -1
        attr.remove(FOLLOWER_KEY)
        return
    }
    if (follower == null) {
        attr.remove(FOLLOWER_KEY)
        petId = -1
    } else {
        attr[FOLLOWER_KEY] = follower
        petId = follower.id
        follower.spawn()
    }
    varManager.sendVar(447, follower?.index ?: -1)
}
