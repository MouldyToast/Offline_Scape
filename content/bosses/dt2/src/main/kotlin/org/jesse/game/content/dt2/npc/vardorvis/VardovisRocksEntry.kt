package org.jesse.game.content.dt2.npc.vardorvis

import org.jesse.game.content.dt2.area.VardorvisInstance
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject


class VardovisRocksEntry : ObjectAction {

    private val awakenedOrb = Item(AWAKENERS_ORB)

    override fun handleObjectAction(player: Player?, `object`: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return; `object` ?: return; name ?: return; option ?: return
        if (option == "Climb-over") {
            if (player.inventory.containsItem(awakenedOrb))
                player.offerAwakenedVariant()
            else
                player.enterVardorvisInstance(awakened = false)
        }
    }

    private fun Player.offerAwakenedVariant() {
        dialogue {
            options("Consume the awakener's orb to awaken Vardorvis?", "Yes.", "No.")
                .onOptionOne {
                    if (inventory.deleteItem(awakenedOrb).succeededAmount == 1)
                        enterVardorvisInstance(awakened = true)
                }
                .onOptionTwo { enterVardorvisInstance(awakened = false) }
        }
    }

    private fun Player.enterVardorvisInstance(awakened: Boolean) {
        val instance = VardorvisInstance.createInstance(this, awakened)
            instance.constructRegion()
    }

    override fun getObjects(): Array<Any> =
        arrayOf(49495)
}