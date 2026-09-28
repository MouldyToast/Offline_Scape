package org.jesse.game.content.skills.sailing

import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.masks.UpdateFlag
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.worldentity.DeckLoc
import org.jesse.game.world.entity.worldentity.WorldEntityCollision
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToLong
import kotlin.math.sin

/**
 * Helm, sails, move modes, wind gusts + trim, steering and per-tick boat movement.
 *
 * Sources: the Pandemonium capture (session 1) and `Sailing_around_controls.txt` ("controls capture", rev 240.1,
 * tick numbers below refer to it unless marked otherwise). See investigation_sailing_controls_capture_v1.md.
 *
 * Move modes (varbit 19175) form a ladder: 3 reverse <- 0 sails down -> 1 half sails -> 2 full sails;
 * 4 = at the helm while idle (a freshly spawned boat, or taking the helm with the sails down).
 * Speed moves toward the mode's target by `acceleration` per tick: full = base, half = 64, down/4 = 0, a trim boost
 * adds 64 at full or half (halfsails capture t76-t95: 128);
 * reverse is a constant -64 from its first tick and leaving it stops dead (t152, t170).
 * Movement: fine coords += round_to_32(speed * (-sin, -cos)(angle)) - fits every controls-capture tick
 * outside land contact.
 *
 * Land collision: [WorldEntityCollision] (rotated hull footprint vs blocked tiles, slide z then x); a boat that
 * cannot move at all drops to speed 0 and re-accelerates.
 *
 * Unverified / not implemented: gust timing beyond the few samples (44-62 running ticks after setting sails, 49
 * after an untrimmed gust, 30 after a boost; the timer pauses while the sails are down - randomised here),
 * sub 0 from modes 1/2 and sub 2 from modes 2/3 (never pressed in a capture).
 */
object Sailing {
    const val MODE_IDLE = 0
    const val MODE_HALF = 1
    const val MODE_SAILS = 2
    const val MODE_REVERSE = 3
    const val MODE_HELM_IDLE = 4

    private const val VARBIT_FACILITY_LOCKEDIN = 19105
    private const val VARBIT_SAIL_BUTTON_TOGGLED = 19174
    private const val VARBIT_BOAT_MOVE_MODE = 19175
    private const val VARBIT_SPAWNED_ANGLE = 19129
    private const val VARBIT_SPAWNED_FINEX = 19141
    private const val VARBIT_SPAWNED_FINEZ = 19142

    private const val WORLD_DEFAULT = -2
    private const val TILE_MODE_WALK = 1
    private const val TILE_MODE_HEADING = 2
    private const val ENTITY_MODE_ALL = 1

    private const val SCRIPT_TOPLEVEL_SIDEBUTTON_SWITCH = 915

    /** `script8778` -> script8777: (captain name, set captain, navigator name, set navigator) + crew rows refresh. */
    const val SCRIPT_CREW_NAMES = 8778

    // Synths (controls capture). Sail sounds depend only on the sail state change.
    private const val SYNTH_HELM_ON = 10792
    private const val SYNTH_HELM_OFF = 10793
    private const val SYNTH_DOWN_TO_FULL = 10831
    private const val SYNTH_FULL_TO_DOWN = 10833
    private const val SYNTH_DOWN_TO_HALF = 10832
    private const val SYNTH_FULL_TO_HALF = 10834
    private const val SYNTH_HALF_TO_DOWN = 10835
    private const val SYNTH_HALF_TO_FULL = 10836
    private const val SYNTH_GUST_FULL = 10839
    private const val SYNTH_GUST_HALF = 10838
    private const val SYNTH_TRIM_1 = 10842
    private const val SYNTH_TRIM_2 = 10841

    // Op-bearing sail loc (linen sail) op masks: op1 Trim, op2 Set, op5 Un-set.
    private const val SAIL_OPS_DOWN = 0b10
    private const val SAIL_OPS_UP = 0b10000
    private const val SAIL_OPS_GUST = 0b10001

    private const val SAIL_DOWN = 0
    private const val SAIL_HALF = 1
    private const val SAIL_FULL = 2

