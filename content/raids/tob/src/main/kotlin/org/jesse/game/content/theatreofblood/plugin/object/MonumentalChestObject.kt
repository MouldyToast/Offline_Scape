package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.GameInterface
import org.jesse.game.content.theatreofblood.TheatreOfBloodRaid
import org.jesse.game.content.theatreofblood.VerSinhazaArea
import org.jesse.game.content.theatreofblood.room.reward.RewardRoom
import org.jesse.game.content.theatreofblood.tobStats
import org.jesse.game.content.theatreofblood.tobStatsHard
import org.jesse.game.model.item.enums.RareDrop
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Colour
import org.jesse.game.world.World
import org.jesse.game.world.broadcasts.BroadcastType
import org.jesse.game.world.broadcasts.WorldBroadcasts
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.Container
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

class MonumentalChestObject : ObjectAction {

	override fun handleObjectAction(
		player: Player,
		obj: WorldObject,
		name: String,
		optionId: Int,
		option: String
	) {
		val party = VerSinhazaArea.getParty(player) ?: return
		val raid = party.raid ?: return
		val room = player.area as? RewardRoom
		if (room != null) {
			val index = party.players.indexOf(player)
			val chestCoords = RewardRoom.chestCoords[index]
			val location = room.getBaseLocation(chestCoords[0], chestCoords[1])
			if (!obj.matches(location)) {
				player.sendMessage("This isn't your chest!")
				player.sendDeveloperMessage("obj=${obj}, loc=$location")
				return
			}

			val rewards = room.playersLoot[player]
			if (rewards != null) {
				if (openChests.contains(obj.id)) {
					handleOpen(player, rewards)
					return
				}

				handleClosed(player, obj, rewards, raid)
			}
		}
	}

	private fun handleClosed(player: Player, obj: WorldObject, rewards: Container, raid: TheatreOfBloodRaid) {
		player.animation = Animation(536)
		WorldTasksManager.schedule {
			val jackpot = RewardRoom.checkForJackpot(rewards)
			val transformId = if (jackpot) OPEN_CHEST_PURPLE else OPEN_CHEST_NORMAL
			World.replaceObject(obj, obj.transform(transformId))
			for (item in rewards.items.values) {
				player.collectionLog.add(item)
				if (RareDrop.contains(item)) {
					WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, item, ".Theatre of Blood on chest ${if (raid.hardMode) player.tobStatsHard.completions else player.tobStats.completions}")
				}
			}

			handleOpen(player, rewards)
		}
	}

	private fun handleOpen(player: Player, rewards: Container) {
		player.packetDispatcher.sendUpdateItemContainer(rewards)
		GameInterface.TOB_REWARDS.open(player)
	}

	override fun getObjects() = arrayOf(
		33086, 33087, 33088, 33089, 33090, OPEN_CHEST_NORMAL, OPEN_CHEST_PURPLE
	)

	companion object {
		const val OPEN_CHEST_NORMAL = 32994
		const val OPEN_CHEST_PURPLE = 41746
		val openChests = setOf(OPEN_CHEST_NORMAL, OPEN_CHEST_PURPLE)
	}

}