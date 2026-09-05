package com.near_reality.game.content.dt2.plugins.rings

import com.near_reality.api.model.Skill
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-12-02
 */
class ChiselOnBerserkerRing : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val ring = if (to.id == ItemId.BERSERKER_RING) to else from
        if (player.skills.getLevel(Skill.CRAFTING.ord) < 80) {
            player.sendMessage("You need a Crafting level of 80 to do this.")
            return
        }
        if (player.inventory.deleteItem(ring).result == RequestResult.SUCCESS) {
            player.inventory.addItem(ItemId.BERSERKER_ICON, 1)
            player.dialogue {
                item(ItemId.BERSERKER_ICON, "You successfully break down the Berserker ring into a Berserker icon.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.CHISEL, ItemId.BERSERKER_RING)
}

class ChiselOnSeersRing : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val ring = if (to.id == ItemId.SEERS_RING) to else from
        if (player.skills.getLevel(Skill.CRAFTING.ord) < 80) {
            player.sendMessage("You need a Crafting level of 80 to do this.")
            return
        }
        if (player.inventory.deleteItem(ring).result == RequestResult.SUCCESS) {
            player.inventory.addItem(ItemId.SEERS_ICON, 1)
            player.dialogue {
                item(ItemId.SEERS_ICON, "You successfully break down the Seers ring into a Seers icon.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.CHISEL, ItemId.SEERS_RING)
}

class ChiselOnArchersRing : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val ring = if (to.id == ItemId.ARCHERS_RING) to else from
        if (player.skills.getLevel(Skill.CRAFTING.ord) < 80) {
            player.sendMessage("You need a Crafting level of 80 to do this.")
            return
        }
        if (player.inventory.deleteItem(ring).result == RequestResult.SUCCESS) {
            player.inventory.addItem(ItemId.ARCHER_ICON, 1)
            player.dialogue {
                item(ItemId.ARCHER_ICON, "You successfully break down the Archers ring into an Archers icon.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.CHISEL, ItemId.ARCHERS_RING)
}

class ChiselOnWarriorsRing : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val ring = if (to.id == ItemId.WARRIOR_RING) to else from
        if (player.skills.getLevel(Skill.CRAFTING.ord) < 80) {
            player.sendMessage("You need a Crafting level of 80 to do this.")
            return
        }
        if (player.inventory.deleteItem(ring).result == RequestResult.SUCCESS) {
            player.inventory.addItem(ItemId.WARRIOR_ICON, 1)
            player.dialogue {
                item(ItemId.WARRIOR_ICON, "You successfully break down the Warriors ring into a Warriors icon.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(ItemId.CHISEL, ItemId.WARRIOR_RING)
}
