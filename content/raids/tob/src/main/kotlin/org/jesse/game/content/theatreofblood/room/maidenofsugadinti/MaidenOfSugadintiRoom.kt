package org.jesse.game.content.theatreofblood.room.maidenofsugadinti

import org.jesse.game.content.theatreofblood.TheatreOfBloodRaid
import org.jesse.game.content.theatreofblood.room.*
import org.jesse.game.content.theatreofblood.room.maidenofsugadinti.npc.MaidenOfSugadinti
import org.jesse.game.util.Direction
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.area.plugins.DeathPlugin
import org.jesse.game.world.region.dynamicregion.AllocatedArea

/**
 * @author Tommeh
 * @author Jire
 */
internal class MaidenOfSugadintiRoom(
    raid: TheatreOfBloodRaid,
    area: AllocatedArea,
    room: TheatreRoomType
) : TheatreRoom(raid, area, room), DeathPlugin {

    private val maiden = MaidenOfSugadinti(this)
    private val maidenObjectPlaceholder =
        WorldObject(THE_MAIDEN_OF_SUGADINTI, 10, 3, getLocation(MaidenOfSugadinti.spawnLocation))
    private val poolOfBlood = WorldObject(POOL_OF_BLOOD, 10, 0, getLocation(MaidenOfSugadinti.spawnLocation))

    override fun onLoad() {
        if (!completed) World.spawnObject(maidenObjectPlaceholder)
        else World.spawnObject(poolOfBlood)
    }

    override fun isEnteringBossRoom(barrier: WorldObject, player: Player) = player.x > barrier.x

    override fun enterBossRoom(barrier: WorldObject, player: Player) {
        if (!started) {
            World.removeObject(maidenObjectPlaceholder)
            maiden.spawn()
        }

        super.enterBossRoom(barrier, player)
    }

    override var nextRoomType : TheatreRoomType? = TheatreRoomType.THE_PESTILENT_BLOAT

    override val entranceLocation: Location = getLocation(3219, 4459, 0)
    override val vyreOrator = WorldObject(VYRE_ORATOR, 11, 0, getLocation(3192, 4447, 0))
    override val spectatingLocation: Location = getLocation(3190, 4453, 0)
    override var boss: TheatreBossNPC<out TheatreRoom>? = maiden
    override val healthBarType = HealthBarType.REGULAR

    override fun onComplete() {
        super.onComplete()

        World.spawnObject(poolOfBlood)
    }

    override val jailLocations = Companion.jailLocations

    private companion object {
        val jailLocations = arrayOf(
            JailLocation(30, 44),
            JailLocation(31, 44),
            JailLocation(30, 17, Direction.NORTH),
            JailLocation(31, 17, Direction.NORTH)
        )
    }

}