package org.jesse.game.content.skills.sailing

import com.google.common.eventbus.Subscribe
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.worldentity.WorldEntities
import org.jesse.game.world.entity.worldentity.WorldEntity
import org.jesse.game.world.entity.worldentity.WorldEntityListener
import org.jesse.plugins.events.ServerLaunchEvent

/**
 * Sailing's hooks into the engine world entity lifecycle. Every hook ignores world entities that are not
 * sailing [Boat]s. Engine order on a despawn: [onEvacuate] per passenger (still aboard) -> [onDespawn].
 * On logout: [evacuationTile] + [onEvacuate] (logout = true) for the player, then their boats despawn.
 */
object SailingWorldEntityListener : WorldEntityListener {

    @JvmStatic
    @Subscribe
    fun onServerLaunch(@Suppress("UNUSED_PARAMETER") event: ServerLaunchEvent) {
        WorldEntities.addListener(this)
    }

    override fun onTick(entity: WorldEntity) {
        val boat = Boats[entity] ?: return
        Sailing.tick(boat)
    }

    override fun onSetHeading(player: Player, entity: WorldEntity, heading: Int) {
        val boat = Boats[entity] ?: return
        Sailing.onSetHeading(player, boat, heading)
    }

    override fun onEvacuate(player: Player, entity: WorldEntity, logout: Boolean) {
        val boat = Boats[entity] ?: return
        if (boat.helmsman === player) {
            // The sidepanel is closed right after (or goes with the session), so no deferred panel update.
            Sailing.leaveHelm(boat, updatePanel = false)
        }
        if (logout) {
            // Logging out: only the varbits (the sidepanel goes with the session), as before the split.
            Docking.setAboardVarbits(player, false)
        } else {
            Docking.clearAboard(player)
        }
    }

    /**
     * Logout saves the player on the land tile of the port their boat is moored at (45 capture login: the login
     * rebuild is at the Port Sarim land tile (3050, 3193), `sailing_boat_1_port` = Port Sarim), not on the deck.
     */
    override fun evacuationTile(player: Player, entity: WorldEntity, logout: Boolean): Location? {
        if (!logout) {
            return null
        }
        val boat = Boats[entity] ?: return null
        return (BoatOwnership.port(player) ?: Dock.nearest(boat))?.landTile
    }

    override fun onDespawn(entity: WorldEntity) {
        val boat = Boats[entity] ?: return
        // The helmsman is normally released in onEvacuate; this covers one who left the deck earlier this tick.
        Sailing.leaveHelm(boat, updatePanel = false)
        Boats.remove(entity)
    }
}