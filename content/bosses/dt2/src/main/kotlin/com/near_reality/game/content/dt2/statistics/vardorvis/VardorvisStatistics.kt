package com.near_reality.game.content.dt2.statistics.vardorvis

import com.google.gson.reflect.TypeToken
import com.near_reality.game.content.dt2.area.DT2Module
import com.near_reality.game.content.dt2.statistics.DT2Score
import com.near_reality.game.content.dt2.statistics.whisperer.WhispererStatistics
import com.near_reality.game.content.dt2.statistics.whisperer.WhispererStatistics.Companion
import com.zenyte.cores.ScheduledExternalizable
import org.slf4j.Logger
import java.io.BufferedReader

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-17
 */
data class VardorvisStatistics(var awakened: Boolean = false): DT2Score() {

    companion object : ScheduledExternalizable {
        override fun getLog(): Logger = DT2Module.logger

        override fun writeInterval(): Int = 0

        override fun read(reader: BufferedReader) {
            try {
                DT2Module.vardStatistics = gson.fromJson(reader, object : TypeToken<VardorvisStatistics>(){}.type)
                log.info("VardorvisStatistics read from file.")
            }
            catch (e: Exception) {
                log.error("Error while reading VardorvisStatistics from file: ${e.message}")
                DT2Module.vardStatistics = VardorvisStatistics()
            }
        }

        override fun ifFileNotFoundOnRead() {
            DT2Module.vardStatistics = VardorvisStatistics()
            write()
        }

        override fun write() =
            out(gson.toJson(DT2Module.vardStatistics))

        override fun path(): String =
            "data/dt2/${if (VardorvisStatistics().awakened) "awakened_" else ""}vardorvis_statistics.json"
    }
}
