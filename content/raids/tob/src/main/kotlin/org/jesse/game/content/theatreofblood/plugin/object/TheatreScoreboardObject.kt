package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.GameInterface
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

class TheatreScoreboardObject : ObjectAction {

	override fun handleObjectAction(
		player: Player,
		obj: WorldObject,
		name: String,
		optionId: Int,
		option: String
	) {
		GameInterface.TOB_STATS.open(player)
	}

	override fun getObjects() = TheatreScoreboardObject.objects

	private companion object {

		val objects = arrayOf(SCOREBOARD_32987)

	}

}
