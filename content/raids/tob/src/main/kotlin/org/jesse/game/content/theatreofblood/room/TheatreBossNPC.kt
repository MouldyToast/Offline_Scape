package org.jesse.game.content.theatreofblood.room

import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Hit

/**
 * @author Jire
 */
internal abstract class TheatreBossNPC<T : TheatreRoom>(
    room: T,
    id: Int,
    tile: Location?,
    facing: Direction = Direction.DEFAULT
) : TheatreNPC<T>(room, id, tile, facing) {

    override fun heal(amount: Int) {
        super.heal(amount)

        room.refreshHealthBar()
    }

    override fun removeHitpoints(hit: Hit) {
        super.removeHitpoints(hit)

        room.refreshHealthBar()
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        room.complete()
    }

}