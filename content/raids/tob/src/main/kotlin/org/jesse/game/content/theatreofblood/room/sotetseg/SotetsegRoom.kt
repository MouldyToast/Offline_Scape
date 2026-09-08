package org.jesse.game.content.theatreofblood.room.sotetseg

import org.jesse.game.content.theatreofblood.TheatreOfBloodRaid
import org.jesse.game.content.theatreofblood.room.*
import org.jesse.game.content.theatreofblood.room.sotetseg.npc.Sotetseg
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.area.plugins.FullMovementPlugin
import org.jesse.game.world.region.dynamicregion.AllocatedArea

/**
 * @author Tommeh
 * @author Jire
 */
internal class SotetsegRoom(
    raid: TheatreOfBloodRaid,
    area: AllocatedArea,
    room: TheatreRoomType
) : TheatreRoom(raid, area, room),
    FullMovementPlugin {

    private val sotetseg = Sotetseg(this)

    fun setMazeTileIds(tile: SotetsegRoomTile) {
        val topLeft = mazeTopLeft
        val bottomRight = mazeBottomRight
        for (x in topLeft.x..bottomRight.x) {
            for (y in bottomRight.y..topLeft.y) {
                World.spawnObject(WorldObject(tile.id, 22, 0, x, y, topLeft.plane))
            }
        }
    }

    fun isInMaze(player: Player) = isInMaze(player.location)

    fun isInMaze(location: Location) = if (location.x < mazeTopLeft.x || location.x > mazeBottomRight.x) false
    else location.y >= mazeBottomRight.y && location.y <= mazeTopLeft.y

    val mazeTopLeft: Location = getLocation(3273, 4324)
    val mazeBottomRight: Location = getLocation(3286, 4310)

    override fun onLoad() = setMazeTileIds(SotetsegRoomTile.LIGHT_GREY)

    override val entranceLocation: Location = getLocation(3279, 4293, 0)

    override fun enterBossRoom(barrier: WorldObject, player: Player) {
        if (!started) {
            sotetseg.spawn()
            sotetseg.started = true
        }

        super.enterBossRoom(barrier, player)
    }

    override val vyreOrator = WorldObject(VYRE_ORATOR, 11, 1, getLocation(3281, 4301, 0))
    override val spectatingLocation: Location = getLocation(3272, 4301, 0)

    override var boss: TheatreBossNPC<out TheatreRoom>? = sotetseg

    override var chestInfo: ChestInfo? = SotetsegRoom.chestInfo

    override fun isEnteringBossRoom(barrier: WorldObject, player: Player) = player.y < barrier.y

    override val healthBarType: HealthBarType
        get() = if (sotetseg.isMazePhase) HealthBarType.DISABLED else HealthBarType.REGULAR

    override var nextRoomType : TheatreRoomType? = TheatreRoomType.XARPUS

    override val jailLocations = Companion.jailLocations

    private companion object {

        val chestInfo = ChestInfo(17, 5, 2)

        val jailLocations = arrayOf(
            JailLocation(6, 25),
            JailLocation(6, 26),
            JailLocation(25, 25),
            JailLocation(25, 26)
        )

    }

    override fun processMovement(player: Player?, x: Int, y: Int): Boolean {
        player ?: return false
        val damage: Int = ((player.hitpoints * 0.0667) + 15).toInt()
        val hit = Hit(damage, HitType.DEFAULT)
        if (World.getObjectWithId(player.location, 33033) != null && sotetseg.shadowRealm != null) {
            if (!(sotetseg.shadowRealm!!.mazePath?.contains(player.location)!!))
                player.applyHit(hit)
        }
        return true
    }

}