    private const val HELM_LOOP_INTERVAL = 10
    private const val TRIM_LOOP_INTERVAL = 4
    private const val REVERSE_SPEED = 64
    private const val GUST_WINDOW = 14
    private const val GUST_AFTER_BOOST = 30
    private const val GUST_MIN = 40
    private const val GUST_MAX = 65
    private const val SPAWNED_VARBIT_INTERVAL = 5

    private const val MSG_NOT_AT_HELM = "You or a crewmate must be navigating at the helm to adjust the sails."

    /**
     * Helm "Navigate" / "Stop-navigating" (the multiloc flips its op1 label on lockedin).
     * Tick N (t57, t73): lockedin, interaction modes, face 3 tiles south, helm anims, sail state refresh,
     * sidebutton switch, synth. Tick N+1: sidepanel at-helm varbits, mode 0 -> 4, navigator name.
     */
    @JvmStatic
    fun toggleHelm(player: Player, boat: Boat) {
        val anims = boat.type.anims ?: return
        val entity = boat.entity
        if (!entity.containsDeckTile(player.location)) {
            return
        }
        val current = boat.helmsman
        if (current === player) {
            leaveHelm(boat)
            return
        }
        if (current != null) {
            player.sendMessage("Someone else is already at the helm.")
            return
        }
        boat.helmsman = player
        boat.helmTicks = 0
        boat.targetHeading = (entity.angle + 64) shr 7 and 15
        player.varManager.sendBit(VARBIT_FACILITY_LOCKEDIN, 3)
        val sender = player.packetDispatcher.sender
        sender.setInteractionMode(WORLD_DEFAULT, TILE_MODE_HEADING, ENTITY_MODE_ALL)
        sender.setInteractionMode(entity.index, TILE_MODE_WALK, ENTITY_MODE_ALL)
        faceDeckSouth(player)
        player.setAnimation(Animation(anims.playerHelmStart))
        locAnim(boat, boat.type.helmLoc, anims.helmLocStart)
        refreshSail(boat)
        player.packetDispatcher.sendClientScript(SCRIPT_TOPLEVEL_SIDEBUTTON_SWITCH, 0)
        synth(player, SYNTH_HELM_ON)
        WorldTasksManager.schedule(1) {
            if (boat.helmsman !== player || !boat.isLive) {
                return@schedule
            }
            SailingSidepanel.setAtHelm(player, true)
            if (boat.moveMode == MODE_IDLE) {
                setMode(boat, MODE_HELM_IDLE, visuals = false)
            } else {
                player.varManager.sendBit(VARBIT_BOAT_MOVE_MODE, boat.moveMode)
            }
            player.packetDispatcher.sendClientScript(SCRIPT_CREW_NAMES, "", 0, player.name, 1)
        }
    }

    /**
     * Releases the helm (Stop-navigating, walked off, disembarked, logged out, boat despawned).
     * Tick N (t48, t71): interaction modes reset, lockedin 0, player stays facing deck south, anim stop, helm inactive,
     * synth; raised sails are lowered. Tick N+1 (only when [updatePanel]): mode / at-helm varbits, appearance,
     * navigator name cleared. Disembark and evacuation pass false - the sidepanel is closed instead.
     */
    @JvmStatic
    @JvmOverloads
    fun leaveHelm(boat: Boat, updatePanel: Boolean = true) {
        val player = boat.helmsman ?: return
        boat.helmsman = null
        boat.helmTicks = 0
        player.varManager.sendBit(VARBIT_FACILITY_LOCKEDIN, 0)
        val sender = player.packetDispatcher.sender
        sender.resetInteractionMode(boat.entity.index)
        sender.setInteractionMode(WORLD_DEFAULT, TILE_MODE_WALK, ENTITY_MODE_ALL)
        faceDeckSouth(player)
        player.setAnimation(Animation.STOP)
        val anims = boat.type.anims
        if (anims != null) {
            locAnim(boat, boat.type.helmLoc, anims.helmLocInactive)
        }
        synth(player, SYNTH_HELM_OFF)
        if (boat.moveMode != MODE_IDLE) {
            // Sails drop now (anims, ops, synth) but the boat keeps its speed this tick; deceleration and the mode
            // varbit follow next tick. Full sails: controls t48-t49; half sails: halfsails t43-t44, t157-t158.
            if (boat.moveMode == MODE_SAILS || boat.moveMode == MODE_HALF) {
                boat.holdSpeed = true
            }
            setMode(boat, MODE_IDLE)
        }
        if (!updatePanel) {
            return
        }
        WorldTasksManager.schedule(1) {
            if (player.isFinished || boat.helmsman === player) {
                return@schedule
            }
            player.varManager.sendBit(VARBIT_BOAT_MOVE_MODE, boat.moveMode)
            player.varManager.sendBit(VARBIT_SAIL_BUTTON_TOGGLED, 0)
            SailingSidepanel.setAtHelm(player, false)
            player.updateFlags.flag(UpdateFlag.APPEARANCE)
            player.packetDispatcher.sendClientScript(SCRIPT_CREW_NAMES, "", 0, "", 1)
        }
    }

