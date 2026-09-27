package org.jesse.game.content.skills.sailing

import org.jesse.game.GameInterface
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.model.ui.UserInterface
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.AccessMask
import org.jesse.game.world.entity.player.Player

/**
 * The sailing sidepanel (`sailing_sidepanel`, interface 937) that replaces the combat tab while aboard.
 * Open/close sequence and every value are from the live captures (controls capture board t32-33 / t329-330,
 * disembark t319-320; session 1 board t439 / t647, disembark t606 / t827):
 * - board tick: boarding / boat varbits, sidepanel switch, 937 on side0 with its 7 if_setevents, captain name,
 *   camera zoom limits;
 * - next tick: the refresh flags drop and the boat stats go out (varps 5147-5165, speed varbits);
 * - disembark: everything zeroed, captain + navigator cleared, zoom limits restored, combat tab reopened;
 *   `boat_stats_needs_update` raised the tick after.
 *
 * Stats are the raft's (the only boat so far). Skiff/sloop get the raft's until their captures exist.
 * Not sent (no content yet): `toplevel:stone0` if_setevents -1..-1 (pane id depends on the display mode),
 * the `script1846` seq prefetch list for sea creatures, the ocean music track.
 */
object SailingSidepanel {
    const val INTERFACE_ID = 937

    private const val COMPONENT_SWITCH_BUTTON = 1
    private const val COMPONENT_CREW_CONTENT_CLICKLAYER = 10
    private const val COMPONENT_CREW_ASSIGNATION_CLICKLAYER = 20
    const val COMPONENT_FACILITIES_CLICKLAYER = 25
    private const val COMPONENT_FACILITIES_NPC_TARGETLAYER = 26
    private const val COMPONENT_CREW_ASSIGNATION_BACK_BUTTON = 29
    private const val COMPONENT_CREW_RADIO_BUTTONS = 38

    private const val SCRIPT_TOPLEVEL_SIDEBUTTON_SWITCH = 915

    /** `script8776` -> script8777: (captain name, set captain, navigator name, set navigator). */
    private const val SCRIPT_CREW_NAMES_BOARD = 8776

    /** `script603` -> script604 / camera_do_zoom: camera zoom limits (small min, small max, big min, big max). */
    private const val SCRIPT_CAMERA_ZOOM_LIMITS = 603

    // varbits
    private const val PLAYER_ROLE = 19233
    private const val BOARDED_BOAT_NAME_2 = 19149
    private const val BOARDED_BOAT_NAME_3 = 19150
    private const val PRELOADED_ANIMS = 19118
    private const val SIDEPANEL_VISIBLE = 19151
    private const val SIDEPANEL_TABS = 19152
    private const val FACILITY_HOTSPOT0 = 19156
    private const val SAIL_BUTTON_TOGGLED = 19174
    private const val BOAT_MOVE_MODE = 19175
    private const val HELM_STATUS = 19176
    private const val BOAT_HP_MAX = 19177
    private const val BOAT_HP = 19181
    private const val PLAYER_AT_HELM = 19205
    private const val REPAIRKITS = 19210
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

    /**
     * Raft values sent on boarding. Player role 10 = owner/captain; boat name parts 9 + 22 = the capture account's
     * boat name ("Bladed Craft", cargo hold title t336) - placeholders until boat naming exists;
     * HP 20 and repair kits 10 are the capture account's values.
     */
    private const val RAFT_BOAT_TYPE = 8110
    private const val RAFT_HOTSPOT0 = 15
    private const val RAFT_HP = 20
    private const val ROLE_OWNER = 10
    private const val NAME_PART_2 = 9
    private const val NAME_PART_3 = 22
    private const val REPAIR_KITS = 10

