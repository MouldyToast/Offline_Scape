package org.jesse.game.content.skills.sailing

import org.jesse.game.model.MinimapState
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.player.Player

/**
 * Boarding and disembarking, following the live sequences (controls capture board t31-34 / t328-331,
 * disembark t318-321; session 1 t436-440 / t605-607):
 * - tick N (op): fade out (fade_overlay 174, script 948 [0,255,0,0,15]), minimap off; disembark also lowers
 *   the sails (normal deceleration - the boat is not stopped) and records the dock;
 * - tick N+1: teleport, boarding varbits, sidepanel open/close, message, synth 10754;
 * - tick N+2: fade in (script 948 [0,0,0,255,15]), minimap on;
 * - tick N+3: fade overlay closed.
 * Boats stay where they are after disembarking and keep drifting to a stop.
 */
object Docking {
    private const val VARBIT_PLAYER_IS_ON_PLAYER_BOAT = 19104
    private const val VARBIT_BOARDED_BOAT_WORLD = 19122
    private const val VARBIT_BOARDED_BOAT = 19136
    private const val VARBIT_BOAT_1_PORT = 19260
    private const val VARBIT_LAST_DOCK = 19145
    private const val VARBIT_LAST_STANDARD_DOCK = 19146

    private const val FADE_OVERLAY = 174
    private const val SCRIPT_FADE_OVERLAY = 948
    private const val FADE_CYCLES = 15
    private const val SYNTH_BOARD = 10754

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
            Sailing.lowerSails(boat)
        }
        val vars = player.varManager
        vars.sendBit(VARBIT_BOAT_1_PORT, dock.id)
        vars.sendBit(VARBIT_LAST_DOCK, dock.id)
        vars.sendBit(VARBIT_LAST_STANDARD_DOCK, dock.id)
        fadeThen(player) {
            if (boat.helmsman === player) {
                Sailing.leaveHelm(boat, updatePanel = false)
            }
            player.sendMessage("You dock the boat at ${dock.displayName} and disembark.")
            exitBoat(player, dock.landTile)
        }
    }

    /** Teleports [player] onto [boat]'s deck and sets the boarding varbits (no fade - see [boardAtDock]). */
    @JvmStatic
    fun enterBoat(player: Player, boat: Boat) {
        player.sendMessage("You board your boat.")
        player.setLocation(boat.entity.boardTile)
        setAboardVarbits(player, true)
        Sailing.sendSpawnedPosition(player, boat)
        SailingSidepanel.open(player, boat)
        player.packetDispatcher.sendSoundEffect(SoundEffect(SYNTH_BOARD))
    }

    /** Teleports [player] off a deck to [tile] and clears the boarding state (no fade). */
    @JvmStatic
    fun exitBoat(player: Player, tile: Location) {
        player.setLocation(tile)
        clearAboard(player)
        player.packetDispatcher.sendSoundEffect(SoundEffect(SYNTH_BOARD))
    }

    /** Clears the boarding varbits and closes the sidepanel, without moving the player. */
    @JvmStatic
    fun clearAboard(player: Player) {
        setAboardVarbits(player, false)
        SailingSidepanel.close(player)
    }

    /**
     * Boarding varbits the live server sets on board / clears on disembark. 19104 also flips
     * the gangplank multiloc between Board and Disembark.
     */
    @JvmStatic
    fun setAboardVarbits(player: Player, aboard: Boolean) {
        player.varManager.sendBit(VARBIT_PLAYER_IS_ON_PLAYER_BOAT, if (aboard) 1 else 0)
        player.varManager.sendBit(VARBIT_BOARDED_BOAT, if (aboard) 1 else 0)
        player.varManager.sendBit(VARBIT_BOARDED_BOAT_WORLD, if (aboard) player.worldId else 0)
    }

    private inline fun fadeThen(player: Player, crossinline action: () -> Unit) {
        player.lock(3)
        fadeOut(player)
        WorldTasksManager.schedule(1) {
            action()
            WorldTasksManager.schedule(1) {
                fadeIn(player)
            }
        }
    }

    private fun fadeOut(player: Player) {
        player.interfaceHandler.sendInterface(InterfacePosition.OVERLAY, FADE_OVERLAY)
        player.packetDispatcher.sendClientScript(SCRIPT_FADE_OVERLAY, 0, 255, 0, 0, FADE_CYCLES)
        player.packetDispatcher.sendMinimapState(MinimapState.MAP_DISABLED)
    }

    private fun fadeIn(player: Player) {
        if (player.isFinished) {
            return
        }
        player.packetDispatcher.sendClientScript(SCRIPT_FADE_OVERLAY, 0, 0, 0, 255, FADE_CYCLES)
        player.packetDispatcher.sendMinimapState(MinimapState.ENABLED)
        WorldTasksManager.schedule(1) {
            if (!player.isFinished) {
                player.interfaceHandler.closeInterface(InterfacePosition.OVERLAY)
            }
        }
    }
}