    /** Sails loc op2 "Set" (only offered while down) / op5 "Un-set" (offered while half, full or reversing). */
    @JvmStatic
    fun setSails(player: Player, boat: Boat, set: Boolean) {
        if (!requireHelmsman(player, boat)) {
            return
        }
        val mode = boat.moveMode
        if (set) {
            if (mode == MODE_IDLE || mode == MODE_HELM_IDLE) {
                setMode(boat, MODE_SAILS)
            }
        } else if (mode == MODE_HALF || mode == MODE_SAILS || mode == MODE_REVERSE) {
            setMode(boat, MODE_IDLE)
        }
    }

    /**
     * Sidepanel facility buttons (`sailing_sidepanel:facilities_content_clicklayer` 937:25):
     * - sub 0 toggles the sails: down/4 -> full (t76), otherwise -> down (3 -> 0 at t170);
     * - sub 1 steps down the ladder: 2 -> 1 (t137), 1 -> 0 (t139), 0/4 -> 3 (t152), 3 -> "already reversing" (t168);
     * - sub 2 steps up: 0/4 -> 1 (t174), 1 -> 2 (t190); 3 -> 0 and 2 -> no-op are unverified.
     */
    @JvmStatic
    fun sidepanelButton(player: Player, sub: Int) {
        val boat = Boats.at(player.location) ?: return
        if (!requireHelmsman(player, boat)) {
            return
        }
        val mode = boat.moveMode
        val next = when (sub) {
            0 -> if (mode == MODE_IDLE || mode == MODE_HELM_IDLE) MODE_SAILS else MODE_IDLE
            1 -> when (mode) {
                MODE_SAILS -> MODE_HALF
                MODE_HALF -> MODE_IDLE
                MODE_REVERSE -> {
                    player.sendMessage("The boat is already reversing.")
                    return
                }
                else -> MODE_REVERSE
            }
            2 -> when (mode) {
                MODE_HALF -> MODE_SAILS
                MODE_REVERSE -> MODE_IDLE
                MODE_SAILS -> return
                else -> MODE_HALF
            }
            else -> return
        }
        if (next != mode) {
            setMode(boat, next)
        }
    }

    /**
     * Sails loc op1 "Trim": only offered during a gust with half or full sails (opflags 0b10001).
     * Trim tick (t291): message, face forward, trim start anims, sail refresh (Trim removed), boost vfx, 2 synths.
     * The boost applies from the next tick for `boostDuration` ticks - see [tickWind].
     */
    @JvmStatic
    fun trim(player: Player, boat: Boat) {
        if (!requireHelmsman(player, boat)) {
            return
        }
        val anims = boat.type.anims ?: return
        val mode = boat.moveMode
        if (boat.gustTicks <= 0 || (mode != MODE_SAILS && mode != MODE_HALF)) {
            player.sendMessage("There's no wind to catch right now.")
            return
        }
        boat.gustTicks = 0
        boat.nextGustIn = -1
        boat.boostTick = 0
        player.sendMessage("You trim the sails, catching the wind for a burst of speed!")
        faceDeckSouth(player)
        player.setAnimation(Animation(anims.playerTrimStart))
        locAnim(boat, boat.type.helmLoc, anims.helmLocTrimStart)
        setSailOpFlags(boat)
        sendSteadySail(boat, sailState(mode))
        graphic(boat, boat.type.sailBLoc, anims.boostGraphic)
        synth(player, SYNTH_TRIM_1)
        synth(player, SYNTH_TRIM_2)
    }

