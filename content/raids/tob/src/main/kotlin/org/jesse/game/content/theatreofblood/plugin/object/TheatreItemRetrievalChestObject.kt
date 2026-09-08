package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.GameInterface
import org.jesse.game.content.ItemRetrievalService
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Jire
 */
class TheatreItemRetrievalChestObject : ObjectAction {

	override fun handleObjectAction(
		player: Player,
		obj: WorldObject,
		name: String,
		optionId: Int,
		option: String
	) {
		val service = player.retrievalService
		if (service.type != ItemRetrievalService.RetrievalServiceType.THEATRE_OF_BLOOD || service.container.size == 0) {
			player.dialogue {
				plain("The chest seems to be empty. If it did have any of your items, but<br><br>you died before collecting them, they'll now be lost.")
			}
		} else {
			GameInterface.ITEM_RETRIEVAL_SERVICE.open(player)
		}
	}

	override fun getObjects() = TheatreItemRetrievalChestObject.objects

	private companion object {

		val objects = arrayOf(CHEST_32656)

	}

}