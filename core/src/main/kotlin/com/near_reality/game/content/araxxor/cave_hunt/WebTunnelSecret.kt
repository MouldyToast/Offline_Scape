package com.near_reality.game.content.araxxor.cave_hunt

import com.near_reality.game.content.araxxor.objects.WebTunnel
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId.*
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-14
 */
class WebTunnelSecret : WebTunnel(), ObjectAction {
    override fun handleObjectAction(
        player: Player?,
        tunnel: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        player ?: return
        tunnel ?: return
        // if we're not already in the cave, start it
        if (player.location.equals(3862, 9802, 0) &&
            tunnel.id == WEB_TUNNEL_54271 &&
            player.mapInstance !is AraxyteCaveHunt
        ) {
            AraxyteCaveHunt(player).constructRegion()
            return
        }
        // If we're nulled instance, but still in the region, take us out
        if (player.mapInstance == null && player.location.regionId == 15002) {
            player.setLocation(AraxyteCaveHunt.exitLocation)
            return
        }
        val instance = player.mapInstance as AraxyteCaveHunt
        if (tunnel.id == instance.correctTunnel && instance.roomCompleted) {
            instance.progressCaveHunt()
            player.setLocation(instance.entryTile)
        }
        if (tunnel.id == instance.wrongTunnel) {
            AraxyteCaveHunt(player).constructRegion()
            player.dialogue { plain("You get lost and find yourself back at the beginning...") }
        }
    }

    override fun getObjects(): Array<Any> =
        arrayOf(
            WEB_TUNNEL_54155,
            WEB_TUNNEL_54159,
            WEB_TUNNEL_54271
        )
}