    /**
     * SET_HEADING from the client (heading mode is only enabled for the helmsman). In mode 4 the first heading
     * also sets full sails: controls capture t40 and halfsails t12 / t48 switched 4 -> 2 with the sail-set anims and
     * synth on the same tick the boat started turning, with no op / button packet logged (RSProx does not log
     * SET_HEADING, so this is inferred - but consistent across every helm-on followed by a click on the sea).
     */
    @JvmStatic
    fun onSetHeading(player: Player, boat: Boat, heading: Int) {
        if (boat.helmsman !== player) {
            return
        }
        boat.targetHeading = heading and 15
        if (boat.moveMode == MODE_HELM_IDLE) {
            setMode(boat, MODE_SAILS)
        }
    }

    /** Lowers the sails (disembark): mode 0 and a normal deceleration, the boat is not stopped dead (t318-t320). */
    @JvmStatic
    fun lowerSails(boat: Boat) {
        if (boat.moveMode != MODE_IDLE) {
            setMode(boat, MODE_IDLE)
        }
    }

    /**
     * One game tick for [boat]: helm upkeep, wind, turning, speed, movement, saved-position varbits.
     * Called from [SailingWorldEntityListener.onTick]; the engine reloads passengers' scenes when the boat moves.
     */
    internal fun tick(boat: Boat) {
        val entity = boat.entity
        val anims = boat.type.anims
        val helmsman = boat.helmsman
        boat.ticks++
        if (helmsman != null) {
            if (!isAtHelm(helmsman, boat)) {
                leaveHelm(boat)
            } else {
                boat.helmTicks++
                if (anims != null && !boat.boosting && boat.helmTicks % HELM_LOOP_INTERVAL == 0) {
                    helmsman.setAnimation(Animation(anims.playerHelmLoop))
                    locAnim(boat, boat.type.helmLoc, anims.helmLocLoop)
                }
            }
        }

        CargoHold.tick(boat)

        val boosted = tickWind(boat)

        // Turn one step (128) towards the target heading. 180-degree ties go through increasing angles.
        val target = (boat.targetHeading and 15) shl 7
        if (entity.angle != target) {
            val diff = (target - entity.angle) and 2047
            val next = when {
                diff <= 128 || diff >= 2048 - 128 -> target
                diff <= 1024 -> entity.angle + 128
                else -> entity.angle - 128
            }
            entity.turnTo(next)
        }

        // Speed toward the mode's target.
        val type = boat.type
        val targetSpeed = when (boat.moveMode) {
            MODE_SAILS -> min(type.baseSpeed + (if (boosted) type.boostAmount else 0), type.speedCap)
            MODE_HALF -> min(type.halfSpeed + (if (boosted) type.boostAmount else 0), type.speedCap)
            else -> 0
        }
        if (boat.holdSpeed) {
            boat.holdSpeed = false
        } else {
            boat.speed = when {
                boat.moveMode == MODE_REVERSE -> -REVERSE_SPEED
                boat.speed < 0 -> 0
                boat.speed < targetSpeed -> min(boat.speed + type.acceleration, targetSpeed)
                else -> max(boat.speed - type.acceleration, targetSpeed)
            }
        }

        if (boat.ticks % SPAWNED_VARBIT_INTERVAL == 0) {
            val owner = World.getPlayers().get(entity.ownerIndex)
            if (owner != null) {
                sendSpawnedPosition(owner, boat)
                BoatPersistence.recordIfAboard(owner, boat)
            }
        }

        // Move on the 32-fine-unit (quarter tile) grid, colliding with land. Called even at speed 0 so a boat turned
        // into the shore is pushed back out (45 capture t8).
        val radians = entity.angle * Math.PI / 1024.0
        val dx = quarter(-sin(radians) * boat.speed)
        val dz = quarter(-cos(radians) * boat.speed)
        val moved = WorldEntityCollision.move(entity, dx, dz)
        if (!moved && (dx != 0 || dz != 0)) {
            // Pinned against land: the boat re-accelerates from a standstill (controls capture t96-t100: 64, 128, 192).
            boat.speed = 0
        }
    }

