package org.jesse.game.content.dt2.statistics.duke

import org.jesse.game.content.dt2.area.DT2Module
import org.jesse.game.content.dt2.statistics.DT2Score
import org.jesse.game.content.dt2.statistics.DT2Scoreboard
import org.jesse.game.GameInterface.DUKE_SCOREBOARD
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

class DukeScoreboard : DT2Scoreboard(
        scoreboard = DUKE_SCOREBOARD,
        bossName = "duke sucellus"
) {

    override fun getBossStatistics(): DT2Score =
        DT2Module.getDukeStatistics(awakened)

    override fun getObjects(): Array<Any> =
        arrayOf(46091)
}