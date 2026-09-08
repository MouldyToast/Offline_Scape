package org.jesse.game.content.boss.nex.`object`.actions

import org.jesse.game.content.boss.nex.NexModule
import org.jesse.game.content.boss.nex.nexBestTime
import org.jesse.game.content.boss.nex.nexDeathCount
import org.jesse.game.content.boss.nex.nexKillCount
import org.jesse.game.GameInterface.NEX_STATS
import org.jesse.game.world.entity.player.BossTimer
import org.jesse.scripts.`object`.actions.ObjectActionScript
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.*

class AncientScoreboardObjectaction : ObjectActionScript() {

    init {
        SCOREBOARD_42936 {
            when(option) {
                "Read" -> {
                    player.interfaceHandler.sendInterface(NEX_STATS)
                    player.packetDispatcher.run {
                        sendComponentText(NEX_STATS, 9, player.nexKillCount)
                        sendComponentText(NEX_STATS, 11, player.nexDeathCount)
                        sendComponentText(NEX_STATS, 13, player.nexBestTime)
                        sendComponentText(NEX_STATS, 15, NexModule.statistics.globalKillCount)
                        sendComponentText(NEX_STATS, 17, NexModule.statistics.globalDeathCount)
                        sendComponentText(NEX_STATS, 19, BossTimer.formatBestTime(NexModule.statistics.globalBestKillTimeSeconds))
                    }
                }
            }
        }
    }
}