    /**
     * `sailing_boat_spawned_angle` / `_finex` / `_finez`: the boat's angle and sub-tile fine offset (0/32/64/96),
     * sent to the owner on a 5-tick cycle and on board (controls capture t32, 42, 47, 52, ...; also after
     * disembarking, t322). Only changed values go out (varbits).
     */
    @JvmStatic
    fun sendSpawnedPosition(player: Player, boat: Boat) {
        val entity = boat.entity
        val vars = player.varManager
        vars.sendBit(VARBIT_SPAWNED_ANGLE, entity.angle)
        vars.sendBit(VARBIT_SPAWNED_FINEX, entity.fineX and 127)
        vars.sendBit(VARBIT_SPAWNED_FINEZ, entity.fineZ and 127)
    }

    /**
     * Trim boost and wind gusts. Returns whether this tick's speed includes the boost.
     *
     * Boost (t291 trim): boost vfx on the trim tick and boosted ticks 1..19, trim loop anims on boosted ticks
     * 4, 8, 12, 16 (t295-t307), on boosted tick 20 (t311) "The wind dies down..." + trim end anims + sail refresh;
     * the speed is back to normal the tick after. Next gust 30 ticks later (session 1).
     *
     * Gust (controls t129-t143, t223-t237, t286; halfsails t69, t127): starts under full or half sails with a
     * helmsman; the timer pauses while the sails are down (halfsails: boost end t95 + 30 + 2 ticks down = t127).
     * Lasts 14 ticks whatever happens to the sails, then "The wind dies down..." + sail refresh. Gust start: message,
     * synth (10839 full / 10838 half), sail refresh (Trim op shown), vfx. Every gust tick: vfx by last tick's speed -
     * full above half speed, half at half speed, none when stopped (controls t137-t140, halfsails t69-t74).
     */
    private fun tickWind(boat: Boat): Boolean {
        val anims = boat.type.anims ?: return false
        val helmsman = boat.helmsman

        val boostTick = boat.boostTick
        if (boostTick >= 0) {
            val duration = boat.type.boostDuration
            if (boostTick == 0) {
                boat.boostTick = 1
                return false
            }
            if (boostTick < duration) {
                graphic(boat, boat.type.sailBLoc, anims.boostGraphic)
                if (helmsman != null && boostTick % TRIM_LOOP_INTERVAL == 0) {
                    helmsman.setAnimation(Animation(anims.playerTrimLoop))
                    locAnim(boat, boat.type.helmLoc, anims.helmLocTrimLoop)
                }
                boat.boostTick = boostTick + 1
                return true
            }
            if (helmsman != null) {
                helmsman.sendMessage("The wind dies down and your sails with it.")
                helmsman.setAnimation(Animation(anims.playerTrimEnd))
            }
            locAnim(boat, boat.type.helmLoc, anims.helmLocTrimEnd)
            boat.boostTick = -1
            boat.nextGustIn = GUST_AFTER_BOOST
            boat.helmTicks = 0
            refreshSail(boat)
            return true
        }

        if (boat.gustTicks > 0) {
            boat.gustTicks--
            if (boat.gustTicks == 0) {
                helmsman?.sendMessage("The wind dies down and your sails with it.")
                refreshSail(boat)
                boat.nextGustIn = Utils.random(GUST_MIN, GUST_MAX)
            } else {
                gustGraphic(boat, anims)
            }
            return false
        }

        // The gust timer only runs with the sails up (full or half) and someone at the helm.
        if ((boat.moveMode != MODE_SAILS && boat.moveMode != MODE_HALF) || helmsman == null) {
            return false
        }
        if (boat.nextGustIn < 0) {
            boat.nextGustIn = Utils.random(GUST_MIN, GUST_MAX)
        }
        if (--boat.nextGustIn > 0) {
            return false
        }
        boat.nextGustIn = -1
        boat.gustTicks = GUST_WINDOW
        helmsman.sendMessage("You feel a gust of wind.")
        refreshSail(boat)
        gustGraphic(boat, anims)
        synth(boat, if (boat.moveMode == MODE_HALF) SYNTH_GUST_HALF else SYNTH_GUST_FULL)
        return false
    }

