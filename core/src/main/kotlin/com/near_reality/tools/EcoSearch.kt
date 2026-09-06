package com.near_reality.tools

import com.zenyte.game.content.gravestones.scanGravestone
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container

/**
 * @author Jire
 */
object EcoSearch {

    @JvmStatic
    fun main(args: Array<String>) {
        val itemID = args[0].toInt()
        searchEconomyForItem(itemID)
        .forEach { (p, count) -> println("\"${p.username}\" had ${"%,d".format(count)}") }
    }

    private fun Container.countOf(itemID: Int) = getAmountOf(itemID)

    /**
     * Searches the game's economy for a specific item by its ID and retrieves the top 100 players
     * who possess the item along with their respective quantities. The search includes items in
     * the bank, inventory, equipment, looting bag, gravestone, and retrieval service containers.
     *
     * @param itemID The unique identifier for the item to search for.
     * @return A sequence of pairs, where each pair contains a Player object and the total quantity
     * of the specified item owned by the player. The sequence is sorted in descending order
     * by the item quantity, and only the top 100 results are included.
     */
    fun searchEconomyForItem(itemID: Int, maxResults: Int = 100): Sequence<Pair<Player, Int>> {
        return searchEconomyForItem(itemID).take(n = maxResults)
    }

    fun searchEconomyForItem(itemID: Int): Sequence<Pair<Player, Int>> {
        WealthScanner.loadDefault()
        return WealthScanner.allAccounts()
            .map { p ->
                var count = p.bank.container.countOf(itemID)
                    count += p.inventory.container.countOf(itemID)
                    count += p.equipment.container.countOf(itemID)
                    count += p.lootingBag.container.countOf(itemID)
                    count += (scanGravestone(p)?.container?.countOf(itemID) ?: 0)
                    count += p.retrievalService.container.countOf(itemID)
                p to count
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
    }

}
