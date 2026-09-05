package com.near_reality.game.content.dt2.npc.vardorvis

import com.near_reality.game.content.dt2.area.VardorvisInstance
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-16
 */
class VardovisRocksEntry : ObjectAction {

    private val awakenedOrb = Item(ItemId.AWAKENERS_ORB)

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
        arrayOf(49495, 48741)
}