    /** Wind vfx on the linen sail by last tick's speed: full above half speed, half at half speed, none when stopped. */
    private fun gustGraphic(boat: Boat, anims: SailingAnims) {
        if (boat.speed > boat.type.halfSpeed) {
            graphic(boat, boat.type.sailBLoc, anims.gustGraphic)
        } else if (boat.speed > 0) {
            graphic(boat, boat.type.sailBLoc, anims.gustGraphicHalf)
        }
    }

    private fun requireHelmsman(player: Player, boat: Boat): Boolean {
        if (boat.helmsman !== player) {
            player.sendMessage(MSG_NOT_AT_HELM)
            return false
        }
        return true
    }

    /**
     * Changes the move mode. With [visuals]: the sail loc's ops are re-sent (every change, t190), a sail state change
     * plays the transition anims + synth now and the steady anims next tick, a change within the same sail state
     * (0 <-> 3) re-sends the steady anims now and next tick (t152-153, t170-171). The helmsman gets the mode and
     * sail-button varbits. A running boost ends when the sails come down (it applies at full and half sails).
     */
    private fun setMode(boat: Boat, mode: Int, visuals: Boolean = true) {
        val oldSail = sailState(boat.moveMode)
        boat.moveMode = mode
        if (mode != MODE_SAILS && mode != MODE_HALF) {
            boat.boostTick = -1
        }
        val anims = boat.type.anims
        if (visuals && anims != null) {
            val newSail = sailState(mode)
            setSailOpFlags(boat)
            if (oldSail != newSail) {
                locAnim(boat, boat.type.sailALoc, transition(anims.sailAAnims, oldSail, newSail))
                locAnim(boat, boat.type.sailBLoc, transition(anims.sailBAnims, oldSail, newSail))
                synth(boat, sailSound(oldSail, newSail))
                scheduleSteadySail(boat, newSail)
            } else {
                sendSteadySail(boat, newSail)
                scheduleSteadySail(boat, newSail)
            }
        }
        val helmsman = boat.helmsman ?: return
        helmsman.varManager.sendBit(VARBIT_BOAT_MOVE_MODE, mode)
        helmsman.varManager.sendBit(
            VARBIT_SAIL_BUTTON_TOGGLED,
            if (mode == MODE_SAILS || mode == MODE_HALF || mode == MODE_REVERSE) 1 else 0,
        )
    }

    /** Re-sends the sail ops and steady sail anims now and next tick (helm on, gust start/end, boost end). */
    private fun refreshSail(boat: Boat) {
        val state = sailState(boat.moveMode)
        setSailOpFlags(boat)
        sendSteadySail(boat, state)
        scheduleSteadySail(boat, state)
    }

    private fun setSailOpFlags(boat: Boat) {
        boat.type.anims ?: return
        val mode = boat.moveMode
        val ops = when {
            (mode == MODE_SAILS || mode == MODE_HALF) && boat.gustTicks > 0 -> SAIL_OPS_GUST
            mode == MODE_SAILS || mode == MODE_HALF || mode == MODE_REVERSE -> SAIL_OPS_UP
            else -> SAIL_OPS_DOWN
        }
        boat.entity.setLocOpFlags(boat.type.sailBLoc, ops)
    }

    private fun sendSteadySail(boat: Boat, state: Int) {
        val anims = boat.type.anims ?: return
        locAnim(boat, boat.type.sailALoc, steady(anims.sailAAnims, state))
        locAnim(boat, boat.type.sailBLoc, steady(anims.sailBAnims, state))
    }

