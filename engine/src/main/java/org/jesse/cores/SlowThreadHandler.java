package org.jesse.cores;


import org.jesse.logger.NearRealityLogger;
import org.jesse.logger.NearRealityPrintStream;
import org.slf4j.Logger;

/**
 * A hidden exception handler for logging silent thread death from the slow executor pool.
 * @author David O'Neill (dlo3)
 */
public final class SlowThreadHandler implements Thread.UncaughtExceptionHandler {

	private static final Logger logger = NearRealityLogger.getLogger(SlowThreadHandler.class);

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        logger.error("({}, slow pool) - Printing trace", thread.getName());
        throwable.printStackTrace(NearRealityPrintStream.getErrorStream());
    }

}
