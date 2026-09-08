package com.near_reality.game.content.dt2.plugins

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.dialogue.dialogue;
import com.zenyte.game.world.entity.player.dialogue.options

class BloodQuartzOnAncientSceptre : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val sceptre = if (to.id == ANCIENT_SCEPTRE) to else from
        val quartz = if (to.id == BLOOD_QUARTZ) to else from

        player.dialogue {
            item(
                ICE_ANCIENT_SCEPTRE_28262,
                SMOKE_QUARTZ,
                "Are you sure you wish to combine the Blood quartz and Ancient sceptre to create a Blood ancient sceptre?"
            )
            options("Combine the Blood quartz and Ancient sceptre?") {
                "Yes." to {
                    if (player.inventory.deleteItem(sceptre).result == RequestResult.SUCCESS &&
                        player.inventory.deleteItem(quartz).result == RequestResult.SUCCESS
                    ) {
                        player.inventory.addItem(BLOOD_ANCIENT_SCEPTRE_28260, 1)
                        player.dialogue {
                            item(
                                BLOOD_ANCIENT_SCEPTRE_28260,
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
        intArrayOf(BLOOD_QUARTZ, ANCIENT_SCEPTRE)
}

class IceQuartzOnAncientSceptre : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val sceptre = if (to.id == ANCIENT_SCEPTRE) to else from
        val quartz = if (to.id == ICE_QUARTZ) to else from

        player.dialogue {
            item(
                ICE_ANCIENT_SCEPTRE_28262,
                SMOKE_QUARTZ,
                "Are you sure you wish to combine the Ice quartz and Ancient sceptre to create an Ice ancient sceptre?"
            )
            options("Combine the Smoke quartz and Ancient sceptre?") {
                "Yes." to {
                    if (player.inventory.deleteItem(sceptre).result == RequestResult.SUCCESS &&
                        player.inventory.deleteItem(quartz).result == RequestResult.SUCCESS
                    ) {
                        player.inventory.addItem(ICE_ANCIENT_SCEPTRE_28262, 1)
                        player.dialogue {
                            item(
                                ICE_ANCIENT_SCEPTRE_28262,
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
        intArrayOf(ICE_QUARTZ, ANCIENT_SCEPTRE)
}

class ShadowQuartzOnAncientSceptre : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val sceptre = if (to.id == ANCIENT_SCEPTRE) to else from
        val quartz = if (to.id == SHADOW_QUARTZ) to else from

        player.dialogue {
            options("Are you sure you wish to combine the Shadow quartz and Ancient sceptre to create a Shadow ancient sceptre?") {
                "Yes." to {
                    if (player.inventory.deleteItem(sceptre).result == RequestResult.SUCCESS &&
                        player.inventory.deleteItem(quartz).result == RequestResult.SUCCESS
                    ) {
                        player.inventory.addItem(SHADOW_ANCIENT_SCEPTRE_28266, 1)
                        player.dialogue {
                            item(
                                SHADOW_ANCIENT_SCEPTRE_28266,
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
        intArrayOf(SHADOW_QUARTZ, ANCIENT_SCEPTRE)
}

class SmokeQuartzOnAncientSceptre : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val sceptre = if (to.id == ANCIENT_SCEPTRE) to else from
        val quartz = if (to.id == SMOKE_QUARTZ) to else from

        player.dialogue {
            options("Are you sure you wish to combine the Smoke quartz and Ancient sceptre to create a Smoke ancient sceptre?") {
                "Yes." to {
                    if (player.inventory.deleteItem(sceptre).result == RequestResult.SUCCESS &&
                        player.inventory.deleteItem(quartz).result == RequestResult.SUCCESS
                    ) {
                        player.inventory.addItem(SMOKE_ANCIENT_SCEPTRE_28264, 1)
                        player.dialogue {
                            item(
                                SMOKE_ANCIENT_SCEPTRE_28264,
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
        intArrayOf(SMOKE_QUARTZ, ANCIENT_SCEPTRE)
}

