package org.jesse.game.content.dt2.statistics.duke

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
data class DukeStatistics(var awakened: Boolean = false): DT2Score() {

    companion object : ScheduledExternalizable {
        override fun getLog(): Logger = DT2Module.logger

        override fun writeInterval(): Int = 0

        override fun read(reader: BufferedReader) {
            try {
                DT2Module.dukeStatistics = gson.fromJson(reader, object : TypeToken<DukeStatistics>(){}.type)
                log.info("DukeStatistics read from file.")
            }
            catch (e: Exception) {
                log.error("Error while reading DukeStatistics from file: ${e.message}")
                DT2Module.dukeStatistics = DukeStatistics()
            }
        }

        override fun ifFileNotFoundOnRead() {
            DT2Module.dukeStatistics = DukeStatistics()
            write()
        }

        override fun write() =
            out(gson.toJson(DT2Module.dukeStatistics))

        override fun path(): String =
            "data/dt2/${if (DukeStatistics().awakened) "awakened_" else ""}duke_statistics.json"
    }
}
