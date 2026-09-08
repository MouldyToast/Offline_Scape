package org.jesse.game.content.scoreboard

import org.jesse.game.content.araxxor.AraxxorStatistics
import org.jesse.game.content.scoreboard.impl.PhantomMuspahStatistics
import org.jesse.cores.CoresManager
import org.jesse.logger.NearRealityLogger
import org.jesse.utils.TimeUnit

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-18
 */
object ScoreboardModule {

    internal val logger = NearRealityLogger.getLogger(this::class.java)

    var araxxorStatistics = AraxxorStatistics()
    var phantomMuspahStatistics = PhantomMuspahStatistics()

    @JvmStatic
    fun updateAraxxorStatistics(time: Long) {
        araxxorStatistics.updateStatistics(time)
        CoresManager.slowExecutor.execute(AraxxorStatistics::write)
    }

    @JvmStatic
    fun updatePhantomMuspahStatistics(time: Long) {
        phantomMuspahStatistics.updateStatistics(time)
        CoresManager.slowExecutor.execute(PhantomMuspahStatistics::write)
    }

}