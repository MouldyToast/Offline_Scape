package com.near_reality.game.content.dt2.plugins

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player

class BloodAncientSceptrePlugin : ItemPlugin() {
    override fun handle() {
        bind("Dismantle") { player: Player, item: Item, slotId: Int ->
            if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(
                    ItemId.BLOOD_QUARTZ,
                    ItemId.ANCIENT_SCEPTRE
                )
            ) {
                player.inventory.deleteItem(item)
                player.inventory.addItem(Item(ItemId.BLOOD_QUARTZ))
                player.inventory.addItem(Item(ItemId.ANCIENT_SCEPTRE))
                player.sendMessage("You dismantle your Blood ancient sceptre.")
            } else {
                player.sendMessage("Not enough space in your inventory.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.BLOOD_ANCIENT_SCEPTRE_28260)
}

class IceAncientSceptrePlugin : ItemPlugin() {
    override fun handle() {
        bind("Dismantle") { player: Player, item: Item, slotId: Int ->
            if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(
                    ItemId.ICE_QUARTZ,
                    ItemId.ANCIENT_SCEPTRE
                )
            ) {
                player.inventory.deleteItem(item)
                player.inventory.addItem(Item(ItemId.ICE_QUARTZ))
                player.inventory.addItem(Item(ItemId.ANCIENT_SCEPTRE))
                player.sendMessage("You dismantle your Ice ancient sceptre.")
            } else {
                player.sendMessage("Not enough space in your inventory.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.ICE_ANCIENT_SCEPTRE_28262)
}

class ShadowAncientSceptrePlugin : ItemPlugin() {
    override fun handle() {
        bind("Dismantle") { player: Player, item: Item, slotId: Int ->
            if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(
                    ItemId.SHADOW_QUARTZ,
                    ItemId.ANCIENT_SCEPTRE
                )
            ) {
                player.inventory.deleteItem(item)
                player.inventory.addItem(Item(ItemId.SHADOW_QUARTZ))
                player.inventory.addItem(Item(ItemId.ANCIENT_SCEPTRE))
                player.sendMessage("You dismantle your Shadow ancient sceptre.")
            } else {
                player.sendMessage("Not enough space in your inventory.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.SHADOW_ANCIENT_SCEPTRE_28266)
}

class SmokeAncientSceptrePlugin : ItemPlugin() {
    override fun handle() {
        bind("Dismantle") { player: Player, item: Item, slotId: Int ->
            if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(
                    ItemId.SMOKE_QUARTZ,
                    ItemId.ANCIENT_SCEPTRE
                )
            ) {
                player.inventory.deleteItem(item)
                player.inventory.addItem(Item(ItemId.SMOKE_QUARTZ))
                player.inventory.addItem(Item(ItemId.ANCIENT_SCEPTRE))
                player.sendMessage("You dismantle your Smoke ancient sceptre.")
            } else {
                player.sendMessage("Not enough space in your inventory.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.SMOKE_ANCIENT_SCEPTRE_28264)
}
