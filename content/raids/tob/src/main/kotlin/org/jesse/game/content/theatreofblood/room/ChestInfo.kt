package org.jesse.game.content.theatreofblood.room

import org.jesse.game.content.theatreofblood.interfaces.TheatreOfBloodSuppliesInterface.Companion.tobPoints
import org.jesse.game.content.theatreofblood.party.RaidingParty
import org.jesse.game.model.HintArrow
import org.jesse.game.util.Colour
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.utils.TextUtils

/**
 * @author Jire
 */
internal data class ChestInfo(
    val localX: Int,
    val localY: Int,
    val rotation: Int
) {

    fun spawnInArea(theatreRoom: TheatreRoom) {
        val worldObject = WorldObject(
            CLOSED_CHEST_OBJECT_ID,
            WorldObject.DEFAULT_TYPE,
            rotation,
            theatreRoom.getBaseLocation(localX, localY)
        )

        World.spawnObject(worldObject)

        val hintArrow = HintArrow(worldObject.x, worldObject.y, worldObject.plane.toByte())
        theatreRoom.raid.party.players.forEach { player ->
            player.sendMessage("The Vampyres are impressed by your prowess. " + "${Colour.RED.wrap("Check the chest")} to see how many points they've granted you.")
            player.packetDispatcher.sendHintArrow(hintArrow)
        }
    }

    companion object {

        internal const val CLOSED_CHEST_OBJECT_ID = CHEST_32758
        internal const val OPEN_CHEST_OBJECT_ID = CHEST_32759

    }

}