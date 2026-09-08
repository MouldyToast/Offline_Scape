package org.jesse.tools.logging.file

import org.jesse.tools.logging.GameLogMessage
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi

@ExperimentalSerializationApi
@InternalSerializationApi
fun main() {
    val publicMessageLogs = readGameLogsFromTime<GameLogMessage.Message.Public>(Clock.System.now())
    publicMessageLogs.forEach {
        println(it)
    }
}
