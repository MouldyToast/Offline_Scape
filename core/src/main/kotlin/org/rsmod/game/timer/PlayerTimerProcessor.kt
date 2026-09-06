package org.rsmod.game.timer

import com.zenyte.game.world.entity.player.Player
import org.rsmod.events.EventBus
import org.rsmod.game.events.PlayerTimerEvent

/**
 * The soft-timer half of OpenRune's PlayerTimerProcessor (api/
 * game-process/src/main/kotlin/org/rsmod/api/game/process/player/
 * PlayerTimerProcessor.kt), adapted: no DI/MapClock/ProtectedAccess —
 * the caller (Player.processEntity) passes the clock and bus; the
 * normal-timer half is not ported. Loop body is otherwise
 * upstream-shaped: expired-key snapshot -> publish Soft event -> re-arm
 * iff the handler didn't clear the timer. A handler throw propagates to
 * the caller (same behavior as EventBus.publish everywhere else): the
 * thrown timer stays expired and retries next tick, later timers are
 * skipped for the tick.
 */
public object PlayerTimerProcessor {
    @JvmStatic
    public fun processSoftTimers(player: Player, mapClock: Int, eventBus: EventBus) {
        val timers = player.softTimers
        if (!timers.isNotEmpty) {
            return
        }
        val expired = timers.expiredKeysBuffer
        expired.clear()
        for (entry in timers) {
            val expiry = timers.extractExpiry(entry.longValue)
            if (mapClock >= expiry) {
                expired.add(entry.shortKey)
            }
        }
        for (timerType in expired) {
            eventBus.publish(PlayerTimerEvent.Soft(player, timerType.toInt()))
            val packedValue = timers[timerType]
            if (packedValue != null) {
                val interval = timers.extractInterval(packedValue)
                timers.put(timerType, mapClock = mapClock, interval = interval)
            }
        }
    }
}
