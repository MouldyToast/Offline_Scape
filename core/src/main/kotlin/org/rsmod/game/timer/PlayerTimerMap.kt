package org.rsmod.game.timer

import it.unimi.dsi.fastutil.objects.ObjectIterator
import it.unimi.dsi.fastutil.shorts.Short2LongLinkedOpenHashMap
import it.unimi.dsi.fastutil.shorts.Short2LongMap
import it.unimi.dsi.fastutil.shorts.ShortArraySet

/**
 * Copied from OpenRune-Server engine/game/src/main/kotlin/org/rsmod/game/
 * timer/PlayerTimerMap.kt. DIVERGENCE from OpenRune (pre-RSCM port):
 * schedule/remove take a Short timer id directly (upstream resolves a
 * String through RSCM, arriving with the RSCM phase), and the
 * @InternalApi/@OptIn annotations are dropped (org.rsmod.annotations is
 * not ported). Everything else is byte-equal to upstream.
 */
public class PlayerTimerMap(
    private val timers: Short2LongLinkedOpenHashMap = Short2LongLinkedOpenHashMap()
) : Iterable<Short2LongMap.Entry> {
    public val expiredKeysBuffer: ShortArraySet = ShortArraySet()

    public val isNotEmpty: Boolean
        get() = timers.isNotEmpty()

    public fun remove(timer: Short) {
        timers.remove(timer)
    }

    public fun schedule(timer: Short, mapClock: Int, interval: Int) {
        put(timer, mapClock, interval)
    }

    public fun put(timer: Short, mapClock: Int, interval: Int) {
        val expiry = mapClock + interval
        timers[timer] = (expiry.toLong() shl 32) or interval.toLong()
    }

    public fun extractExpiry(packed: Long): Int = (packed shr 32).toInt()

    public fun extractInterval(packed: Long): Int = packed.toInt()

    public operator fun get(timerType: Short): Long? {
        val value = timers.get(timerType)
        return value.takeIf { it != timers.defaultReturnValue() }
    }

    override fun iterator(): ObjectIterator<Short2LongMap.Entry> {
        return timers.short2LongEntrySet().fastIterator()
    }

    override fun toString(): String = timers.toString()
}
