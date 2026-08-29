package com.near_reality.game.content.dt2.plugins.sceptres;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.dialogue;

/**
 * Handles inspecting elemental quartz gems.
 *
 * @author Your Name | 02-19-2025 | 14:45
 */
class BloodQuartzPlugin : ItemPlugin() {
    override fun handle() {
        bind("Inspect") { player: Player, item: Item, slotId: Int ->
            player.dialogue {
                item(
                    ItemId.BLOOD_QUARTZ,
                    "It's a strange chunk of quartz filled with an ancient magical power. You might be able to combine it with something else."
                )
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.BLOOD_QUARTZ)
}

class IceQuartzPlugin : ItemPlugin() {
    override fun handle() {
        bind("Inspect") { player: Player, item: Item, slotId: Int ->
            player.dialogue {
                item(
                    ItemId.ICE_QUARTZ,
                    "It's a strange chunk of quartz filled with an ancient magical power. You might be able to combine it with something else."
                )
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.ICE_QUARTZ)
}

class ShadowQuartzPlugin : ItemPlugin() {
    override fun handle() {
        bind("Inspect") { player: Player, item: Item, slotId: Int ->
            player.dialogue {
                item(
                    ItemId.SHADOW_QUARTZ,
                    "It's a strange chunk of quartz filled with an ancient magical power. You might be able to combine it with something else."
                )
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.SHADOW_QUARTZ)
}

class SmokeQuartzPlugin : ItemPlugin() {
    override fun handle() {
        bind("Inspect") { player: Player, item: Item, slotId: Int ->
            player.dialogue {
                item(
                    ItemId.SMOKE_QUARTZ,
                    "It's a strange chunk of quartz filled with an ancient magical power. You might be able to combine it with something else."
                )
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.SMOKE_QUARTZ)
}
