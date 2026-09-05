package com.near_reality.game.content.dt2.plugins

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.dialogue.dialogue;
import com.zenyte.game.world.entity.player.dialogue.options

class BloodQuartzOnAncientSceptre : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val sceptre = if (to.id == ItemId.ANCIENT_SCEPTRE) to else from
        val quartz = if (to.id == ItemId.BLOOD_QUARTZ) to else from

        player.dialogue {
            item(
                ItemId.ICE_ANCIENT_SCEPTRE_28262,
                ItemId.SMOKE_QUARTZ,
                "Are you sure you wish to combine the Blood quartz and Ancient sceptre to create a Blood ancient sceptre?"
            )
            options("Combine the Blood quartz and Ancient sceptre?") {
                "Yes." to {
                    if (player.inventory.deleteItem(sceptre).result == RequestResult.SUCCESS &&
                        player.inventory.deleteItem(quartz).result == RequestResult.SUCCESS
                    ) {
                        player.inventory.addItem(ItemId.BLOOD_ANCIENT_SCEPTRE_28260, 1)
                        player.dialogue {
                            item(
                                ItemId.BLOOD_ANCIENT_SCEPTRE_28260,
                                "You successfully combine the Blood quartz and Ancient sceptre to create a Blood ancient sceptre."
                            )
                        }
                    }
                }
                "No." to {}
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.BLOOD_QUARTZ, ItemId.ANCIENT_SCEPTRE)
}

class IceQuartzOnAncientSceptre : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val sceptre = if (to.id == ItemId.ANCIENT_SCEPTRE) to else from
        val quartz = if (to.id == ItemId.ICE_QUARTZ) to else from

        player.dialogue {
            item(
                ItemId.ICE_ANCIENT_SCEPTRE_28262,
                ItemId.SMOKE_QUARTZ,
                "Are you sure you wish to combine the Ice quartz and Ancient sceptre to create an Ice ancient sceptre?"
            )
            options("Combine the Smoke quartz and Ancient sceptre?") {
                "Yes." to {
                    if (player.inventory.deleteItem(sceptre).result == RequestResult.SUCCESS &&
                        player.inventory.deleteItem(quartz).result == RequestResult.SUCCESS
                    ) {
                        player.inventory.addItem(ItemId.ICE_ANCIENT_SCEPTRE_28262, 1)
                        player.dialogue {
                            item(
                                ItemId.ICE_ANCIENT_SCEPTRE_28262,
                                "You successfully combine the Ice quartz and Ancient sceptre to create an Ice ancient sceptre."
                            )
                        }
                    }
                }
                "No." to {}
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.ICE_QUARTZ, ItemId.ANCIENT_SCEPTRE)
}

class ShadowQuartzOnAncientSceptre : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val sceptre = if (to.id == ItemId.ANCIENT_SCEPTRE) to else from
        val quartz = if (to.id == ItemId.SHADOW_QUARTZ) to else from

        player.dialogue {
            options("Are you sure you wish to combine the Shadow quartz and Ancient sceptre to create a Shadow ancient sceptre?") {
                "Yes." to {
                    if (player.inventory.deleteItem(sceptre).result == RequestResult.SUCCESS &&
                        player.inventory.deleteItem(quartz).result == RequestResult.SUCCESS
                    ) {
                        player.inventory.addItem(ItemId.SHADOW_ANCIENT_SCEPTRE_28266, 1)
                        player.dialogue {
                            item(
                                ItemId.SHADOW_ANCIENT_SCEPTRE_28266,
                                "You successfully combine the Shadow quartz and Ancient sceptre to create a Shadow ancient sceptre."
                            )
                        }
                    }
                }
                "No." to {}

            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.SHADOW_QUARTZ, ItemId.ANCIENT_SCEPTRE)
}

class SmokeQuartzOnAncientSceptre : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val sceptre = if (to.id == ItemId.ANCIENT_SCEPTRE) to else from
        val quartz = if (to.id == ItemId.SMOKE_QUARTZ) to else from

        player.dialogue {
            options("Are you sure you wish to combine the Smoke quartz and Ancient sceptre to create a Smoke ancient sceptre?") {
                "Yes." to {
                    if (player.inventory.deleteItem(sceptre).result == RequestResult.SUCCESS &&
                        player.inventory.deleteItem(quartz).result == RequestResult.SUCCESS
                    ) {
                        player.inventory.addItem(ItemId.SMOKE_ANCIENT_SCEPTRE_28264, 1)
                        player.dialogue {
                            item(
                                ItemId.SMOKE_ANCIENT_SCEPTRE_28264,
                                "You successfully combine the Smoke quartz and Ancient sceptre to create a Smoke ancient sceptre."
                            )
                        }
                    }
                }
                "No." to {}
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.SMOKE_QUARTZ, ItemId.ANCIENT_SCEPTRE)
}

