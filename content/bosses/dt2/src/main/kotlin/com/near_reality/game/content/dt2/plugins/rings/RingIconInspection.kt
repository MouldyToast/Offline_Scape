package com.near_reality.game.content.dt2.plugins.rings

import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.item.ItemId.BELLATOR_ICON
import com.zenyte.game.item.ItemId.MAGUS_ICON
import com.zenyte.game.item.ItemId.ULTOR_ICON
import com.zenyte.game.item.ItemId.VENATOR_ICON

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-09
 */
class RingIconInspection : ItemPlugin() {
    override fun handle() {
        bind("Inspect") { player, item, _, _ ->
            player.dialogue {
                item(
                    item.id,
                    "It's an ancient icon filled with power. " +
                            "You might be able to use it to craft a powerful ring."
                )
            }
        }

    }

    override fun getItems(): IntArray {
        return intArrayOf(MAGUS_ICON, BELLATOR_ICON, ULTOR_ICON, VENATOR_ICON)
    }
}