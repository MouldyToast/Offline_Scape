package org.jesse.game.content.skills.sailing

import com.google.common.eventbus.Subscribe
import org.jesse.game.world.entity.player.GameCommands.Command
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.plugins.events.ServerLaunchEvent
import kotlin.math.abs
import kotlin.math.max

/** Sailing developer commands (moved out of the engine's DeveloperCommands). */
object SailingCommands {

    @JvmStatic
    @Subscribe
    fun onServerLaunch(@Suppress("UNUSED_PARAMETER") event: ServerLaunchEvent) {
        Command(
            PlayerPrivilege.DEVELOPER,
            "boat",
            "Moor your raft at the nearest dock and spawn it at the berth. Args: [here [raft|skiff|sloop] [angle 0-2047] | off]",
        ) { player, args ->
            when (args.getOrNull(0)?.lowercase()) {
                null -> mooredAtNearestDock(player)
                "here" -> atFeet(player, args.getOrNull(1), args.getOrNull(2))
                "off" -> {
                    val existing = Boats.ownedBy(player.index)
                    if (existing == null) {
                        player.sendMessage("You have no boat spawned.")
                    } else {
                        Boats.despawn(existing)
                        player.sendMessage("Despawned ${existing.type} (world entity ${existing.entity.index}).")
                    }
                }
                else -> player.sendMessage("Usage: ::boat | ::boat here [raft|skiff|sloop] [angle] | ::boat off")
            }
        }

        Command(PlayerPrivilege.DEVELOPER, "board", "Board your boat (teleports onto its deck, no gangplank or port check).") { player, _ ->
            val boat = Boats.ownedBy(player.index)
            if (boat == null) {
                player.sendMessage("You have no boat spawned. Use ::boat to moor one at the nearest dock.")
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
            if (boat.helmsman === player) {
                Sailing.leaveHelm(boat, updatePanel = false)
            }
            Docking.exitBoat(player, boat.entity.rootTile)
            player.sendMessage("You disembark.")
        }
    }

    /**
     * `::boat`: moors the player's raft at the dock nearest to them (grants one if they own none) and spawns it at that
     * dock's berth, so the dock's gangplank can be tested straight away. Replaces any boat they already have out.
     */
    private fun mooredAtNearestDock(player: Player) {
        val location = player.location
        if (Docking.isAboard(player) || Boats.at(location) != null) {
            player.sendMessage("Get off your boat first.")
            return
        }
        val dock = Dock.entries.minByOrNull { max(abs(it.gangplankX - location.x), abs(it.gangplankZ - location.y)) }
        if (dock == null) {
            player.sendMessage("There are no docks.")
            return
        }
        Boats.ownedBy(player.index)?.let { Boats.despawn(it) }
        if (BoatOwnership.owns(player)) {
            BoatOwnership.moor(player, dock)
        } else {
            BoatOwnership.grantRaft(player, dock)
        }
        val boat = Boats.spawn(BoatType.RAFT, player.index, dock.seaTileX, dock.seaTileZ, 0, dock.rotation)
        if (boat == null) {
            player.sendMessage("Your raft is moored at ${dock.displayName}, but it could not be spawned - see server log.")
            return
        }
        player.sendMessage(
            "Your raft is moored at ${dock.displayName} (dock ${dock.id}): berth ${dock.seaTileX}, ${dock.seaTileZ}, " +
                    "angle ${dock.rotation}. Gangplank at ${dock.gangplankX}, ${dock.gangplankZ}.",
        )
    }

    /** `::boat here`: the old test spawn - a boat of [typeName] on the player's own tile, no dock or ownership. */
    private fun atFeet(player: Player, typeName: String?, angleArg: String?) {
        Boats.ownedBy(player.index)?.let { Boats.despawn(it) }
        val type = BoatType.byName(typeName ?: "raft")
        if (type == null) {
            player.sendMessage("Unknown boat type. Use raft, skiff or sloop.")
            return
        }
        val angle = angleArg?.toIntOrNull() ?: 0
        val boat = Boats.spawn(type, player.index, player.x, player.y, player.plane, angle)
        if (boat == null) {
            player.sendMessage("Failed to spawn $type - see server log.")
            return
        }
        val entity = boat.entity
        player.sendMessage(
            "Spawned $type as world entity ${entity.index} at ${player.x}, ${player.y} " +
                    "(deck zone ${entity.instanceZoneX}, ${entity.instanceZoneZ}).",
        )
    }
}