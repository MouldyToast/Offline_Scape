package org.jesse.game.content.dt2.statistics.vardorvis

import org.jesse.game.content.dt2.area.DT2Module
import org.jesse.game.content.dt2.statistics.DT2Score
import org.jesse.game.content.dt2.statistics.DT2Scoreboard
import org.jesse.game.GameInterface.DUKE_SCOREBOARD
import org.jesse.game.GameInterface.VARDORVIS_SCOREBOARD
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
class VardorvisScoreboard: DT2Scoreboard(
        scoreboard = VARDORVIS_SCOREBOARD,
        bossName = "vardorvis"
) {
    override fun getBossStatistics(): DT2Score =
        DT2Module.getVardorvisStatistics(awakened)

    override fun getObjects(): Array<Any> =
        arrayOf(47598)
}