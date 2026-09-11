package org.jesse.game.content.shop

import org.jesse.api.service.vote.totalVoteCredits
import org.jesse.game.world.entity.player.pvmArenaPoints

import org.jesse.game.item.Item
import org.jesse.game.model.shop.CurrencyPalette
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.world.entity.player.Player
import kotlin.math.max
import kotlin.math.min

object ShopCurrencyHandler {

    @JvmStatic
    fun getAmount(type: CurrencyPalette, player: Player): Int {
        return when(type) {
            ShopCurrency.VOTE_POINTS -> player.totalVoteCredits

            ShopCurrency.LOYALTY_POINTS -> player.loyaltyManager.loyaltyPoints
            ShopCurrency.PVM_ARENA_POINTS -> player.pvmArenaPoints.toInt()
            ShopCurrency.SLAYER_POINTS -> player.slayer.slayerPoints
            else -> {
                if (type.isPhysical) {
                    val itemId = type.id.takeIf { it > 0 } ?: error("Invalid item id: ${type.id} for currency type: ${type::class.simpleName}")
                    return player.inventory.getAmountOf(itemId)
                } else
                    error("Invalid currency type: ${type::class.simpleName}")
            }
        }
    }

    @JvmStatic
    fun remove(type: ShopCurrency, player: Player, amount: Int) {
        when(type) {
            ShopCurrency.VOTE_POINTS -> player.totalVoteCredits -= amount

            ShopCurrency.LOYALTY_POINTS -> {
                val currentAmount = player.loyaltyManager.loyaltyPoints
                player.loyaltyManager.setLoyaltyPoints(max(0, (currentAmount - amount)))
            }
            ShopCurrency.PVM_ARENA_POINTS -> player.pvmArenaPoints -= amount
            ShopCurrency.SLAYER_POINTS -> player.slayer.setSlayerPoints(player.slayer.slayerPoints - amount, true)
            else -> {
                if (type.isPhysical) {
                    val itemId = type.id.takeIf { it > 0 } ?: error("Invalid item id: ${type.id} for currency type: ${type::class.simpleName}")
                    player.inventory.deleteItem(Item(itemId, amount))
                } else
                    error("Invalid currency type: ${type::class.simpleName}")
            }
        }
    }

    @JvmStatic
    fun add(type: ShopCurrency, player: Player, amount: Int) {
        when(type) {
            ShopCurrency.VOTE_POINTS -> player.totalVoteCredits += amount

            ShopCurrency.LOYALTY_POINTS -> {
                val currentAmount = player.loyaltyManager.loyaltyPoints
                player.loyaltyManager.setLoyaltyPoints(min((currentAmount + amount), Int.MAX_VALUE))
            }
            ShopCurrency.PVM_ARENA_POINTS -> player.pvmArenaPoints += amount
            ShopCurrency.SLAYER_POINTS -> player.slayer.setSlayerPoints(player.slayer.slayerPoints + amount, true)
            else -> {
                if (type.isPhysical) {
                    val itemId = type.id.takeIf { it > 0 } ?: error("Invalid item id: ${type.id} for currency type: ${type::class.simpleName}")
                    player.inventory.addItem(Item(itemId, amount))
                } else
                    error("Invalid currency type: ${type::class.simpleName}")
            }
        }
    }
}
