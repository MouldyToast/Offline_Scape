package org.jesse.game.content.skills.sailing

import com.google.common.eventbus.Subscribe
import org.jesse.game.world.entity.player.GameCommands.Command
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.plugins.events.ServerLaunchEvent

/** Sailing developer commands (moved out of the engine's DeveloperCommands). */
object SailingCommands {

    @JvmStatic
    @Subscribe
    fun onServerLaunch(@Suppress("UNUSED_PARAMETER") event: ServerLaunchEvent) {
        Command(PlayerPrivilege.DEVELOPER, "boat", "Toggle a test boat at your tile. Args: [raft|skiff|sloop] [angle 0-2047]") { player, args ->
            val existing = Boats.ownedBy(player.index)
            if (existing != null) {
                Boats.despawn(existing)
                player.sendMessage("Despawned ${existing.type} (world entity ${existing.entity.index}).")
                return@Command
            }
            val type = BoatType.byName(args.getOrNull(0) ?: "raft")
            if (type == null) {
                player.sendMessage("Unknown boat type. Use raft, skiff or sloop.")
                return@Command
            }
            val angle = args.getOrNull(1)?.toIntOrNull() ?: 0
            val boat = Boats.spawn(type, player.index, player.x, player.y, player.plane, angle)
            if (boat == null) {
                player.sendMessage("Failed to spawn $type - see server log.")
                return@Command
            }
            val entity = boat.entity
            player.sendMessage("Spawned $type as world entity ${entity.index} at ${player.x}, ${player.y} (deck zone ${entity.instanceZoneX}, ${entity.instanceZoneZ}).")
        }

        Command(PlayerPrivilege.DEVELOPER, "board", "Board your ::boat (teleports onto its deck).") { player, _ ->
            val boat = Boats.ownedBy(player.index)
            if (boat == null) {
                player.sendMessage("You have no boat. Spawn one with ::boat.")
                return@Command
            }
            if (Boats.at(player.location) === boat) {
                player.sendMessage("You are already on your boat.")
                return@Command
            }
            Docking.enterBoat(player, boat)
        }

        Command(PlayerPrivilege.DEVELOPER, "disembark", "Leave the boat you are standing on.") { player, _ ->
            val boat = Boats.at(player.location)
            if (boat == null) {
                player.sendMessage("You are not on a boat.")
                return@Command
            }
            Docking.exitBoat(player, boat.entity.rootTile)
            player.sendMessage("You disembark.")
        }
    }
}
