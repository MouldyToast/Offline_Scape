package org.jesse.game.content.theatreofblood.plugin.item

import org.jesse.game.content.skills.magic.spells.teleports.TeleportCollection
import org.jesse.game.content.theatreofblood.VerSinhazaArea
import org.jesse.game.content.theatreofblood.interfaces.PartyOverlayInterface
import org.jesse.game.content.theatreofblood.room.TheatreRoom
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.task.TickTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.player.Player

/**
 * @author Tommeh
 * @author Jire
 */
class VerzikCrystalShard : ItemPlugin() {

    override fun handle() {
        bind("Teleport") { player: Player, _, _ ->
            if (player.area !is TheatreRoom) {
                player.sendMessage("The crystal shard seems inert outside the Theatre of Blood.")
                return@bind
            }
            val party = VerSinhazaArea.getParty(player) ?: return@bind
            player.inventory.deleteItem(verzikCrystalShard)
            TeleportCollection.VERZIK_CRYSTAL_SHARD.teleport(player)
            WorldTasksManager.schedule(object : TickTask() {
                override fun run() {
                    when (ticks++) {
                        1 -> PartyOverlayInterface.fadeRedPortal(player, "The crystal teleports you out.")
                        3 -> {
                            party.removeMember(player)
                            PartyOverlayInterface.fade(player, 200, 0, "The crystal teleports you out.")
                            stop()
                        }
                    }
                }
            }, 0, 0)
        }
    }

    override fun getItems() = VerzikCrystalShard.items

    internal companion object {

        val verzikCrystalShard = Item(ESCAPE_CRYSTAL)

        private val items = intArrayOf(ESCAPE_CRYSTAL)

    }

}