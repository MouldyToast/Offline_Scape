package com.near_reality.game.content.dt2.plugins

import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player

class BloodAncientSceptrePlugin : ItemPlugin() {
    override fun handle() {
        bind("Dismantle") { player: Player, item: Item, slotId: Int ->
            if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(
                    BLOOD_QUARTZ,
                    ANCIENT_SCEPTRE
                )
            ) {
                player.inventory.deleteItem(item)
                player.inventory.addItem(Item(BLOOD_QUARTZ))
                player.inventory.addItem(Item(ANCIENT_SCEPTRE))
                player.sendMessage("You dismantle your Blood ancient sceptre.")
            } else {
                player.sendMessage("Not enough space in your inventory.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(BLOOD_ANCIENT_SCEPTRE_28260)
}

class IceAncientSceptrePlugin : ItemPlugin() {
    override fun handle() {
        bind("Dismantle") { player: Player, item: Item, slotId: Int ->
            if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(
                    ICE_QUARTZ,
                    ANCIENT_SCEPTRE
                )
            ) {
                player.inventory.deleteItem(item)
                player.inventory.addItem(Item(ICE_QUARTZ))
                player.inventory.addItem(Item(ANCIENT_SCEPTRE))
                player.sendMessage("You dismantle your Ice ancient sceptre.")
            } else {
                player.sendMessage("Not enough space in your inventory.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ICE_ANCIENT_SCEPTRE_28262)
}

class ShadowAncientSceptrePlugin : ItemPlugin() {
    override fun handle() {
        bind("Dismantle") { player: Player, item: Item, slotId: Int ->
            if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(
                    SHADOW_QUARTZ,
                    ANCIENT_SCEPTRE
                )
            ) {
                player.inventory.deleteItem(item)
                player.inventory.addItem(Item(SHADOW_QUARTZ))
                player.inventory.addItem(Item(ANCIENT_SCEPTRE))
                player.sendMessage("You dismantle your Shadow ancient sceptre.")
            } else {
                player.sendMessage("Not enough space in your inventory.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(SHADOW_ANCIENT_SCEPTRE_28266)
}

class SmokeAncientSceptrePlugin : ItemPlugin() {
    override fun handle() {
        bind("Dismantle") { player: Player, item: Item, slotId: Int ->
            if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(
                    SMOKE_QUARTZ,
                    ANCIENT_SCEPTRE
                )
            ) {
                player.inventory.deleteItem(item)
                player.inventory.addItem(Item(SMOKE_QUARTZ))
                player.inventory.addItem(Item(ANCIENT_SCEPTRE))
                player.sendMessage("You dismantle your Smoke ancient sceptre.")
            } else {
                player.sendMessage("Not enough space in your inventory.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(SMOKE_ANCIENT_SCEPTRE_28264)
}
