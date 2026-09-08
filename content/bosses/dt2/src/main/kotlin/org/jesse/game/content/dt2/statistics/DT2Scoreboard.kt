package org.jesse.game.content.dt2.statistics

import org.jesse.game.content.dt2.npc.*
import org.jesse.game.GameInterface
import org.jesse.game.world.entity.player.BossTimer
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject
import org.jesse.utils.TextUtils

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-18
 */
abstract class DT2Scoreboard(
    val scoreboard: GameInterface,
    val bossName: String,
    var awakened: Boolean = false
): ObjectAction {

    abstract fun getBossStatistics(): DT2Score

    override fun handleObjectAction(player: Player?, `object`: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return; `object` ?: return; name ?: return; option ?: return
        when(option){
            "Read" -> { player.openScoreboard(awakened = false) }
            "Read (Awakened)" -> { player.openScoreboard(awakened = true) }
        }
    }

    private fun Player.openScoreboard(awakened: Boolean, board: DT2Scoreboard = this@DT2Scoreboard) {
        interfaceHandler.sendInterface(scoreboard)
        board.awakened = awakened;
        val name = "${if (awakened) "awakened " else ""}${bossName}"
        with(packetDispatcher) {
            sendComponentText(scoreboard, 6, "${TextUtils.capitalizeFirstCharacter(name)} statistics")
            sendComponentText(scoreboard, 9, notificationSettings.getKillcount(name))
            sendComponentText(scoreboard, 11, getDeathsToBoss(awakened).toString())
            sendComponentText(scoreboard, 13, bossTimer.personalBest(name))
            sendComponentText(scoreboard, 15, getBossStatistics().globalKillCount)
            sendComponentText(scoreboard, 17, getBossStatistics().globalDeathCount)
            sendComponentText(scoreboard, 19, BossTimer.formatBestTime(getBossStatistics().globalBestKillTimeSeconds))
        }
    }

    private fun Player.getDeathsToBoss(awakened: Boolean): Int {
        val name = if (awakened) "awakened " else "" + bossName
        return if (name.contains("duke"))
            if (name.contains("awakened"))
                deathsToAwakenedDuke else deathsToDuke
        else if (name.contains("whisperer"))
            if (name.contains("awakened"))
                deathsToAwakenedWhisperer else deathsToWhisperer
        else if (name.contains("leviathan"))
            if (name.contains("awakened"))
                deathsToAwakenedLeviathan else deathsToLeviathan
        else if (name.contains("vardorvis"))
            if (name.contains("awakened"))
                deathsToAwakenedVardorvis else deathsToVardorvis
        else
            0
    }
}