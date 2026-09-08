package org.jesse.logger

import java.io.PrintStream

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-14
 */
object NearRealityPrintStream: PrintStream(System.err) {

    private val logger = NearRealityLogger.getLogger(NearRealityPrintStream::class.java)

    @JvmStatic
    fun getErrorStream(): PrintStream {
        // TODO: evaluate the error coming through

        return PrintStream(System.err)
    }

    fun handleStackTrace(throwable: Throwable) {
        logger.error(throwable.message, throwable)
    }
}
