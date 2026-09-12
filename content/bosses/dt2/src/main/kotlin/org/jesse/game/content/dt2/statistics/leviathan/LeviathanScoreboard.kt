package org.jesse.game.content.dt2.statistics.leviathan

import org.jesse.game.content.dt2.area.DT2Module
import org.jesse.game.content.dt2.statistics.DT2Score
import org.jesse.game.content.dt2.statistics.DT2Scoreboard
import org.jesse.game.GameInterface.DUKE_SCOREBOARD
import org.jesse.game.GameInterface.LEVIATHAN_SCOREBOARD
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

class LeviathanScoreboard: DT2Scoreboard(
        scoreboard = LEVIATHAN_SCOREBOARD,
        bossName = "leviathan"
) {
    override fun getBossStatistics(): DT2Score =
        DT2Module.getLeviathanStatistics(awakened)

    override fun getObjects(): Array<Any> =
        arrayOf(49475)
}