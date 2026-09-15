package org.jesse.game.content.gauntlet

import org.jesse.game.content.gauntlet.rewards.GauntletRewards
import org.jesse.game.util.invoke
import org.jesse.game.content.achievementdiary.Diary
import org.jesse.game.item.Item
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.GameCommands
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import mgi.types.config.items.ItemDefinitions

object GauntletCommands {

    fun register() {
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "gauntlet", "Gauntlet testing menu.") { p, args ->
            p.options {
                "Teleport to" {
                    p.setLocation(Location(3030, 6128, 1))
                }
                "Generate Rewards" {
                    p.options {
                        "Crystalline" {
                            p.generateGauntletRewards(GauntletRewards.Crystalline)
                        }
                        "Corrupted" {
                            p.generateGauntletRewards(GauntletRewards.Corrupted)
                        }
                    }
                }
            }
        }
    }

    private fun Player.generateGauntletRewards(rewards: GauntletRewards) {
        dialogueManager.finish()
        sendInputInt("How many times?") { times ->
            options("What type?") {
                "Minimal damage" {
                    sendDeveloperMessage("Rolling $times (Minimal Damage)")
                    rollAndAdd(times, rewards::rollMinimalDamageRewardSim)
                }
                "Partial damage" {
                    sendDeveloperMessage("Rolling $times (Partial Damage)")
                    rollAndAdd(times, rewards::rollBossPartiallyDamagedRewardSim)
                }
                "Completed" {
                    sendDeveloperMessage("Rolling $times (Completed)")
                    rollAndAdd(times, rewards::rollCompleted)
                }
                "Completed (25% tertiary)" {
                    sendDeveloperMessage("Rolling $times (Completed)")
                    rollAndAdd(times) { rewards.rollCompleted(it, 0.75) }
                }
            }
        }
    }

    private fun Player.rollAndAdd(times: Int, provider: (Player) -> List<Item>) {
        val totalItems = mutableListOf<Item>()
        repeat(times) { totalItems += provider(this) }
        bank.container.clear()
        val failureIds = mutableSetOf<Int>()
        val dropStrings =totalItems
            .groupBy { it.id }
            .mapValues { it.value.size to it.value.sumOf { it.amount } }
            .entries
            .sortedBy { it.value.first }
            .map { (itemId, value) ->
                val (dropped, amount) = value
                val itemName = ItemDefinitions.nameOf(itemId)
                val every = times.toDouble().div(dropped)
                "${"${amount}x".padEnd(10, '-')} ${itemName.replace(" ", "-").replace("(", "").replace(")", "").padEnd(35, '-')} ${Colour.RS_PURPLE("(1/${every.toInt()})").padEnd(5, '-')}"
             }
        Diary.sendJournal(this, "Simulations: $times - Drops: " + dropStrings.size, dropStrings)

        for (item in totalItems) {
            if (bank.add(item).isFailure) {
                failureIds += item.id
            }
        }
        if (failureIds.isNotEmpty()) {
            sendDeveloperMessage("Failed to add some items to ur bank, ask dev to check console logs.")
            failureIds.forEach {
                println(it)
            }
        }
//        GameInterface.BANK.open(this)
    }
}
