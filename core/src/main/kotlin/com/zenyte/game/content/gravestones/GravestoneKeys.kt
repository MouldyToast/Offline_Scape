@file:JvmName("GravestoneKeys")

package com.zenyte.game.content.gravestones

import com.google.common.eventbus.Subscribe
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.login.LoginManager
import org.rsmod.api.attr.AttributeKey
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerLoginEvent
import org.rsmod.game.events.PlayerTimerEvent
import org.rsmod.game.timer.PlayerTimers

/**
 * Persisted gravestone state. Saved under attrPersistence["gravestone"] as the
 * [Gravestone] instance itself; Gson skips the transient player back-ref, the
 * transient placed [GravestoneNPC] and the transient tick interval, so only
 * gravestoneLocation, the retrieval container and coinsInCoffer round-trip.
 * The placed gravestone NPC is re-spawned at login by the existing LoginEvent
 * subscriber from the persistent timer varbit.
 */
@JvmField
val GRAVESTONE_KEY: AttributeKey<Gravestone> = AttributeKey(persistenceKey = "gravestone")

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new Gravestone(player)) on first access, and rehydrates the raw persistence
 * map left by AttributeMap.putAllFromPersistence if this is the first access
 * after a load. A Gson-rehydrated snapshot is never stored directly: its
 * transient player back-ref is null — state is copied into a properly-parented
 * instance via copyFrom instead. Declared as an extension so Kotlin call sites
 * keep their implicit-receiver ergonomics; Java sees the same
 * GravestoneKeys.gravestone(player) static facade as the other D/E-series
 * keys. Never returns null.
 */
fun Player.gravestone(): Gravestone {
    val raw = rawGravestoneAttr(this)
    if (raw is Gravestone) {
        return raw
    }
    val gravestone = Gravestone(this)
    if (raw != null) {
        val gson = LoginManager.gson.get()
        val typed = gson.fromJson(gson.toJsonTree(raw), Gravestone::class.java)
        if (typed != null) {
            gravestone.copyFrom(typed)
        }
    }
    attr[GRAVESTONE_KEY] = gravestone
    return gravestone
}

/**
 * Untyped view of the value stored under [GRAVESTONE_KEY]: the raw Map
 * produced by putAllFromPersistence before rehydration, the typed [Gravestone]
 * after, or null. Exists because AttributeMap.get's unchecked cast makes a
 * typed read unsafe before rehydration.
 */
fun rawGravestoneAttr(player: Player): Any? {
    @Suppress("UNCHECKED_CAST")
    return player.attr[GRAVESTONE_KEY as AttributeKey<Any>]
}

/**
 * Read-only snapshot for OFFLINE scans over parser players
 * (LoginManager.deserializePlayerFromFile — WealthScanner and friends).
 * Parser players are Unsafe-allocated, so their transient attr map is null
 * and [gravestone] cannot be used on them. Reads the raw
 * attrPersistence["gravestone"] shape; returns null when the save carries
 * none. The snapshot is unparented (transient player is null) — container
 * reads only; never store it in a live player's attr.
 */
fun scanGravestone(parser: Player): Gravestone? {
    val raw = parser.attrPersistenceRaw?.get(GRAVESTONE_KEY.persistenceKey) ?: return null
    val gson = LoginManager.gson.get()
    return gson.fromJson(gson.toJsonTree(raw), Gravestone::class.java)
}

/**
 * T2-a: gravestone countdown on a soft timer (id reserved in T3.2).
 * The old inline call had its own try/catch inside processEntity —
 * preserved here so a gravestone throw cannot skip later timers.
 */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    val bus = event.worldThread.eventBus
    bus.subscribeUnbound(PlayerLoginEvent::class.java) {
        player.softTimers.schedule(PlayerTimers.GRAVESTONE, WorldThread.getCurrentCycle().toInt(), interval = 1)
    }
    bus.subscribeKeyed(PlayerTimerEvent.Soft::class.java, PlayerTimers.GRAVESTONE.toLong()) {
        try {
            player.gravestone().process()
        } catch (e: Exception) {
            org.slf4j.LoggerFactory.getLogger("GravestoneKeys").error("", e)
        }
    }
}
