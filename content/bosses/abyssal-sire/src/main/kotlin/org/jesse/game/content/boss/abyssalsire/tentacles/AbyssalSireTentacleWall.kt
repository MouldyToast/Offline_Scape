package org.jesse.game.content.boss.abyssalsire.tentacles

import org.jesse.game.util.Direction
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.`object`.WorldObject

/**
 * The invisible wall object constants, used to block access to the respiratory systems of the sire.
 * These walls are impenetrable, and will temporarily be removed once the sire has been hit.
 * They return upon death, or if sire resets.
 *
 * @author Jire
 * @author Kris
 */
internal class AbyssalSireTentacleWall(
    location: Location,
    blockingDirection: Direction,

    private val worldObject: WorldObject = WorldObject(
        WALL_OBJECT_ID,
        WALL_OBJECT_TYPE,
        blockingDirection.offsetX + 1,
        location
    )
) {

    init {
        check(blockingDirection.direction and 0x600.inv() == 0) {
            "Invalid direction constant: $blockingDirection"
        }
    }

    fun build() {
        if (!World.containsSpawnedObject(worldObject))
            World.spawnObject(worldObject)
    }

    fun remove() {
        if (World.containsSpawnedObject(worldObject))
            World.removeObject(worldObject)
    }

    private companion object {
        const val WALL_OBJECT_ID = 4451
        const val WALL_OBJECT_TYPE = 0
    }

}