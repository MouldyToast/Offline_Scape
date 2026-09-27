package org.jesse.game.content.skills.sailing

import org.jesse.game.GameInterface
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.model.ui.UserInterface
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.AccessMask
import org.jesse.game.world.entity.player.Player

/**
 * The sailing sidepanel (`sailing_sidepanel`, interface 937) that replaces the combat tab while aboard.
 * Open/close sequence and every value are from the live capture (board t439 / t647, disembark t606 / t827):
 * boarding sets the boat stat varbits/varps, opens 937 on side0 with its if_setevents and switches to side0;
 * disembarking zeroes them and reopens the combat tab.
 *
 * Stats are the raft's (the only boat so far). Skiff/sloop get the raft's until their captures exist.
 */
object SailingSidepanel {
    const val INTERFACE_ID = 937

    private const val COMPONENT_SWITCH_BUTTON = 1
    const val COMPONENT_FACILITIES_CLICKLAYER = 25

    private const val SCRIPT_TOPLEVEL_SIDEBUTTON_SWITCH = 915

    // varbits
    private const val SIDEPANEL_VISIBLE = 19151
    private const val SIDEPANEL_TABS = 19152
    private const val FACILITY_HOTSPOT0 = 19156
    private const val HELM_STATUS = 19176
    private const val BOAT_HP_MAX = 19177
    private const val PLAYER_AT_HELM = 19205
    private const val PLAYERS_ON_BOARD_TOTAL = 19235
    private const val AMMO_NEEDS_UPDATE = 19236
    private const val BOAT_STATS_NEEDS_UPDATE = 19237
    private const val BOAT_BASESPEED = 19250
    private const val BOAT_SPEEDCAP = 19251
    private const val BOAT_SPEEDBOOST_DURATION = 19256
    private const val BOAT_ACCELERATION = 19257

    // varps
    private const val BOAT_TYPE = 5117
    private const val BOAT_DEFENCE = 5147
    private const val STAT_STAB = 5159
    private const val STAT_SLASH = 5160
    private const val STAT_CRUSH = 5161
    private const val STAT_MAGIC = 5162
    private const val STAT_HEAVY_RANGED = 5163
    private const val STAT_STANDARD_RANGED = 5164
    private const val STAT_LIGHT_RANGED = 5165

    /** Raft values sent on boarding (capture t439 / t647). */
    private const val RAFT_BOAT_TYPE = 8110
    private const val RAFT_HOTSPOT0 = 15
    private const val RAFT_HP = 20

    @JvmStatic
    fun open(player: Player, boat: Boat) {
        val vars = player.varManager
        val type = boat.type
        vars.sendVar(BOAT_TYPE, RAFT_BOAT_TYPE)
        vars.sendBit(PLAYERS_ON_BOARD_TOTAL, 1)
        vars.sendBit(FACILITY_HOTSPOT0, RAFT_HOTSPOT0)
        vars.sendBit(BOAT_HP_MAX, RAFT_HP)
        vars.sendBit(HELM_STATUS, 1)
        vars.sendVar(BOAT_DEFENCE, 1)
        vars.sendVar(STAT_STAB, 24)
        vars.sendVar(STAT_SLASH, 11)
        vars.sendVar(STAT_CRUSH, 6)
        vars.sendVar(STAT_HEAVY_RANGED, 4)
        vars.sendVar(STAT_STANDARD_RANGED, 13)
        vars.sendVar(STAT_LIGHT_RANGED, 26)
        vars.sendVar(STAT_MAGIC, 9)
        vars.sendBit(BOAT_BASESPEED, type.baseSpeed)
        vars.sendBit(BOAT_SPEEDCAP, type.speedCap)
        vars.sendBit(BOAT_SPEEDBOOST_DURATION, type.boostDuration)
        vars.sendBit(BOAT_ACCELERATION, type.acceleration)
        vars.sendBit(SIDEPANEL_TABS, 0)
        vars.sendBit(SIDEPANEL_VISIBLE, 1)
        vars.sendBit(AMMO_NEEDS_UPDATE, 1)
        vars.sendBit(BOAT_STATS_NEEDS_UPDATE, 1)

        player.interfaceHandler.sendInterface(InterfacePosition.COMBAT_TAB, INTERFACE_ID)
        val dispatcher = player.packetDispatcher
        dispatcher.sendComponentSettings(INTERFACE_ID, COMPONENT_SWITCH_BUTTON, 0, 12, AccessMask.CLICK_OP1)
        dispatcher.sendComponentSettings(
            INTERFACE_ID, COMPONENT_FACILITIES_CLICKLAYER, 0, 16,
            AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4,
        )
        dispatcher.sendClientScript(SCRIPT_TOPLEVEL_SIDEBUTTON_SWITCH, 0)

        // The live server drops the refresh flags again the next tick (t440 / t648).
        WorldTasksManager.schedule(1) {
            vars.sendBit(AMMO_NEEDS_UPDATE, 0)
            vars.sendBit(BOAT_STATS_NEEDS_UPDATE, 0)
        }
    }

    @JvmStatic
    fun close(player: Player) {
        val vars = player.varManager
        vars.sendVar(BOAT_TYPE, -1)
        vars.sendBit(FACILITY_HOTSPOT0, 0)
        vars.sendBit(HELM_STATUS, 0)
        vars.sendBit(PLAYER_AT_HELM, 0)
        vars.sendBit(BOAT_HP_MAX, 0)
        vars.sendVar(BOAT_DEFENCE, 0)
        vars.sendVar(STAT_STAB, 0)
        vars.sendVar(STAT_SLASH, 0)
        vars.sendVar(STAT_CRUSH, 0)
        vars.sendVar(STAT_HEAVY_RANGED, 0)
        vars.sendVar(STAT_STANDARD_RANGED, 0)
        vars.sendVar(STAT_LIGHT_RANGED, 0)
        vars.sendVar(STAT_MAGIC, 0)
        vars.sendBit(BOAT_BASESPEED, 0)
        vars.sendBit(BOAT_SPEEDBOOST_DURATION, 0)
        vars.sendBit(BOAT_SPEEDCAP, 0)
        vars.sendBit(BOAT_ACCELERATION, 0)
        vars.sendBit(PLAYERS_ON_BOARD_TOTAL, 0)
        vars.sendBit(SIDEPANEL_VISIBLE, 0)
        GameInterface.COMBAT_TAB.open(player)
    }

    /** Helm status on the sidepanel: 2 + at-helm while navigating, 1 otherwise (t444 / t600). */
    @JvmStatic
    fun setAtHelm(player: Player, atHelm: Boolean) {
        player.varManager.sendBit(PLAYER_AT_HELM, if (atHelm) 1 else 0)
        player.varManager.sendBit(HELM_STATUS, if (atHelm) 2 else 1)
    }
}

/** Sidepanel clicks. Only the facilities click layer (sail buttons) is handled so far. */
@Suppress("unused")
class SailingSidepanelInterface : UserInterface {
    override fun handleComponentClick(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        slotId: Int,
        itemId: Int,
        optionId: Int,
        option: String?,
    ) {
        when (componentId) {
            SailingSidepanel.COMPONENT_FACILITIES_CLICKLAYER -> Sailing.sidepanelButton(player, slotId)
        }
    }

    override fun getInterfaceIds(): IntArray = intArrayOf(SailingSidepanel.INTERFACE_ID)
}
