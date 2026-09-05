package com.near_reality.game.content.dt2.area

import com.google.common.eventbus.Subscribe
import com.near_reality.game.content.dt2.statistics.duke.DukeStatistics
import com.near_reality.game.content.dt2.statistics.leviathan.LeviathanStatistics
import com.near_reality.game.content.dt2.statistics.vardorvis.VardorvisStatistics
import com.near_reality.game.content.dt2.statistics.whisperer.WhispererStatistics
import com.zenyte.cores.CoresManager
import com.zenyte.logger.NearRealityLogger
import com.zenyte.plugins.events.ServerLaunchEvent
import com.zenyte.utils.TimeUnit

object DT2Module {

    internal val logger = NearRealityLogger.getLogger(this::class.java)

    var dukeStatistics: DukeStatistics? = null
    var vardStatistics: VardorvisStatistics? = null
    var whispererStatistics: WhispererStatistics? = null
    var leviathanStatistics: LeviathanStatistics? = null

    fun getDukeStatistics(awakened: Boolean = false): DukeStatistics {
        if (dukeStatistics == null) {
            dukeStatistics = DukeStatistics(awakened)
            return dukeStatistics!!
        }
        return dukeStatistics!!
    }

    fun getVardorvisStatistics(awakened: Boolean = false): VardorvisStatistics {
        if (vardStatistics == null) {
            vardStatistics = VardorvisStatistics(awakened)
            return vardStatistics!!
        }
        return vardStatistics!!
    }

    fun getWhispererStatistics(awakened: Boolean = false): WhispererStatistics {
        if (whispererStatistics == null) {
            whispererStatistics = WhispererStatistics(awakened)
            return whispererStatistics!!
        }
        return whispererStatistics!!
    }

    fun getLeviathanStatistics(awakened: Boolean = false): LeviathanStatistics {
        if (leviathanStatistics == null) {
            leviathanStatistics = LeviathanStatistics(awakened)
            return leviathanStatistics!!
        }
        return leviathanStatistics!!
    }


    @JvmStatic
    fun updateDukeStatistics(time: Long, awakened: Boolean = false) {
        getDukeStatistics(awakened).globalKillCount++
        val duration = System.currentTimeMillis() - time
        val timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration)
        val previousRecord = getDukeStatistics(awakened).globalBestKillTimeSeconds
        if (previousRecord == 0 || timeInSeconds < previousRecord)
            getDukeStatistics(awakened).globalBestKillTimeSeconds = timeInSeconds.toInt()
        CoresManager.slowExecutor.execute(DukeStatistics::write)
    }

    @JvmStatic
    fun updateVardorvisStatistics(time: Long, awakened: Boolean = false) {
        getVardorvisStatistics(awakened).globalKillCount++
        val duration = System.currentTimeMillis() - time
        val timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration)
        val previousRecord = getVardorvisStatistics(awakened).globalBestKillTimeSeconds
        if (previousRecord == 0 || timeInSeconds < previousRecord)
            getVardorvisStatistics(awakened).globalBestKillTimeSeconds = timeInSeconds.toInt()
        CoresManager.slowExecutor.execute(VardorvisStatistics::write)
    }

    @JvmStatic
    fun updateWhispererStatistics(time: Long, awakened: Boolean = false) {
        getWhispererStatistics(awakened).globalKillCount++
        val duration = System.currentTimeMillis() - time
        val timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration)
        val previousRecord = getWhispererStatistics(awakened).globalBestKillTimeSeconds
        if (previousRecord == 0 || timeInSeconds < previousRecord)
            getWhispererStatistics(awakened).globalBestKillTimeSeconds = timeInSeconds.toInt()
        CoresManager.slowExecutor.execute(WhispererStatistics::write)
    }

    @JvmStatic
    fun updateLeviathanStatistics(time: Long, awakened: Boolean = false) {
        getLeviathanStatistics(awakened).globalKillCount++
        val duration = System.currentTimeMillis() - time
        val timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration)
        val previousRecord = getLeviathanStatistics(awakened).globalBestKillTimeSeconds
        if (previousRecord == 0 || timeInSeconds < previousRecord)
            getLeviathanStatistics(awakened).globalBestKillTimeSeconds = timeInSeconds.toInt()
        CoresManager.slowExecutor.execute(LeviathanStatistics::write)
    }

    @JvmStatic
    @Subscribe
    fun onServerLaunch(event: ServerLaunchEvent) {
        DT2Commands.register()
    }
}