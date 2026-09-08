package org.jesse.game.content.gauntlet.`object`

import org.jesse.game.content.gauntlet.*
import org.jesse.game.content.gauntlet.rewards.GauntletRewardType
import org.jesse.game.content.gauntlet.rewards.GauntletRewards
import org.jesse.game.content.advent.AdventCalendarManager
import org.jesse.game.content.follower.impl.BossPet
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.enums.RareDrop
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.broadcasts.BroadcastType
import org.jesse.game.world.broadcasts.WorldBroadcasts
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin

/**
 *
 * @author Andys1814.
 * @since 1/19/2022.
 */
class GauntletRewardsChest : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        val gauntletType = player.gauntletTypeForReward
        val rewardType = player.gauntletRewardType
        if (gauntletType == null || rewardType == null) {
            player.sendMessage("There is nothing waiting for you in this chest.")
            return
        }
        player.animation = Animation.GRAB
        player.lock(1)
        player.sendMessage("You open the chest.")
        WorldTasksManager.schedule {
            player.sendMessage("You find some treasure in the chest!")
            val rewardsTable = when (gauntletType) {
                GauntletType.STANDARD -> GauntletRewards.Crystalline
                GauntletType.CORRUPTED -> GauntletRewards.Corrupted
                GauntletType.STANDARD_NO_PREP -> GauntletRewards.CrystallineNoPrep
                GauntletType.CORRUPTED_NO_PREP -> GauntletRewards.CorruptedNoPrep
            }
            val itemRewards = when(rewardType) {
                GauntletRewardType.NONE -> emptyList()
                GauntletRewardType.MINIMAL_DAMAGE -> rewardsTable.rollMinimalDamageReward(player)
                GauntletRewardType.PARTIAL_DAMAGE -> rewardsTable.rollBossPartiallyDamagedReward(player)
                GauntletRewardType.FULLY_COMPLETED -> {
                    var rate = 1.0

                    /* Adds 100% to unique drop weight (makes less common) for No Prep modes*/
                    if(gauntletType.isNoPrep)
                        rate = 2.0

                    rewardsTable.rollCompleted(player, rate)
                }
            }
            for (item in itemRewards) {
                if (item.id == YOUNGLLEF && BossPet.YOUNGLLEF.hasPet(player)) {
                    player.sendMessage("<col=ff0000>You have a funny feeling like you would have been followed...</col>")
                    continue
                }

                player.inventory.addOrDrop(item)
                player.collectionLog.add(item)
                LootBroadcastPlugin.fireEvent(player.name, item, player.location, false)
                if (RareDrop.contains(item))
                    WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, item, "Gauntlet")
            }

            AdventCalendarManager.increaseChallengeProgress(player, 2022, 20, 1)

            val coins = Item(995, if (gauntletType == GauntletType.CORRUPTED) Utils.random(50_000, 100_000) else Utils.random(10_000, 50_000))
            player.inventory.addOrDrop(coins)

            player.gauntletTypeForReward = null
            player.gauntletRewardType = null
            Gauntlet.sendRewardChestVarbit(player)
        }
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(GauntletConstants.REWARDS_CHEST)
    }
}
