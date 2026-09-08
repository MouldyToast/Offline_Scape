package org.jesse.game.content.dt2.plugins

import org.jesse.game.content.dt2.npc.playAnimation
import org.jesse.game.content.dt2.npc.playGraphics
import org.jesse.game.content.dt2.npc.playSound
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

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
            BLOOD_RUNE,
            EXECUTIONERS_AXE_HEAD,
            LEVIATHANS_LURE,
            SIRENS_STAFF,
            EYE_OF_THE_DUKE
        )
    }

    private fun getDescriptionFor(id: Int): String {
        return when (id) {
            EXECUTIONERS_AXE_HEAD ->
                "It's the head of Vardorvis' axe. You might be able to combine it with something else."

            LEVIATHANS_LURE ->
                "It's the lure of the Leviathan. You might be able to combine it with something else."

            SIRENS_STAFF ->
                "It's a staff taken from the Whisperer. You might be able to combine it with something else."

            EYE_OF_THE_DUKE ->
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
        val lure: Item? = inventory.getItemById(LEVIATHANS_LURE)
        val staff: Item? = inventory.getItemById(SIRENS_STAFF)
        val eye: Item? = inventory.getItemById(EYE_OF_THE_DUKE)
        val head: Item? = inventory.getItemById(EXECUTIONERS_AXE_HEAD)
        if (lure == null || staff == null || eye == null || head == null) {
            mesbox("You don't have enough components to make anything useful. It looks like you'll need four separate components.")
            return
        }
        val runes: Item? = inventory.getItemById(BLOOD_RUNE)
        if (runes == null || runes.amount < 2000) {
            mesbox("Assembling those items will require the power of 2,000 blood runes.")
            return
        }
        runes.amount = 2000
        options("Create a Soulreaper axe?") {
            "Yes" {
                // Only if we're able to delete the required items, will qwe proceed with the process
                if (inventory.deleteItems(lure,  staff, eye, head, runes).result == RequestResult.SUCCESS) {
                    inventory.addOrDrop(SOULREAPER_AXE_28338, 1)
                    playAnimation(4462)
                    playGraphics(759)
                    playSound(144)
                    skills.addXp(SkillConstants.MAGIC, 400.0, true)
                    dialogue {
                        item(
                            SOULREAPER_AXE_28338,
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
