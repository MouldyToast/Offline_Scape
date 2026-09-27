package org.jesse.game.content.skills.sailing

import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.player.Player
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToLong
import kotlin.math.sin

/**
 * Helm, sails, move modes, wind gusts + trim, steering and per-tick boat movement.
 *
 * Every rule here is taken from the live Pandemonium capture (investigation_sailing_world_entities_v2.md §4-§5):
 * - taking the helm: `sailing_boat_facility_lockedin`=3, SET_INTERACTION_MODE(default: heading/all, boat: walk/all),
 *   player faces the deck tile 3 south of the helm, helm anims, move mode 4 when idle (t443, t601-602);
 * - leaving the helm: RESET_INTERACTION_MODE(boat), SET_INTERACTION_MODE(default: walk/all), lockedin=0 (t599);
 * - steering: SET_HEADING 0..15; the boat turns 128 angle units per tick towards heading*128,
 *   180-degree ties through increasing angles (t672-680);
 * - move modes (sidepanel sail buttons, t446-466): 2 sails set -> +acceleration up to base speed (+ boost);
 *   1 slowing and 0 idle -> -acceleration to 0; 3 reverse -> constant 0.5 tiles/tick backwards, and leaving
 *   reverse stops dead (t461-464);
 * - gusts + trim (t510-533, t563-586, t734-756, t786-808): "You feel a gust of wind." -> Trim within the gust ->
 *   "You trim the sails..." + 20-tick +0.5 boost -> "The wind dies down and your sails with it." when the boost ends;
 *   the next gust came 30 ticks after each boost ended;
 * - movement: fine coords += round_to_32(speed * (-sin, -cos)(angle)) - fits 398/400 live updates.
 *
 * Unverified (not in the capture): the first gust delay after setting sails (live: 44 and 62 ticks - randomised
 * 40..60 here), how long an untrimmed gust lasts (20 here), and land collision (none yet).
 */
object Sailing {
    const val MODE_IDLE = 0
    const val MODE_SLOWING = 1
    const val MODE_SAILS = 2
    const val MODE_REVERSE = 3
    const val MODE_HELM_IDLE = 4

    private const val VARBIT_FACILITY_LOCKEDIN = 19105
    private const val VARBIT_SAIL_BUTTON_TOGGLED = 19174
    private const val VARBIT_BOAT_MOVE_MODE = 19175

    private const val WORLD_DEFAULT = -2
    private const val TILE_MODE_WALK = 1
    private const val TILE_MODE_HEADING = 2
    private const val ENTITY_MODE_ALL = 1

    private const val HELM_LOOP_INTERVAL = 10
    private const val TRIM_LOOP_INTERVAL = 4
    private const val TRIM_LOOP_FIRST = 8
    private const val REVERSE_SPEED = 64
    private const val GUST_WINDOW = 20
    private const val GUST_AFTER_BOOST = 30
    private const val FIRST_GUST_MIN = 40
    private const val FIRST_GUST_MAX = 60

