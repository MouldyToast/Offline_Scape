package com.near_reality.game.content.tormented_demon.items

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue

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