@file:JvmName("LootkeySettingsKeys")

package com.zenyte.game.content.lootkeys

import com.google.common.eventbus.Subscribe
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerLoginEvent

/**
 * Persisted loot key settings. Saved under attrPersistence["lootkey_settings"]
 * as the [LootkeySettings] instance itself. The class is a pure data holder
 * with no transient back-refs, so a Gson-rehydrated snapshot is stored
 * directly — no copyFrom step. NULLABLE BY DESIGN: absent means loot keys
 * were never enabled for this player, exactly matching the legacy
 * null-initialized field.
 */
@JvmField
val LOOTKEY_SETTINGS_KEY: AttributeKey<LootkeySettings> = AttributeKey(persistenceKey = "lootkey_settings")

/**
 * Nullable typed accessor — mirrors the legacy null-field semantics: no
 * default construction, null until something calls [setLootkeySettings].
 * Rehydrates the raw persistence map left by AttributeMap.putAllFromPersistence
 * if this is the first access after a load.
 */
fun Player.lootkeySettings(): LootkeySettings? {
    val raw = rawLootkeySettingsAttr(this) ?: return null
    if (raw is LootkeySettings) {
        return raw
    }
    val gson = LoginManager.gson.get()
    val typed = gson.fromJson(gson.toJsonTree(raw), LootkeySettings::class.java) ?: return null
    attr[LOOTKEY_SETTINGS_KEY] = typed
    return typed
}

/**
 * Typed setter — replaces the legacy Player.setLootkeySettings. Null removes
 * the key (loot keys disabled; the attrPersistence entry disappears on the
 * next save).
 */
fun Player.setLootkeySettings(settings: LootkeySettings?) {
    if (settings == null) {
        attr.remove(LOOTKEY_SETTINGS_KEY)
    } else {
        attr[LOOTKEY_SETTINGS_KEY] = settings
    }
}

/**
 * Untyped view of the value stored under [LOOTKEY_SETTINGS_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed
 * [LootkeySettings] after, or null. Exists because AttributeMap.get's
 * unchecked cast makes a typed read unsafe before rehydration.
 */
fun rawLootkeySettingsAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[LOOTKEY_SETTINGS_KEY as AttributeKey<Any>]
}

/** T2-a: loot-chest interface resend at login (was in the login block; guards mirrored verbatim). */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    event.worldThread.eventBus.subscribeUnbound(PlayerLoginEvent::class.java) {
        val settings = player.lootkeySettings()
        if (settings?.currentItemsInChest?.isEmpty() == false) {
            LootkeySettings.sendOpenChest(player)
        }
    }
}
