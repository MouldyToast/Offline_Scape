package org.jesse.game.content.skills.sailing

import com.google.common.eventbus.Subscribe
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.VarManager
import org.jesse.plugins.events.ServerLaunchEvent

/**
 * The player's boat record (boat slot 1), kept in the same varbits the live game uses, so the client sees
 * exactly what it expects at login. Values are the ones every capture shows for an owned raft
 * (login dumps of `Sailing_around_controls`, `sailing_straightintoland`, `sailing45tryingto`).
 *
 * The boat is moored at [port]: the last port it was docked at. It only changes on disembark (controls capture t318);
 * boarding and sailing never touch it, so a boat can only be boarded at the port it was last docked at.
 *
 * Persistence: the engine saves whole varps, so one varbit of every persistent varp is registered at launch.
 * Saved now: the boat record (sailing_boat_1_data, _name, _customisation_2, hp_storage_1/_3, boat_selection,
 * last_dock). Jagex also keeps `sailing_perm_transmit_no_protect_1..3`, but those hold the boarded state
 * (boarded_boat, boarded_boat_world) and the at-sea position - they are saved together with the login restore in
 * the boat-persistence step, so a crash while aboard can't leave stale "on a boat" state behind.
 */
object BoatOwnership {
    // sailing_boat_1_data
    private const val VARBIT_OWNED = 19258
    private const val VARBIT_TYPE = 19259
    private const val VARBIT_PORT = 19260
    private const val VARBIT_BOTTLE_PREVIOUS_PORT = 19261
    private const val VARBIT_FACILITIES_UNALTERED = 19262

    // sailing_boat_1_name
    private const val VARBIT_NAME_2 = 19264
    private const val VARBIT_NAME_3 = 19265

    // sailing_boat_1_customisation_2
    private const val VARBIT_HOTSPOT_0 = 19273

    // sailing_boat_hp_storage_1 / _3
    private const val VARBIT_STORED_HP = 19458
    private const val VARBIT_STORED_MAX_HP = 19463

    // sailing_boat_selection
    private const val VARBIT_LAST_PERSONAL_BOAT_BOARDED = 18554

    // sailing_last_dock
    private const val VARBIT_LAST_DOCK = 19145
    private const val VARBIT_LAST_STANDARD_DOCK = 19146

    // sailing_perm_transmit_no_protect_1 / _2 (not saved yet - see the class doc)
    private const val VARBIT_BOAT_SPAWNED = 19121
    private const val VARBIT_PREVIOUS_BOAT_DATA_SLOT = 19130

    /** One varbit per persistent varp: sailing_boat_1_data, _name, _customisation_2, hp_storage_1, hp_storage_3,
     *  boat_selection, last_dock. */
    private val PERSISTENT_VARPS_BY_VARBIT = intArrayOf(
        VARBIT_OWNED, VARBIT_NAME_2, VARBIT_HOTSPOT_0, VARBIT_STORED_HP, VARBIT_STORED_MAX_HP,
        VARBIT_LAST_PERSONAL_BOAT_BOARDED, VARBIT_LAST_DOCK,
    )

    // An owned raft, as in every capture. Name parts 9 + 22 ("Bladed Craft", cargo hold title) are the capture
    // account's name - a placeholder until boat naming exists.
    private const val TYPE_RAFT = 0
    private const val RAFT_HOTSPOT_0 = 15
    private const val RAFT_HP = 20
    private const val NO_PREVIOUS_PORT = 255
    private const val DEFAULT_NAME_2 = 9
    private const val DEFAULT_NAME_3 = 22

    @JvmStatic
    @Subscribe
    fun onServerLaunch(@Suppress("UNUSED_PARAMETER") event: ServerLaunchEvent) {
        for (varbit in PERSISTENT_VARPS_BY_VARBIT) {
            VarManager.appendPersistentVarbit(varbit)
        }
    }

    @JvmStatic
    fun owns(player: Player): Boolean = player.varManager.getBitValue(VARBIT_OWNED) == 1

    /** The port the player's boat is moored at, or null if they own no boat (or it is moored at an unknown dock). */
    @JvmStatic
    fun port(player: Player): Dock? = if (owns(player)) Dock.byId(player.varManager.getBitValue(VARBIT_PORT)) else null

    /**
     * Gives [player] a raft moored at [dock]. Stand-in for the sailing intro (the live game hands the raft over
     * during the Pandemonium quest); values are the owned-raft record from the captures.
     */
    @JvmStatic
    fun grantRaft(player: Player, dock: Dock) {
        val vars = player.varManager
        vars.sendBit(VARBIT_OWNED, 1)
        vars.sendBit(VARBIT_TYPE, TYPE_RAFT)
        vars.sendBit(VARBIT_PORT, dock.id)
        vars.sendBit(VARBIT_BOTTLE_PREVIOUS_PORT, NO_PREVIOUS_PORT)
        vars.sendBit(VARBIT_FACILITIES_UNALTERED, 1)
        vars.sendBit(VARBIT_NAME_2, DEFAULT_NAME_2)
        vars.sendBit(VARBIT_NAME_3, DEFAULT_NAME_3)
        vars.sendBit(VARBIT_HOTSPOT_0, RAFT_HOTSPOT_0)
        vars.sendBit(VARBIT_STORED_HP, RAFT_HP)
        vars.sendBit(VARBIT_STORED_MAX_HP, RAFT_HP)
        vars.sendBit(VARBIT_BOAT_SPAWNED, 1)
        vars.sendBit(VARBIT_LAST_PERSONAL_BOAT_BOARDED, 1)
        vars.sendBit(VARBIT_PREVIOUS_BOAT_DATA_SLOT, 1)
    }

    /** Moors the boat at [dock] (disembark, controls capture t318: port + last dock + last standard dock). */
    @JvmStatic
    fun moor(player: Player, dock: Dock) {
        val vars = player.varManager
        vars.sendBit(VARBIT_PORT, dock.id)
        vars.sendBit(VARBIT_LAST_DOCK, dock.id)
        vars.sendBit(VARBIT_LAST_STANDARD_DOCK, dock.id)
    }

    /** Boat name parts 2 and 3 (`sailing_boat_1_name_2/3`); a boat spawned without ownership (`::boat`) uses the default. */
    @JvmStatic
    fun nameParts(player: Player): Pair<Int, Int> =
        if (owns(player)) {
            player.varManager.getBitValue(VARBIT_NAME_2) to player.varManager.getBitValue(VARBIT_NAME_3)
        } else {
            DEFAULT_NAME_2 to DEFAULT_NAME_3
        }

    @JvmStatic
    fun hotspot0(player: Player): Int =
        if (owns(player)) player.varManager.getBitValue(VARBIT_HOTSPOT_0) else RAFT_HOTSPOT_0

    @JvmStatic
    fun storedHp(player: Player): Int = if (owns(player)) player.varManager.getBitValue(VARBIT_STORED_HP) else RAFT_HP

    @JvmStatic
    fun storedMaxHp(player: Player): Int =
        if (owns(player)) player.varManager.getBitValue(VARBIT_STORED_MAX_HP) else RAFT_HP
}
