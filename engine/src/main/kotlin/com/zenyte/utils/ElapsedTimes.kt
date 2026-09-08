package com.zenyte.utils

import com.google.common.base.Stopwatch
import com.zenyte.logger.NearRealityLogger
import org.slf4j.Logger
import java.util.concurrent.TimeUnit

/**
 * @author Jire
 */
object ElapsedTimes {

    private val DEFAULT_UNIT = TimeUnit.MILLISECONDS

    private val log = NearRealityLogger.getLogger(ElapsedTimes::class.java)

    @JvmStatic
    @JvmOverloads
    fun logElapsed(
        log: Logger = ElapsedTimes.log,
        message: String,
        timeUnit: TimeUnit = DEFAULT_UNIT,
        runnable: Runnable
    ) = Runnable {
        val stopwatch = Stopwatch.createStarted()

        var throwable: Throwable? = null
        try {
            runnable.run()
        } catch (t: Throwable) {
            throwable = t
        }
        val elapsed = stopwatch.elapsed(timeUnit)
        val logMessage = "$message ($elapsed ${timeUnit.name.lowercase()})"

        if (throwable != null) log.error(logMessage, throwable)
        else log.info(logMessage)
    }

    @JvmStatic
    @JvmOverloads
    fun runLogElapsed(
        log: Logger = ElapsedTimes.log,
        message: String,
        timeUnit: TimeUnit = DEFAULT_UNIT,
        runnable: Runnable
    ) = logElapsed(log, message, timeUnit, runnable).run()

}