package org.jesse.game.content.scoreboard.impl

import com.google.gson.reflect.TypeToken
import org.jesse.game.content.scoreboard.Score
import org.jesse.game.content.scoreboard.Scoreboard
import org.jesse.game.content.scoreboard.ScoreboardModule
import org.jesse.cores.ScheduledExternalizable
import org.jesse.game.GameInterface
import org.jesse.game.world.entity.player.Player
import org.jesse.game.obj.ids.*
import org.slf4j.Logger
import java.io.BufferedReader

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-17
 */
class PhantomMuspahStatistics : Scoreboard() {

    companion object : ScheduledExternalizable {
        override fun getLog(): Logger = ScoreboardModule.logger

        override fun writeInterval(): Int = 1

        override fun read(reader: BufferedReader) {
            try {
                ScoreboardModule.phantomMuspahStatistics = gson.fromJson(reader, object : TypeToken<PhantomMuspahStatistics>(){}.type)
                log.info("Phantom Muspah Statistics loaded from file")
            }
            catch (e: Exception) {
                log.error("Error while reading Statistics from file: ${e.message}")
                ScoreboardModule.phantomMuspahStatistics = PhantomMuspahStatistics()
            }
        }

        override fun ifFileNotFoundOnRead() = write()

        override fun write() =
            out(gson.toJson(ScoreboardModule.phantomMuspahStatistics))

        override fun path(): String =
            "data/scoreboard/${ScoreboardModule.phantomMuspahStatistics.getBossName().replace(" ", "_")}_statistics.json"
    }

    override fun getScoreboardInterface(): GameInterface =
        GameInterface.PHANTOM_MUSPAH_SCOREBOARD

    override fun getBossName(): String =
        "Phantom Muspah"

    override fun getStatistics(): Score =
        ScoreboardModule.phantomMuspahStatistics

    override fun getObjects(): Array<Any> =
        arrayOf(PHANTOM_MUSPAH_SCOREBOARD)
}
