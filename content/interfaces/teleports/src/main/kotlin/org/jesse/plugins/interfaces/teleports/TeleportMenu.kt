package org.jesse.plugins.interfaces.teleports

import com.google.common.eventbus.Subscribe
import org.jesse.cache.interfaces.teleports.Category
import org.jesse.cache.interfaces.teleports.Destination
import org.jesse.cache.interfaces.teleports.TeleportsList
import org.jesse.game.world.entity.player.GameCommands
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.game.world.entity.player.teleports.DestinationTeleport
import org.jesse.plugins.dialogue.OptionsMenuD
import org.jesse.plugins.events.ServerLaunchEvent

@Suppress("unused")
object TeleportMenu {

    private val categories: List<Category> by lazy { TeleportsList.teleports.categories }

    @JvmStatic
    fun open(player: Player) {
        if (player.isLocked) {
            player.sendMessage("You can't do that right now.")
            return
        }
        openCategoryPicker(player)
    }

    private fun openCategoryPicker(player: Player) {
        val previous = player.teleportsManager.previousDestination
        val names = mutableListOf<String>()

        if (previous != null) {
            names.add("Previous: ${previous.name}")
        }
        for (category in categories) {
            // Strip " Teleports" suffix for cleaner display
            val label = category.name.removeSuffix(" Teleports")
            names.add(label)
        }

        player.dialogueManager.start(object : OptionsMenuD(player, "Teleports", *names.toTypedArray()) {
            override fun handleClick(slotId: Int) {
                var idx = slotId
                if (previous != null) {
                    if (idx == 0) {
                        DestinationTeleport(previous).teleport(player)
                        player.teleportsManager.setPreviousTeleport(previous)
                        return
                    }
                    idx--
                }
                if (idx < 0 || idx >= categories.size) return
                openCategory(player, categories[idx])
            }

            override fun cancelOption() = true
        })
    }

    private fun openCategory(player: Player, category: Category) {
        val destinations = category.destinations
        val names = destinations.map { it.name }.toMutableList()
        names.add("Back")
        val title = category.name.removeSuffix(" Teleports")

        player.dialogueManager.start(object : OptionsMenuD(player, title, *names.toTypedArray()) {
            override fun handleClick(slotId: Int) {
                if (slotId == destinations.size) {
                    openCategoryPicker(player)
                    return
                }
                if (slotId < 0 || slotId >= destinations.size) return
                val destination = destinations[slotId]
                DestinationTeleport(destination).teleport(player)
                player.teleportsManager.setPreviousTeleport(destination)
            }
        })
    }

    @JvmStatic
    @Subscribe
    fun onLaunch(event: ServerLaunchEvent) {
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "tele", "Opens the teleport menu") { player, _ ->
            open(player)
        }
    }
}