package com.near_reality.threads

import java.util.concurrent.locks.LockSupport
import kotlin.math.max

/**
 * @author Jire
 */
object Threads {

    @JvmStatic
    @JvmOverloads
    fun preciseSleep(
        totalNanos: Long,
        busyWaitNanos: Long = 1_000_000 // 1ms of busy-waiting
    ) {
        val start = System.nanoTime()
        val sleepUntil = start + totalNanos - busyWaitNanos

        // Phase 1: Passive sleep (park)
        while (true) {
            val now = System.nanoTime()
            val remaining = sleepUntil - now
            if (remaining <= 0) break

            if (remaining > 1_000_000) {
                LockSupport.parkNanos(max(0, remaining - 500_000)) // park with headroom
            } else {
                Thread.yield() // let scheduler breathe
            }
        }

        // Phase 2: Spin-wait for precision
        while (System.nanoTime() - start < totalNanos) {
            Thread.onSpinWait()
        }
    }

}
