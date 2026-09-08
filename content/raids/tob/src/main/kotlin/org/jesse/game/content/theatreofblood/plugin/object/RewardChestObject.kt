package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.content.theatreofblood.RewardRegistry
import org.jesse.game.model.item.enums.RareDrop
import org.jesse.game.world.broadcasts.BroadcastType
import org.jesse.game.world.broadcasts.WorldBroadcasts
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import java.util.*


/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-08-30
 */
class RewardChestObject : ObjectAction {

	private fun openChest(player: Player) {
		val registry = RewardRegistry.getRewardRegistry()
		val rewards = registry.getContainer(player)
		if (Objects.nonNull(rewards) && rewards?.isNotEmpty() == true) {
			var banked = false
			var totalSlots = 0
			rewards.forEach { item ->
				run {
					if (item.isStackable)
						totalSlots++
					else
						totalSlots += item.amount
				}
			}
			if (player.inventory.freeSlots >= totalSlots)
				rewards.forEach { reward ->
					player.inventory.addItem(reward)
					player.collectionLog.add(reward)
					if (RareDrop.contains(reward))
						WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, reward, ".Theatre of Blood")
				}
			else {
				banked = true
				rewards.forEach { reward ->
					player.bank.add(reward)
					player.collectionLog.add(reward)
					if (RareDrop.contains(reward))
						WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, reward, ".Theatre of Blood")
				}
			}
			if (registry.removedPlayerFromRewardMap(player)) {
				val container = if (banked) "Bank" else "Inventory"
				val message = String.format("Your rewards have been added to your %s", container)
				player.sendMessage(message)
			}
			else {
				// TODO: send a message to a dev log that something went wrong
			}
		}
		else
			player.sendMessage("You have nothing to collect.")
	}

	override fun handleObjectAction(player: Player, obj: WorldObject, name: String, optionId: Int, option: String) {
		// If the account has a PIN & Required unlocking; STOP HERE
		if (player.bankPin.requiresVerification(player) { openChest(player) }) return
		openChest(player)
	}

	override fun getObjects() = arrayOf(
		REWARDS_CHEST_41437,
		REWARDS_CHEST_41436,
		REWARDS_CHEST_41435
	)
}