package org.jesse.game.content.araxxor.objects

import org.jesse.game.content.araxxor.cave_hunt.AraxyteCaveHunt
import org.jesse.game.content.slayer.RegularTask
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-14
 */
open class WebTunnel : ObjectAction {
    // The X axis we're moving the player
    private val southTunnel = Pair(3677, 3684)
    private val centerTunnel = Pair(3689, 3696)

    // The Y axis we're moving the player
    private val northTunnel = Pair(9842, 9849)

    private fun getPairForTunnel(tunnel: WorldObject): Pair<Int, Int> =
        when {
            tunnel.location.equals(3678, 9819, 0) -> southTunnel
            tunnel.location.equals(3681, 9819, 0) -> southTunnel
            tunnel.location.equals(3690, 9836, 0) -> centerTunnel
            tunnel.location.equals(3693, 9836, 0) -> centerTunnel
            tunnel.location.equals(3677, 9843, 0) -> northTunnel
            else -> northTunnel
        }

    private fun isPlayerOnAraxyteTask(player: Player): Boolean =
        player.slayer.assignment?.task == RegularTask.ARAXYTES

    private fun isPlayerEnteringSecretTunnel(player: Player): Boolean =
        player.location.x == 3682 &&
                (player.location.y == 9802 || player.location.y == 9803 || player.location.y == 9804)

    private fun showTaskRequirementMessage(player: Player) =
        player.sendMessage("You need to be on an Araxyte task to enter this cave.")

    private fun moveOutOfRoom(player: Player, destination: Int, isNorthSouth: Boolean) {
        val newLocation = if (isNorthSouth)
            Location(player.location.x, destination)
        else
            Location(destination, player.location.y)
        player.setLocation(newLocation)
    }

    private fun enterRoom(player: Player, destination: Int, isNorthSouth: Boolean) {
        val newLocation = if (isNorthSouth)
            Location(player.location.x, destination)
        else
            Location(destination, player.location.y)
        player.setLocation(newLocation)
    }

    private fun handleTunnelMovement(player: Player, pair: Pair<Int, Int>, isNorthSouth: Boolean = pair == northTunnel) {
        val currentAxisLocation = if (isNorthSouth) player.location.y else player.location.x
        val destinationOutside = pair.first
        val destinationInside = pair.second

        when {
            isPlayerEnteringSecretTunnel(player) -> AraxyteCaveHunt(player).constructRegion()
            currentAxisLocation == destinationInside -> moveOutOfRoom(player, destinationOutside, isNorthSouth)
            isPlayerOnAraxyteTask(player) -> enterRoom(player, destinationInside, isNorthSouth)
            else -> showTaskRequirementMessage(player)
        }
    }


    override fun handleObjectAction(
        player: Player?,
        tunnel: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        player ?: return; tunnel ?: return
        handleTunnelMovement(player, getPairForTunnel(tunnel))
    }

    override fun getObjects(): Array<Any> =
        arrayOf(WEB_TUNNEL_54271, WEB_TUNNEL_54272, WEB_TUNNEL_54273)
}