package com.near_reality.game.content.dt2.statistics.vardorvis

import com.near_reality.game.content.dt2.area.DT2Module
import com.near_reality.game.content.dt2.statistics.DT2Score
import com.near_reality.game.content.dt2.statistics.DT2Scoreboard
import com.zenyte.game.GameInterface.DUKE_SCOREBOARD
import com.zenyte.game.GameInterface.VARDORVIS_SCOREBOARD
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

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