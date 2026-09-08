package com.near_reality.game.content.dt2.plugins.rings

import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.item.ItemId.BELLATOR_VESTIGE
import com.zenyte.game.item.ItemId.MAGUS_VESTIGE
import com.zenyte.game.item.ItemId.ULTOR_VESTIGE
import com.zenyte.game.item.ItemId.VENATOR_VESTIGE

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-09
 */
class Vestige : ItemPlugin() {
    override fun handle() {
        bind("Inspect") { player, item, _, _ ->
            player.dialogue {
                item(
                    item.id,
                    "It's an ancient vestige filled with power. " +
                            "You might be able to combine it with something else."
                )
            }
        }

    }

    override fun getItems(): IntArray {
        return intArrayOf(MAGUS_VESTIGE, BELLATOR_VESTIGE, ULTOR_VESTIGE, VENATOR_VESTIGE)
    }
}