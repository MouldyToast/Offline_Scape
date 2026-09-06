@file:JvmName("AchievementDiariesKeys")

package com.zenyte.game.content.achievementdiary

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey

/**
 * Persisted achievement-diary state. Saved under
 * attrPersistence["achievement_diaries"] as the [AchievementDiaries] instance
 * itself; Gson skips the transient player back-ref, so the per-objective
 * progress map and the pending-reward maps round-trip.
 */
@JvmField
val ACHIEVEMENT_DIARIES_KEY: AttributeKey<AchievementDiaries> = AttributeKey(persistenceKey = "achievement_diaries")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new AchievementDiaries(player)) on first access, and rehydrates the raw
 * persistence map left by AttributeMap.putAllFromPersistence if this is the
 * first access after a load. A Gson-rehydrated snapshot is never stored
 * directly: its transient player back-ref is null — state is copied into a
 * properly-parented instance via copyFrom (the exact putAll body of the
 * legacy setFields initialize). Never returns null.
 */
fun Player.achievementDiaries(): AchievementDiaries {
    val raw = rawAchievementDiariesAttr(this)
    if (raw is AchievementDiaries) {
        return raw
    }
    val diaries = AchievementDiaries(this)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), AchievementDiaries::class.java)
        if (typed != null) {
            diaries.copyFrom(typed)
        }
    }
    attr[ACHIEVEMENT_DIARIES_KEY] = diaries
    return diaries
}

/**
 * Untyped view of the value stored under [ACHIEVEMENT_DIARIES_KEY]: the raw
 * Map produced by putAllFromPersistence before rehydration, the typed
 * [AchievementDiaries] after, or null. Exists because AttributeMap.get's
 * unchecked cast makes a typed read unsafe before rehydration.
 */
fun rawAchievementDiariesAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[ACHIEVEMENT_DIARIES_KEY as AttributeKey<Any>]
}
