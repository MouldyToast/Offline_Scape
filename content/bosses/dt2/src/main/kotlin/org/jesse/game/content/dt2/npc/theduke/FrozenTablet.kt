package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-01-21
 */
class FrozenTablet: ItemPlugin() {
    override fun handle() {
        bind("Inspect") { player: Player, item: Item, _: Int ->
            player.dialogue {
                item(item, "You touch the frozen tablet. You feel a surge of energy run through your body, hearing the word 'Ghorrock' echo in your mind.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(FROZEN_TABLET)
}