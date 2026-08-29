package com.near_reality.game.content.dt2.plugins

import com.near_reality.game.content.dt2.npc.playAnimation
import com.near_reality.game.content.dt2.npc.playGraphics
import com.near_reality.game.content.dt2.npc.playSound
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class SoulreaperAxePlugin : ItemPlugin(), ItemOnItemAction {
    override fun handle() {
        bind("Inspect") { player: Player, item: Item, slotId: Int ->
            player.dialogue {
                item(item.id, getDescriptionFor(item.id))
            }
        }
    }

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        player.combineAxe()
    }

    override fun getItems(): IntArray {
        return intArrayOf(
            ItemId.BLOOD_RUNE,
            ItemId.EXECUTIONERS_AXE_HEAD,
            ItemId.LEVIATHANS_LURE,
            ItemId.SIRENS_STAFF,
            ItemId.EYE_OF_THE_DUKE
        )
    }

    private fun getDescriptionFor(id: Int): String {
        return when (id) {
            ItemId.EXECUTIONERS_AXE_HEAD ->
                "It's the head of Vardorvis' axe. You might be able to combine it with something else."

            ItemId.LEVIATHANS_LURE ->
                "It's the lure of the Leviathan. You might be able to combine it with something else."

            ItemId.SIRENS_STAFF ->
                "It's a staff taken from the Whisperer. You might be able to combine it with something else."

            ItemId.EYE_OF_THE_DUKE ->
                "It's one of Duke Sucellus' many eyes. You might be able to combine it with something else."

            else -> ""
        }
    }

    /**
     * Extension function for Player to combine the four axe components into a Soulreaper axe.
     */
    fun Player.combineAxe() {
        if (skills.getLevel(SkillConstants.MAGIC) < 75) {
            mesbox("You need a Magic level of 75 to assemble the components.")
            return
        }
        val lure: Item? = inventory.getItemById(ItemId.LEVIATHANS_LURE)
        val staff: Item? = inventory.getItemById(ItemId.SIRENS_STAFF)
        val eye: Item? = inventory.getItemById(ItemId.EYE_OF_THE_DUKE)
        val head: Item? = inventory.getItemById(ItemId.EXECUTIONERS_AXE_HEAD)
        if (lure == null || staff == null || eye == null || head == null) {
            mesbox("You don't have enough components to make anything useful. It looks like you'll need four separate components.")
            return
        }
        val runes: Item? = inventory.getItemById(ItemId.BLOOD_RUNE)
        if (runes == null || runes.amount < 2000) {
            mesbox("Assembling those items will require the power of 2,000 blood runes.")
            return
        }
        runes.amount = 2000
        options("Create a Soulreaper axe?") {
            "Yes" {
                // Only if we're able to delete the required items, will qwe proceed with the process
                if (inventory.deleteItems(lure,  staff, eye, head, runes).result == RequestResult.SUCCESS) {
                    inventory.addOrDrop(ItemId.SOULREAPER_AXE_28338, 1)
                    playAnimation(4462)
                    playGraphics(759)
                    playSound(144)
                    skills.addXp(SkillConstants.MAGIC, 400.0, true)
                    dialogue {
                        item(
                            ItemId.SOULREAPER_AXE_28338,
                            "You successfully assemble the four components into a Soulreaper axe."
                        )
                    }
                }
            }

            "No" { }
        }
    }

    private fun Player.mesbox(message: String) = dialogue {
        plain(message)
    }
}
