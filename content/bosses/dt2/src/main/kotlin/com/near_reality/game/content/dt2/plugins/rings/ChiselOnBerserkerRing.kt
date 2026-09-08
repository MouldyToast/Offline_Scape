package com.near_reality.game.content.dt2.plugins.rings

import com.near_reality.api.model.Skill
import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
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
        val ring = if (to.id == BERSERKER_RING) to else from
        if (player.skills.getLevel(Skill.CRAFTING.ord) < 80) {
            player.sendMessage("You need a Crafting level of 80 to do this.")
            return
        }
        if (player.inventory.deleteItem(ring).result == RequestResult.SUCCESS) {
            player.inventory.addItem(BERSERKER_ICON, 1)
            player.dialogue {
                item(BERSERKER_ICON, "You successfully break down the Berserker ring into a Berserker icon.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(CHISEL, BERSERKER_RING)
}

class ChiselOnSeersRing : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val ring = if (to.id == SEERS_RING) to else from
        if (player.skills.getLevel(Skill.CRAFTING.ord) < 80) {
            player.sendMessage("You need a Crafting level of 80 to do this.")
            return
        }
        if (player.inventory.deleteItem(ring).result == RequestResult.SUCCESS) {
            player.inventory.addItem(SEERS_ICON, 1)
            player.dialogue {
                item(SEERS_ICON, "You successfully break down the Seers ring into a Seers icon.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(CHISEL, SEERS_RING)
}

class ChiselOnArchersRing : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val ring = if (to.id == ARCHERS_RING) to else from
        if (player.skills.getLevel(Skill.CRAFTING.ord) < 80) {
            player.sendMessage("You need a Crafting level of 80 to do this.")
            return
        }
        if (player.inventory.deleteItem(ring).result == RequestResult.SUCCESS) {
            player.inventory.addItem(ARCHER_ICON, 1)
            player.dialogue {
                item(ARCHER_ICON, "You successfully break down the Archers ring into an Archers icon.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(CHISEL, ARCHERS_RING)
}

class ChiselOnWarriorsRing : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        player ?: return; from ?: return; to ?: return
        val ring = if (to.id == WARRIOR_RING) to else from
        if (player.skills.getLevel(Skill.CRAFTING.ord) < 80) {
            player.sendMessage("You need a Crafting level of 80 to do this.")
            return
        }
        if (player.inventory.deleteItem(ring).result == RequestResult.SUCCESS) {
            player.inventory.addItem(WARRIOR_ICON, 1)
            player.dialogue {
                item(WARRIOR_ICON, "You successfully break down the Warriors ring into a Warriors icon.")
            }
        }
    }

    override fun getItems(): IntArray =
        intArrayOf(CHISEL, WARRIOR_RING)
}
