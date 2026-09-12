package org.jesse.game.content.dt2.statistics.whisperer

import org.jesse.game.content.dt2.area.DT2Module
import org.jesse.game.content.dt2.statistics.DT2Score
import org.jesse.game.content.dt2.statistics.DT2Scoreboard
import org.jesse.game.GameInterface.DUKE_SCOREBOARD
import org.jesse.game.GameInterface.WHISPERER_SCOREBOARD
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-17
 */
class WhispererScoreboard: DT2Scoreboard(
        scoreboard = WHISPERER_SCOREBOARD,
        bossName = "whisperer"
) {
    override fun getBossStatistics(): DT2Score =
        DT2Module.getWhispererStatistics(awakened)

    override fun getObjects(): Array<Any> =
        arrayOf(49474, 46092)
}