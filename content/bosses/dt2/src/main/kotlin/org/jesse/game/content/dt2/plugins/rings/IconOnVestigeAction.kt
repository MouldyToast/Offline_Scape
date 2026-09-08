package org.jesse.game.content.dt2.plugins.rings

import org.jesse.game.content.dt2.npc.playAnimation
import org.jesse.game.content.dt2.npc.playGraphics
import org.jesse.game.content.dt2.npc.playSound
import org.jesse.game.item.Item
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.item.ids.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-09
 */
class IconOnVestigeAction : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val bloodRunes = Item(BLOOD_RUNE, 500)
        val itemUsed = getItem(from.id)
        val usedOn = getItem(to.id)
        if (itemUsed == null || usedOn == null) return
        val product = getResultingProduct(itemUsed.id)
        if (!player.inventory.containsItems(bloodRunes)) {
            player.dialogue { plain("You need 500 Blood Runes to craft this icon") }
            return
        }
        if (player.inventory.deleteItems(itemUsed, usedOn, bloodRunes).result != RequestResult.SUCCESS) return
        player.inventory.addItem(product)
        player.playAnimation(4462)
        player.playGraphics(759)
        player.playSound(144)
    }

    private fun getItem(id: Int): Item? {
        return when (id) {
            SEERS_ICON -> Item(SEERS_ICON)
            WARRIOR_ICON -> Item(WARRIOR_ICON)
            BERSERKER_ICON -> Item(BERSERKER_ICON)
            ARCHER_ICON -> Item(ARCHER_ICON)
            MAGUS_VESTIGE -> Item(MAGUS_VESTIGE)
            BELLATOR_VESTIGE -> Item(BELLATOR_VESTIGE)
            ULTOR_VESTIGE -> Item(ULTOR_VESTIGE)
            VENATOR_VESTIGE -> Item(VENATOR_VESTIGE)
            else -> null
        }
    }

    private fun getResultingProduct(id: Int): Item? {
        return when (id) {
            SEERS_ICON, MAGUS_VESTIGE -> Item(MAGUS_ICON)
            WARRIOR_ICON, BELLATOR_VESTIGE -> Item(BELLATOR_ICON)
            BERSERKER_ICON, ULTOR_VESTIGE -> Item(ULTOR_ICON)
            ARCHER_ICON, VENATOR_VESTIGE -> Item(VENATOR_ICON)
            else -> null
        }
    }

    override fun getItems(): IntArray? = null

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair(SEERS_ICON, MAGUS_VESTIGE), // Magus Icon
            ItemOnItemAction.ItemPair(WARRIOR_ICON, BELLATOR_VESTIGE), // Bellator icon
            ItemOnItemAction.ItemPair(BERSERKER_ICON, ULTOR_VESTIGE), // Ultor icon
            ItemOnItemAction.ItemPair(ARCHER_ICON, VENATOR_VESTIGE) // Venator icon
        )
    }
}