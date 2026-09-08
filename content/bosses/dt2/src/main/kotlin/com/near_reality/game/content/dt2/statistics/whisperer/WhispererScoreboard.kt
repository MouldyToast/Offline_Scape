package com.near_reality.game.content.dt2.statistics.whisperer

import com.near_reality.game.content.dt2.area.DT2Module
import com.near_reality.game.content.dt2.statistics.DT2Score
import com.near_reality.game.content.dt2.statistics.DT2Scoreboard
import com.zenyte.game.GameInterface.DUKE_SCOREBOARD
import com.zenyte.game.GameInterface.WHISPERER_SCOREBOARD
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.obj.ids.*
import com.zenyte.game.world.`object`.WorldObject

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
        arrayOf(47581)
}