package org.jesse.game.content.skills.sailing

import com.google.common.eventbus.Subscribe
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.persistentAttribute
import org.jesse.game.world.entity.player.Player
import org.jesse.plugins.events.LoginEvent

/*
 * Where the player's boat is while it is out of port: written while the owner is boarded (every 5 ticks, and at
 * logout), cleared when they leave the boat without logging out (disembark, [Docking.clearAboard]) or moor it.
 * Stored as fine coordinates so the boat comes back on exactly the same spot (45 capture: logout at (3091.5, 3164.0)
 * angle 1536, relog rebuilds it at (3091.5, 3164.0) angle 1536).
 */
private var Player.boatAtSeaLevel by persistentAttribute<Int?>("sailing_boat_at_sea_level", null)
private var Player.boatAtSeaFineX by persistentAttribute<Int?>("sailing_boat_at_sea_fine_x", null)
private var Player.boatAtSeaFineZ by persistentAttribute<Int?>("sailing_boat_at_sea_fine_z", null)
private var Player.boatAtSeaAngle by persistentAttribute<Int?>("sailing_boat_at_sea_angle", null)

/**
 * Keeps the player's boat at sea across logout and puts them back aboard on login, like the live game
 * (`sailing_straightintoland` logout -> `sailing45tryingto` login, same boat):
 * - logout: the player is saved on the land tile of the port the boat is moored at
 *   ([SailingWorldEntityListener.evacuationTile]), the boat's position is recorded here, the boat despawns;
 * - login: the player logs in on that land tile; the boat is rebuilt at the recorded position with a new
 *   world entity and deck, the player is teleported onto the deck board tile, the boarding varbits are set and the
 *   sidepanel opens with move mode 4 (fresh spawn). Live sends no board message, synth or fade for this.
 *
 * Unverified: logging out while the boat is still moving (recorded where it is at logout), logging in on another
 * world (`sailing_boarded_boat_world`), and whether a boat left at sea ever returns to port by itself.
 */
object BoatPersistence {

    /**
     * Records [boat]'s current position for [player] if they own it and are boarded on it. "Boarded" is the on-boat
     * varbit, not the tile: while disembarking the player still stands on the deck for a tick or two after the
     * boat was moored, and must not be recorded as at sea again.
     */
    @JvmStatic
    fun recordIfAboard(player: Player, boat: Boat) {
        val entity = boat.entity
        if (entity.ownerIndex != player.index || !BoatOwnership.owns(player)) {
            return
        }
        if (!Docking.isAboard(player) || !entity.containsDeckTile(player.location)) {
            return
        }
        player.boatAtSeaLevel = entity.level
        player.boatAtSeaFineX = entity.fineX
        player.boatAtSeaFineZ = entity.fineZ
        player.boatAtSeaAngle = entity.angle
    }

    /** The boat is moored (or gone): nothing to restore. */
    @JvmStatic
    fun clear(player: Player) {
        player.boatAtSeaLevel = null
        player.boatAtSeaFineX = null
        player.boatAtSeaFineZ = null
        player.boatAtSeaAngle = null
    }

    @JvmStatic
    @Subscribe
    fun onLogin(event: LoginEvent) {
        val player = event.player
        val level = player.boatAtSeaLevel
        val fineX = player.boatAtSeaFineX
        val fineZ = player.boatAtSeaFineZ
        val angle = player.boatAtSeaAngle
        if (level == null || fineX == null || fineZ == null || angle == null || !BoatOwnership.owns(player)) {
            clear(player)
            // Boarded state is saved (perm varps); with no boat to restore it must not survive a crash while aboard.
            Docking.setAboardVarbits(player, false)
            return
        }
        // Login runs on the login thread; world entities are spawned on the world thread.
        WorldTasksManager.schedule(0) {
            restore(player, level, fineX, fineZ, angle)
        }
    }

    private fun restore(player: Player, level: Int, fineX: Int, fineZ: Int, angle: Int) {
        if (player.isFinished) {
            return
        }
        val boat = Boats.spawn(BoatType.RAFT, player.index, fineX shr 7, fineZ shr 7, level, angle)
        if (boat == null) {
            // No world entity index / map space: leave the player on the port land tile they logged in on.
            Docking.setAboardVarbits(player, false)
            return
        }
        // Spawned on the tile centre; put the pivot back on the exact fine coordinate (no observers yet).
        boat.entity.moveTo(level, fineX, fineZ, true)
        player.setLocation(boat.entity.boardTile)
        Docking.setAboardVarbits(player, true)
        Sailing.sendSpawnedPosition(player, boat)
        SailingSidepanel.open(player, boat)
    }
}
