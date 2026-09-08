package org.jesse.game.content.tormented_demon.items

import org.jesse.scripts.item.actions.ItemActionScript
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
class TormentedSynapse: ItemPlugin() {
    override fun handle() {
        bind("Inspect") { player: Player, _: Item, _: Int ->
            player.dialogue {
                player("Duradel's notes mentioned creating a weapon out of demon parts... Maybe I should try it with this.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(TORMENTED_SYNAPSE)
}