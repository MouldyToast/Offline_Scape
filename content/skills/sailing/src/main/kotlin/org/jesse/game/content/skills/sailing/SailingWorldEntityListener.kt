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
            Sailing.leaveHelm(boat)
        }
        if (logout) {
            // Logging out: only the varbits (the sidepanel goes with the session), as before the split.
            Docking.setAboardVarbits(player, false)
        } else {
            Docking.clearAboard(player)
        }
    }

    /** Logout lands on the nearest dock rather than the boat's root tile, which is usually open sea. */
    override fun evacuationTile(player: Player, entity: WorldEntity, logout: Boolean): Location? {
        if (!logout) {
            return null
        }
        val boat = Boats[entity] ?: return null
        return Dock.nearest(boat)?.landTile
    }

    override fun onDespawn(entity: WorldEntity) {
        val boat = Boats[entity] ?: return
        // The helmsman is normally released in onEvacuate; this covers one who left the deck earlier this tick.
        Sailing.leaveHelm(boat)
        Boats.remove(entity)
    }
}
