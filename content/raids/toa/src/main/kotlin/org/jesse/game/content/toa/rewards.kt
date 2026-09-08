package org.jesse.game.content.toa

import org.jesse.game.item.Item
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.Container
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.container.impl.ContainerType
import org.jesse.game.world.entity.player.dialogue.options
import kotlin.math.max
import kotlin.math.min
import org.jesse.scripts.interfaces.InterfaceScript
import org.jesse.game.model.ui.InterfacePosition.*
import org.jesse.game.GameInterface
import org.jesse.game.GameInterface.*
import org.jesse.game.util.AccessMask
import org.jesse.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class RewardsInterface : InterfaceScript() {

    fun Player.sendRewardsToBank() {
        val iter = pendingTOARewards.items.iterator()
        while(iter.hasNext()) {
            val item = iter.next()
            collectionLog.add(item)
            bank.add(item)
            iter.remove()
        }
        refreshLoot()
    }

    fun Player.sendRewardsToInventory() {
        val iter = pendingTOARewards.items.iterator()
        while(iter.hasNext()) {
            val item = iter.next()
            val depo = if(item.amount > 1) item.toNote() else item
            if (inventory.addItem(depo).result == RequestResult.SUCCESS) {
                collectionLog.add(item)
                iter.remove()
            } else {
                break
            }
        }
        refreshLoot()
    }

    fun Player.checkSize(container: Container) {
        if (container.isEmpty) {
            varManager.sendBit(14319, 0)
            close(this)
        }
    }

    fun Player.refreshLoot() =
        with(buildRewardsContainer()) {
            packetDispatcher.sendUpdateItemContainer(this, ContainerType.TOA_REWARD)
                .also { checkSize(this) }
        }

    fun Player.hasLoot() = getTOARewards().isNotEmpty()

    fun Player.handleTake(slot: Int, option: Int) {
        val containerItem: Item = pendingTOARewards.items[slot]
        var amountToTake = when (option) {
            2 -> 5
            3 -> 10
            4 -> containerItem.amount
            else -> 1
        }
        val availableAmount = containerItem.amount
        amountToTake = min(amountToTake.toDouble(), availableAmount.toDouble()).toInt()
        getInventory().addItem(Item(containerItem.id, amountToTake))
        val newAmount = max(0.0, (availableAmount - amountToTake).toDouble()).toInt()
        if (newAmount > 0) {
            containerItem.amount = newAmount
        } else {
            pendingTOARewards.items.remove(containerItem)
        }
        refreshLoot()
    }

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.31.2025
         */

        TOA_LOOT {
            "Bank-all"(4){
                if(player.hasLoot())
                    player.sendRewardsToBank()
                else player.sendMessage("You do not have any more rewards.")
            }
            "Inventory-all"(6) {
                if(player.hasLoot())
                    player.sendRewardsToInventory()
                else player.sendMessage("You do not have any more rewards.")
            }
            "Trash-all"(8) {
                if(player.hasLoot()) {
                    player.options("Trash your rewards?") {
                        "Yes" {
                            player.pendingTOARewards.items = arrayListOf()
                            player.refreshLoot()
                        }
                        "No" {}
                    }
                } else player.sendMessage("You do not have any more rewards.")
            }
            "Take-x"(10) {
                if(player.hasLoot()) {
                    player.handleTake(slotID, option)
                } else player.sendMessage("You do not have any more rewards.")
            }

            opened {
                if(getTOARewards().isNotEmpty()) {
                    refreshLoot()
                    varManager.sendBit(14139, 1)
                    sendInterface()
                    packetDispatcher.sendClientScript(149, 50528266, 811, 2, 3, 0, 1.unaryMinus(), "Take", "Take-5", "Take-10", "Take-All", "")
                    packetDispatcher.sendComponentSettings(getInterface().id, 10, 0, 5, CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP10)
                } else {
                    sendMessage("You have no reason to view the loot screen again.")
                }
            }
        }
    }
}
