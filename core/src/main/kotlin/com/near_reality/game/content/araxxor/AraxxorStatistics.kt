package com.near_reality.game.content.araxxor

import com.google.gson.reflect.TypeToken
import com.near_reality.game.content.scoreboard.Score
import com.near_reality.game.content.scoreboard.Scoreboard
import com.near_reality.game.content.scoreboard.ScoreboardModule
import com.zenyte.cores.ScheduledExternalizable
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.player.Player
import org.slf4j.Logger
import java.io.BufferedReader

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-17
 */
class AraxxorStatistics : Scoreboard() {

    companion object : ScheduledExternalizable {
        override fun getLog(): Logger = ScoreboardModule.logger

        override fun writeInterval(): Int = 1

        override fun read(reader: BufferedReader) {
            try {
                ScoreboardModule.araxxorStatistics = gson.fromJson(reader, object : TypeToken<AraxxorStatistics>(){}.type)
            }
            catch (e: Exception) {
                log.error("Error while reading Statistics from file: ${e.message}")
                ScoreboardModule.araxxorStatistics = AraxxorStatistics()
            }
        }

        override fun ifFileNotFoundOnRead() = write()

        override fun write() =
            out(gson.toJson(ScoreboardModule.araxxorStatistics))

        override fun path(): String =
            "data/scoreboard/araxxor_statistics.json"
    }

    override fun getScoreboardInterface(): GameInterface =
        GameInterface.ARAXXOR_SCOREBOARD

    override fun getBossName(): String =
        "Araxxor"

    override fun getStatistics(): Score =
        ScoreboardModule.araxxorStatistics

    override fun getObjects(): Array<Any> =
        arrayOf(54149)
}
