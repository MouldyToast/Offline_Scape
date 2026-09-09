package org.jesse.game.content.wilderness.slayer

import com.google.common.eventbus.Subscribe
import org.jesse.game.content.bountyhunter.getWildernessLevel
import org.jesse.game.world.PlayerEvent
import org.jesse.game.world.hook
import org.jesse.game.content.slayer.SlayerMaster
import org.jesse.game.item.ids.*
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.player.Player
import org.jesse.plugins.events.ServerLaunchEvent
import mgi.types.config.items.ItemDefinitions

/**
 * Handles wilderness specific rewards for tasks completed for Krysilia.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object WildernessSlayerModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        event.worldThread.hook<PlayerEvent.SlayerTaskCompleted> {
            if (it.assignment.master == SlayerMaster.KRYSTILIA) {
                tryUpgradeEmblem(it.player)
                rewardBloodMoney(it.player)
                rewardLarransKey(it.player)
            }
        }
    }

    private fun tryUpgradeEmblem(player: Player) {
        if (player.getWildernessLevel() < 1) return
        val inventory = player.inventory
        val bestUpgradeableEmblemInInventory = WildernessSlayerEmblem.entries
            .reversed()
            .drop(1) // drop tier 10 since we cannot upgrade that
            .firstOrNull { inventory.containsItem(it.id) }
        val nextEmblem = bestUpgradeableEmblemInInventory?.nextOrNull()
        if (nextEmblem == null)
            player.sendMessage("No emblems were able to get upgraded when completing the Wilderness slayer assignment.")
        else {
            inventory.deleteItem(bestUpgradeableEmblemInInventory.id, 1)
            inventory.addItem(nextEmblem.id, 1)
            player.sendMessage("Krystilia has upgraded your ${ItemDefinitions.nameOf(nextEmblem.id)} as a reward for completing a Wilderness slayer assignment.")
        }
    }

    private fun rewardBloodMoney(player: Player) {
        var bloodMoneyRewardAmount = Utils.random(10, 50)
        if (player.getWildernessLevel() < 1) return
        player.inventory.addOrDrop(BLOOD_MONEY, bloodMoneyRewardAmount)
    }

    private fun rewardLarransKey(player: Player) {
        if (player.getWildernessLevel() < 1) return
        player.inventory.addOrDrop(LARRANS_KEY, 1)
    }
}
