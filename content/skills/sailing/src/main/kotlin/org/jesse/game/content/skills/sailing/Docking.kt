package org.jesse.game.content.skills.sailing

import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.cutscene.FadeScreen

/**
 * Boarding and disembarking, following the live sequence (capture t436-440 board, t605-607 disembark):
 * fade out, one tick later teleport + boarding varbits + message, then fade in.
 * Boats stay where they are after disembarking (re-boarding at t647 needed no rebuild).
 */
object Docking {
    private const val VARBIT_PLAYER_IS_ON_PLAYER_BOAT = 19104
    private const val VARBIT_BOARDED_BOAT_WORLD = 19122
    private const val VARBIT_BOARDED_BOAT = 19136

    /** Gangplank "Board" from land: spawn the player's boat at this dock if needed, then board it. */
    @JvmStatic
    fun boardAtDock(player: Player, dock: Dock) {
        val existing = Boats.ownedBy(player.index)
        if (existing != null && !dock.isNear(existing)) {
            player.sendMessage("Your boat isn't docked here.")
            return
        }
        fadeThen(player) {
            val boat = Boats.ownedBy(player.index)
                ?: Boats.spawn(BoatType.RAFT, player.index, dock.seaTileX, dock.seaTileZ, 0, dock.rotation)
            if (boat == null) {
                player.sendMessage("Your boat could not be launched right now.")
                return@fadeThen
            }
            enterBoat(player, boat)
        }
    }

    /** Gangplank "Disembark", clicked from the deck (the gangplank is a root-world loc). */
    @JvmStatic
    fun disembarkAtDock(player: Player, boat: Boat, dock: Dock) {
        if (!dock.isNear(boat)) {
            player.sendMessage("You need to bring the boat closer to the dock.")
            return
        }
        if (boat.helmsman === player) {
            Sailing.leaveHelm(boat)
        }
        Sailing.stop(boat)
        fadeThen(player) {
            exitBoat(player, dock.landTile)
            player.sendMessage("You dock the boat at ${dock.displayName} and disembark.")
        }
    }

    /** Teleports [player] onto [boat]'s deck and sets the boarding varbits (no fade - see [boardAtDock]). */
    @JvmStatic
    fun enterBoat(player: Player, boat: Boat) {
        player.setLocation(boat.entity.boardTile)
        setAboardVarbits(player, true)
        SailingSidepanel.open(player, boat)
        player.sendMessage("You board your boat.")
    }

    /** Teleports [player] off a deck to [tile] and clears the boarding state (no fade). */
    @JvmStatic
    fun exitBoat(player: Player, tile: Location) {
        player.setLocation(tile)
        clearAboard(player)
    }

    /** Clears the boarding varbits and closes the sidepanel, without moving the player. */
    @JvmStatic
    fun clearAboard(player: Player) {
        setAboardVarbits(player, false)
        SailingSidepanel.close(player)
    }

    /**
     * Boarding varbits the live server sets on board / clears on disembark (t439 / t606). 19104 also flips
     * the gangplank multiloc between Board and Disembark.
     */
    @JvmStatic
    fun setAboardVarbits(player: Player, aboard: Boolean) {
        player.varManager.sendBit(VARBIT_PLAYER_IS_ON_PLAYER_BOAT, if (aboard) 1 else 0)
        player.varManager.sendBit(VARBIT_BOARDED_BOAT, if (aboard) 1 else 0)
        player.varManager.sendBit(VARBIT_BOARDED_BOAT_WORLD, if (aboard) player.worldId else 0)
    }

    private inline fun fadeThen(player: Player, crossinline action: () -> Unit) {
        player.lock(2)
        FadeScreen(player).fade(2)
        WorldTasksManager.schedule(1) {
            action()
        }
    }
}
