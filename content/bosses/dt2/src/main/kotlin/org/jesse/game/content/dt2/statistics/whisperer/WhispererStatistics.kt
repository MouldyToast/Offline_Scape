package org.jesse.game.content.dt2.statistics.whisperer

import com.google.gson.reflect.TypeToken
import org.jesse.game.content.dt2.area.DT2Module
import org.jesse.game.content.dt2.statistics.DT2Score
import org.jesse.cores.ScheduledExternalizable
import org.slf4j.Logger
import java.io.BufferedReader

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-17
 */
data class WhispererStatistics(var awakened: Boolean = false): DT2Score() {

    companion object : ScheduledExternalizable {
        override fun getLog(): Logger = DT2Module.logger

        override fun writeInterval(): Int = 0

        override fun read(reader: BufferedReader) {
            try {
                DT2Module.whispererStatistics = gson.fromJson(reader, object : TypeToken<WhispererStatistics>(){}.type)
                log.info("WhispererStatistics read from file.")
            }
            catch (e: Exception) {
                log.error("Error while reading WhispererStatistics from file: ${e.message}")
                DT2Module.whispererStatistics = WhispererStatistics()
            }
        }

        override fun ifFileNotFoundOnRead() {
            DT2Module.whispererStatistics = WhispererStatistics()
            write()
        }

        override fun write() =
            out(gson.toJson(DT2Module.whispererStatistics))

        override fun path(): String =
            "data/dt2/${if (WhispererStatistics().awakened) "awakened_" else ""}whisperer_statistics.json"
    }
}
