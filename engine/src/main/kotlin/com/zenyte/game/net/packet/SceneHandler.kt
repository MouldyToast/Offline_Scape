package com.zenyte.game.net.packet

import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.WalkStep
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.impl.slayer.superior.impl.BasiliskSentinel
import com.zenyte.game.world.entity.pathfinding.RouteFinder
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy
import com.zenyte.game.world.entity.player.Player
import net.rsprot.protocol.game.incoming.misc.client.MapBuildComplete
import net.rsprot.protocol.game.incoming.misc.client.SoundJingleEnd
import net.rsprot.protocol.game.incoming.misc.client.WindowStatus
import net.rsprot.protocol.game.incoming.misc.user.MoveGameClick
import net.rsprot.protocol.game.incoming.misc.user.MoveMinimapClick
import java.util.*

internal fun PacketConsumer.soundJingleEnd() {
    addListener<SoundJingleEnd> {
//        player.playJingle(null)
    }
}

internal fun PacketConsumer.moveGameClick() {
    fun Player.handle(x: Int, y: Int, type: Int) {
        val player = this
        val offsetX = x
        val offsetY = y
        if (player.isLocked || player.isFullMovementLocked()) {
            player.packetDispatcher.resetMapFlag()
            return
        }
        val route = RouteFinder.findRoute(
            player.getX(), player.getY(), player.getPlane(), player.getSize(), TileStrategy(offsetX, offsetY), true
        )
        val steps = route.steps
        val bufferX = route.xBuffer
        val bufferY = route.yBuffer
        player.stop(
            Player.StopType.INTERFACES, Player.StopType.WALK, Player.StopType.ACTIONS, Player.StopType.ROUTE_EVENT
        )
        if (type == 2) {
            if (player.eligibleForShiftTeleportation()) {
                player.setLocation(Location(offsetX, offsetY, player.getPlane()))
                return
            }
            player.setRun(true)
        }
        if (player.isFrozen()) {
            if (player.getNumericTemporaryAttributeOrDefault(BasiliskSentinel.FROZEN_ATTR, -1).toInt() != -1) {
                player.incrementNumericTemporaryAttribute(BasiliskSentinel.FROZEN_ATTR, 1)
                player.sendMessage("You feel the stone break slightly as you try to move.")
                return
            }
            player.sendMessage("A magical force stops you from moving.")
            return
        }
        if (player.isStunned) {
            player.sendMessage("You're stunned.")
            return
        }
        if (player.isMovementLocked(true) || player.isLocked) {
            return
        }
        for (i in steps - 1 downTo 0) {
            if (!player.addWalkSteps(bufferX[i], bufferY[i], 60, true)) {
                break
            }
        }
        val walksteps = player.walkSteps
        if (walksteps.isEmpty()) {
            return
        }
        val hash = walksteps.last
        val tile = Location(WalkStep.getNextX(hash), WalkStep.getNextY(hash), player.getPlane())
        player.packetDispatcher.sendMapFlag(player.getXInScene(tile), player.getYInScene(tile))
    }

    addListener<MoveGameClick> {
        player.handle(it.x, it.z, it.keyCombination)
    }

    addListener<MoveMinimapClick> {
        player.handle(it.x, it.z, it.keyCombination)
    }
}

internal fun PacketConsumer.windowStatus() {
    addListener<WindowStatus> {
        val width = it.frameWidth
        val height = it.frameHeight
        val mode = it.windowMode

        val player = player

        if (width > 781 && height > 541 && player.temporaryAttributes["welcomeScreen"] != null || player.playerInformation.mode == mode) {
            return@addListener
        }
        player.interfaceHandler.isResizable = mode == 2
        player.playerInformation.mode = mode
        //player.getInterfaceHandler().sendWelcomeScreen();
        //player.getInterfaceHandler().sendWelcomeScreen();
        player.interfaceHandler.sendGameFrame()
    }
}

internal fun PacketConsumer.mapBuildComplete() {
    addListener<MapBuildComplete> {
        player.isLoadingRegion = false
        World.getNPCs().stream()
            .filter { obj: NPC? ->
                Objects.nonNull(
                    obj
                )
            }
            .filter { obj: NPC -> obj.hasOverheadActive() }
            .forEach { demon: NPC ->
                demon.updateFlags[UpdateFlag.NPC_PRAYER_OVERHEAD] = true
            }
    }
}