    @JvmStatic
    fun open(player: Player, boat: Boat) {
        val vars = player.varManager
        vars.sendBit(BOARDED_BOAT_NAME_2, NAME_PART_2)
        vars.sendBit(BOARDED_BOAT_NAME_3, NAME_PART_3)
        vars.sendBit(PLAYER_ROLE, ROLE_OWNER)
        vars.sendVar(BOAT_TYPE, RAFT_BOAT_TYPE)
        vars.sendBit(BOAT_MOVE_MODE, boat.moveMode)
        vars.sendBit(PLAYERS_ON_BOARD_TOTAL, 1)
        vars.sendBit(FACILITY_HOTSPOT0, RAFT_HOTSPOT0)
        vars.sendBit(BOAT_HP_MAX, RAFT_HP)
        vars.sendBit(BOAT_HP, RAFT_HP)
        vars.sendBit(HELM_STATUS, 1)
        vars.sendBit(PLAYER_AT_HELM, 0)
        vars.sendBit(REPAIRKITS, REPAIR_KITS)
        vars.sendBit(SIDEPANEL_TABS, 0)
        vars.sendBit(SIDEPANEL_VISIBLE, 1)
        vars.sendBit(AMMO_NEEDS_UPDATE, 1)
        vars.sendBit(BOAT_STATS_NEEDS_UPDATE, 1)
        vars.sendBit(PRELOADED_ANIMS, 1)

        val dispatcher = player.packetDispatcher
        dispatcher.sendClientScript(SCRIPT_CREW_NAMES_BOARD, player.name, 1, "", 1)
        dispatcher.sendClientScript(SCRIPT_TOPLEVEL_SIDEBUTTON_SWITCH, 0)
        player.interfaceHandler.sendInterface(InterfacePosition.COMBAT_TAB, INTERFACE_ID)
        dispatcher.sendComponentSettings(INTERFACE_ID, COMPONENT_SWITCH_BUTTON, 0, 12, AccessMask.CLICK_OP1)
        dispatcher.sendComponentSettings(
            INTERFACE_ID, COMPONENT_FACILITIES_CLICKLAYER, 0, 16,
            AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4,
        )
        dispatcher.sendComponentSettings(INTERFACE_ID, COMPONENT_FACILITIES_NPC_TARGETLAYER, 0, 16, AccessMask.USE_ON_NPCS)
        dispatcher.sendComponentSettings(INTERFACE_ID, COMPONENT_CREW_ASSIGNATION_CLICKLAYER, 0, 16, AccessMask.CLICK_OP1)
        dispatcher.sendComponentSettings(INTERFACE_ID, COMPONENT_CREW_ASSIGNATION_BACK_BUTTON, 0, 11, AccessMask.CLICK_OP1)
        dispatcher.sendComponentSettings(INTERFACE_ID, COMPONENT_CREW_CONTENT_CLICKLAYER, 0, 1, AccessMask.USE_ON_PLAYERS)
        dispatcher.sendComponentSettings(INTERFACE_ID, COMPONENT_CREW_RADIO_BUTTONS, 0, 3, AccessMask.CLICK_OP1)
        dispatcher.sendClientScript(SCRIPT_CAMERA_ZOOM_LIMITS, -100, 896, -100, 896)

        // Next tick: refresh flags drop and the stats go out (controls capture t33).
        WorldTasksManager.schedule(1) {
            if (player.isFinished) {
                return@schedule
            }
            vars.sendBit(AMMO_NEEDS_UPDATE, 0)
            vars.sendBit(BOAT_STATS_NEEDS_UPDATE, 0)
            sendStats(player, boat)
        }
    }

    private fun sendStats(player: Player, boat: Boat) {
        val vars = player.varManager
        val type = boat.type
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
    }

    @JvmStatic
    fun close(player: Player) {
        val vars = player.varManager
        vars.sendVar(BOAT_TYPE, -1)
        vars.sendBit(FACILITY_HOTSPOT0, 0)
        vars.sendBit(HELM_STATUS, 0)
        vars.sendBit(PLAYER_AT_HELM, 0)
        vars.sendBit(BOAT_MOVE_MODE, 0)
        vars.sendBit(SAIL_BUTTON_TOGGLED, 0)
        vars.sendBit(REPAIRKITS, 0)
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
        val dispatcher = player.packetDispatcher
        dispatcher.sendClientScript(SCRIPT_CAMERA_ZOOM_LIMITS, 128, 896, 128, 896)
        dispatcher.sendClientScript(Sailing.SCRIPT_CREW_NAMES, "", 1, "", 1)
        GameInterface.COMBAT_TAB.open(player)
        WorldTasksManager.schedule(1) {
            if (!player.isFinished) {
                vars.sendBit(BOAT_STATS_NEEDS_UPDATE, 1)
            }
        }
    }

    /** Helm status on the sidepanel: 2 + at-helm while navigating, 1 otherwise (helm on t58 / off t49). */
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