    /** Helm "Navigate" / "Stop-navigating" (the multiloc flips its op1 label on lockedin). */
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
        val helm = anims.helm
        player.setFaceLocation(entity.deckTile(helm.dx, helm.dz - 3, helm.level))
        player.setAnimation(Animation(anims.playerHelmStart))
        locAnim(boat, helm, anims.helmLocStart)
        SailingSidepanel.setAtHelm(player, true)
        if (boat.moveMode == MODE_IDLE && boat.speed == 0) {
            setMode(boat, MODE_HELM_IDLE)
        } else {
            player.varManager.sendBit(VARBIT_BOAT_MOVE_MODE, boat.moveMode)
        }
    }

    /** Releases the helm (player clicked Stop-navigating, walked off, disembarked, logged out, or the boat despawned). */
    @JvmStatic
    fun leaveHelm(boat: Boat) {
        val player = boat.helmsman ?: return
        if (boat.moveMode != MODE_IDLE) {
            setMode(boat, MODE_IDLE)
        }
        boat.helmsman = null
        boat.helmTicks = 0
        endGust(boat)
        player.varManager.sendBit(VARBIT_FACILITY_LOCKEDIN, 0)
        player.varManager.sendBit(VARBIT_BOAT_MOVE_MODE, 0)
        player.varManager.sendBit(VARBIT_SAIL_BUTTON_TOGGLED, 0)
        val sender = player.packetDispatcher.sender
        sender.resetInteractionMode(boat.entity.index)
        sender.setInteractionMode(WORLD_DEFAULT, TILE_MODE_WALK, ENTITY_MODE_ALL)
        player.setAnimation(Animation.STOP)
        SailingSidepanel.setAtHelm(player, false)
        val anims = boat.type.anims ?: return
        locAnim(boat, anims.helm, anims.helmLocInactive)
    }

    /** Sails loc "Set" / "Un-set". Only the player at the helm may adjust the sails (live message). */
    @JvmStatic
    fun setSails(player: Player, boat: Boat, set: Boolean) {
        if (!requireHelmsman(player, boat)) {
            return
        }
        if (boat.sailsSet == set) {
            return
        }
        setMode(boat, if (set) MODE_SAILS else MODE_IDLE)
    }

    /**
     * Sidepanel facility buttons (`sailing_sidepanel:facilities_content_clicklayer` 937:25), live t453-466:
     * sub 0 toggles the sails (2 <-> 0); sub 1 steps 2 -> 1 (slow), 1 -> 0, 0 -> 3 (reverse), 3 -> 0; sub 2 stops (-> 0).
     */
    @JvmStatic
    fun sidepanelButton(player: Player, sub: Int) {
        val boat = Boats.at(player.location) ?: return
        if (!requireHelmsman(player, boat)) {
            return
        }
        val mode = boat.moveMode
        val next = when (sub) {
            0 -> if (mode == MODE_SAILS) MODE_IDLE else MODE_SAILS
            1 -> when (mode) {
                MODE_SAILS -> MODE_SLOWING
                MODE_REVERSE, MODE_SLOWING -> MODE_IDLE
                else -> if (boat.speed == 0) MODE_REVERSE else MODE_SLOWING
            }
            2 -> MODE_IDLE
            else -> return
        }
        if (next != mode) {
            setMode(boat, next)
        }
    }

    /** Sails loc "Trim": only during a gust, with the sails set, from the helm. */
    @JvmStatic
    fun trim(player: Player, boat: Boat) {
        if (!requireHelmsman(player, boat)) {
            return
        }
        if (!boat.sailsSet || boat.gustTicks <= 0) {
            player.sendMessage("There's no wind to catch right now.")
            return
        }
        val anims = boat.type.anims ?: return
        boat.gustTicks = 0
        boat.nextGustIn = -1
        boat.boostTicks = boat.type.boostDuration
        boat.boostElapsed = 0
        player.sendMessage("You trim the sails, catching the wind for a burst of speed!")
        player.setAnimation(Animation(anims.playerTrimStart))
        locAnim(boat, anims.helm, anims.helmLocTrimStart)
        graphic(boat, anims.sailB, anims.boostGraphic)
    }

    /** SET_HEADING from the client (heading mode is only enabled for the helmsman). */
    @JvmStatic
    fun onSetHeading(player: Player, boat: Boat, heading: Int) {
        if (boat.helmsman !== player) {
            return
        }
        boat.targetHeading = heading and 15
    }

    /**
     * One game tick for [boat]: helm upkeep, wind, turning, speed, movement.
     * Called from [SailingWorldEntityListener.onTick]; the engine reloads passengers' scenes when the boat moves.
     */
    internal fun tick(boat: Boat) {
        val entity = boat.entity
        val anims = boat.type.anims
        val helmsman = boat.helmsman
        if (helmsman != null) {
            if (!isAtHelm(helmsman, boat)) {
                leaveHelm(boat)
            } else {
                boat.helmTicks++
                if (anims != null && boat.boostTicks <= 0 && boat.helmTicks % HELM_LOOP_INTERVAL == 0) {
                    helmsman.setAnimation(Animation(anims.playerHelmLoop))
                    locAnim(boat, anims.helm, anims.helmLocLoop)
                }
            }
        }

        tickWind(boat)

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

        // Speed by move mode.
        val type = boat.type
        val cruise = min(type.baseSpeed + (if (boat.boostTicks > 0) type.boostAmount else 0), type.speedCap)
        boat.speed = when (boat.moveMode) {
            MODE_SAILS -> min(boat.speed + type.acceleration, cruise)
            MODE_REVERSE -> -REVERSE_SPEED
            else -> if (boat.speed < 0) 0 else max(boat.speed - type.acceleration, 0)
        }
        if (boat.moveMode == MODE_SLOWING && boat.speed == 0) {
            setMode(boat, MODE_IDLE)
        }
        if (boat.speed == 0) {
            return
        }

        // Move on the 32-fine-unit (quarter tile) grid.
        val radians = entity.angle * Math.PI / 1024.0
        val dx = quarter(-sin(radians) * boat.speed)
        val dz = quarter(-cos(radians) * boat.speed)
        if (dx == 0 && dz == 0) {
            return
        }
        entity.moveTo(entity.level, entity.fineX + dx, entity.fineZ + dz, false)
    }

    /** Gust scheduling, gust window, trim boost countdown and their effects. */
    private fun tickWind(boat: Boat) {
        val anims = boat.type.anims ?: return
        val helmsman = boat.helmsman

        if (boat.boostTicks > 0) {
            boat.boostTicks--
            boat.boostElapsed++
            graphic(boat, anims.sailB, anims.boostGraphic)
            if (helmsman != null && boat.boostElapsed >= TRIM_LOOP_FIRST &&
                (boat.boostElapsed - TRIM_LOOP_FIRST) % TRIM_LOOP_INTERVAL == 0
            ) {
                helmsman.setAnimation(Animation(anims.playerTrimLoop))
                locAnim(boat, anims.helm, anims.helmLocTrimLoop)
            }
            if (boat.boostTicks == 0) {
                if (helmsman != null) {
                    helmsman.sendMessage("The wind dies down and your sails with it.")
                    helmsman.setAnimation(Animation(anims.playerTrimEnd))
                }
                locAnim(boat, anims.helm, anims.helmLocTrimEnd)
                boat.nextGustIn = GUST_AFTER_BOOST
            }
            return
        }

        if (!boat.sailsSet || helmsman == null) {
            endGust(boat)
            return
        }

        if (boat.gustTicks > 0) {
            boat.gustTicks--
            graphic(boat, anims.sailB, anims.gustGraphic)
            if (boat.gustTicks == 0) {
                boat.nextGustIn = GUST_AFTER_BOOST
            }
            return
        }

        if (boat.nextGustIn < 0) {
            boat.nextGustIn = Utils.random(FIRST_GUST_MIN, FIRST_GUST_MAX)
        }
        if (--boat.nextGustIn <= 0) {
            boat.nextGustIn = -1
            boat.gustTicks = GUST_WINDOW
            helmsman.sendMessage("You feel a gust of wind.")
            locAnim(boat, anims.sailA, anims.sailFull)
            locAnim(boat, anims.sailB, anims.sailFullOffset)
            graphic(boat, anims.sailB, anims.gustGraphic)
        }
    }

    private fun endGust(boat: Boat) {
        boat.gustTicks = 0
        boat.nextGustIn = -1
    }

    private fun requireHelmsman(player: Player, boat: Boat): Boolean {
        if (boat.helmsman !== player) {
            player.sendMessage("You must be navigating at the helm to adjust the sails.")
            return false
        }
        return true
    }

    /** Changes the move mode: sail anims on entering / leaving mode 2, sidepanel varbits for the helmsman. */
    private fun setMode(boat: Boat, mode: Int) {
        val wasSails = boat.sailsSet
        boat.moveMode = mode
        if (mode != MODE_SAILS) {
            boat.boostTicks = 0
        }
        val anims = boat.type.anims
        if (anims != null && wasSails != boat.sailsSet) {
            val set = boat.sailsSet
            if (set) {
                locAnim(boat, anims.sailA, anims.sailDownToFull)
                locAnim(boat, anims.sailB, anims.sailDownToFullOffset)
            } else {
                locAnim(boat, anims.sailA, anims.sailFullToDown)
                locAnim(boat, anims.sailB, anims.sailFullToDownOffset)
            }
            WorldTasksManager.schedule(1) {
                if (boat.sailsSet != set || !boat.isLive) {
                    return@schedule
                }
                if (set) {
                    locAnim(boat, anims.sailA, anims.sailFull)
                    locAnim(boat, anims.sailB, anims.sailFullOffset)
                } else {
                    locAnim(boat, anims.sailA, anims.sailDown)
                    locAnim(boat, anims.sailB, anims.sailDownOffset)
                }
            }
        }
        val helmsman = boat.helmsman ?: return
        helmsman.varManager.sendBit(VARBIT_BOAT_MOVE_MODE, mode)
        helmsman.varManager.sendBit(
            VARBIT_SAIL_BUTTON_TOGGLED,
            if (mode == MODE_SAILS || mode == MODE_SLOWING || mode == MODE_REVERSE) 1 else 0,
        )
    }

    /** Stops the boat dead (docking). */
    @JvmStatic
    fun stop(boat: Boat) {
        if (boat.moveMode != MODE_IDLE) {
            setMode(boat, MODE_IDLE)
        }
        boat.speed = 0
        endGust(boat)
    }

    private fun isAtHelm(player: Player, boat: Boat): Boolean {
        val anims = boat.type.anims ?: return false
        val location = player.location
        val helm = anims.helm
        val entity = boat.entity
        return !player.isFinished &&
                location.plane == helm.level &&
                location.x == (entity.instanceZoneX shl 3) + helm.dx &&
                location.y == (entity.instanceZoneZ shl 3) + helm.dz
    }

    private fun locAnim(boat: Boat, loc: DeckRef, animation: Int) {
        World.sendObjectAnimation(
            loc.id,
            loc.shape,
            loc.rotation,
            boat.entity.deckTile(loc.dx, loc.dz, loc.level),
            Animation(animation),
        )
    }

    private fun graphic(boat: Boat, loc: DeckRef, graphic: Int) {
        World.sendGraphics(Graphics(graphic), boat.entity.deckTile(loc.dx, loc.dz, loc.level))
    }

    private fun quarter(value: Double): Int = ((value / 32.0).roundToLong() * 32).toInt()
}
