package com.near_reality.game.content.scoreboard

import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.player.BossTimer
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject
import java.util.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-18
 */
abstract class Scoreboard: ObjectAction, Score() {

    abstract fun getScoreboardInterface(): GameInterface
    abstract fun getBossName(): String
    abstract fun getStatistics(): Score

    /**
     * Opens the player's scoreboard interface and displays various statistics related to the boss fight.
     *
     * This function retrieves the scoreboard interface for the player and updates
     * its components with data such as the player's kill count, death count to the boss,
     * personal best time, global kill counts, and death counts, and the globally best kill time.
     * If the interface is not available, the operation will not proceed.
     *
     * The method utilizes `sendComponentText` to populate the scoreboard interface fields
     * with the respective statistics.
     */
    private fun Player.openScoreboard() {
        interfaceHandler.sendInterface(getScoreboardInterface())
        with(packetDispatcher) {
            sendComponentText(getScoreboardInterface(), 9, notificationSettings.getKillcount(getBossName()))
            sendComponentText(getScoreboardInterface(), 11, getDeathsToBoss().toString())
            sendComponentText(getScoreboardInterface(), 13, bossTimer.personalBest(getBossName()))
            sendComponentText(getScoreboardInterface(), 15, getStatistics().globalKillCount)
            sendComponentText(getScoreboardInterface(), 17, getStatistics().globalDeathCount)
            sendComponentText(getScoreboardInterface(), 19, BossTimer.formatBestTime(getStatistics().globalBestKillTimeSeconds))
        }
    }

    private fun Player.getDeathsToBoss(): Int {
        return when(getBossName().lowercase(Locale.getDefault())) {
            "araxxor" -> deathsToAraxxor
            "phantom muspah" -> deathsToPhantomMuspah
            else -> 0
        }
    }

    override fun handleObjectAction(player: Player?, `object`: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return; option ?: return
        when(option){
            "Read" -> { player.openScoreboard() }
        }
    }
}