    private fun scheduleSteadySail(boat: Boat, state: Int) {
        WorldTasksManager.schedule(1) {
            if (boat.isLive && sailState(boat.moveMode) == state) {
                sendSteadySail(boat, state)
            }
        }
    }

    private fun sailState(mode: Int): Int = when (mode) {
        MODE_SAILS -> SAIL_FULL
        MODE_HALF -> SAIL_HALF
        else -> SAIL_DOWN
    }

    private fun steady(set: SailAnimSet, state: Int): Int = when (state) {
        SAIL_FULL -> set.full
        SAIL_HALF -> set.half
        else -> set.down
    }

    private fun transition(set: SailAnimSet, from: Int, to: Int): Int = when (from) {
        SAIL_DOWN -> if (to == SAIL_HALF) set.downToHalf else set.downToFull
        SAIL_HALF -> if (to == SAIL_DOWN) set.halfToDown else set.halfToFull
        else -> if (to == SAIL_DOWN) set.fullToDown else set.fullToHalf
    }

    private fun sailSound(from: Int, to: Int): Int = when (from) {
        SAIL_DOWN -> if (to == SAIL_HALF) SYNTH_DOWN_TO_HALF else SYNTH_DOWN_TO_FULL
        SAIL_HALF -> if (to == SAIL_DOWN) SYNTH_HALF_TO_DOWN else SYNTH_HALF_TO_FULL
        else -> if (to == SAIL_DOWN) SYNTH_FULL_TO_DOWN else SYNTH_FULL_TO_HALF
    }

    private fun isAtHelm(player: Player, boat: Boat): Boolean {
        boat.type.anims ?: return false
        val location = player.location
        val helm = boat.type.helmLoc
        val entity = boat.entity
        return !player.isFinished &&
                location.plane == helm.level &&
                location.x == (entity.instanceZoneX shl 3) + helm.dx &&
                location.y == (entity.instanceZoneZ shl 3) + helm.dz
    }

    /**
     * Faces [player] deck-south (the raft's bow; the client rotates it with the boat). Live never turns a player
     * away from south on the raft (controls capture): helm on / trim face the tile 3 south (t37, t57, t73, t291),
     * sail Set / Un-set send no facing at all (t54, t62, t69), and Stop-navigating faces the player's own tile
     * (t48, t71), which the client ignores. Our engine converts a face tile to an angle server-side and
     * `DirectionUtil.getFaceDirection(0, 0)` is atan2(-0.0, -0.0) = north, so the own-tile face is replaced by an
     * explicit south face. [Player.setFaceLocation] also clears the engine's pending faced object from the click.
     */
    @JvmStatic
    fun faceDeckSouth(player: Player) {
        val location = player.location
        player.setFaceLocation(Location(location.x, location.y - 3, location.plane))
    }

    /** Players standing on [boat]'s deck. */
    private fun passengers(boat: Boat): List<Player> {
        val entity = boat.entity
        val result = ArrayList<Player>(1)
        for (player in World.getPlayers()) {
            if (player != null && entity.containsDeckTile(player.location)) {
                result.add(player)
            }
        }
        return result
    }

    private fun synth(player: Player, id: Int) {
        player.packetDispatcher.sendSoundEffect(SoundEffect(id))
    }

    private fun synth(boat: Boat, id: Int) {
        for (player in passengers(boat)) {
            synth(player, id)
        }
    }

    private fun locAnim(boat: Boat, loc: DeckLoc, animation: Int) {
        World.sendObjectAnimation(
            loc.id,
            loc.shape,
            loc.rotation,
            boat.entity.deckTile(loc.dx, loc.dz, loc.level),
            Animation(animation),
        )
    }

    private fun graphic(boat: Boat, loc: DeckLoc, graphic: Int) {
        World.sendGraphics(Graphics(graphic), boat.entity.deckTile(loc.dx, loc.dz, loc.level))
    }

    private fun quarter(value: Double): Int = ((value / 32.0).roundToLong() * 32).toInt()
}