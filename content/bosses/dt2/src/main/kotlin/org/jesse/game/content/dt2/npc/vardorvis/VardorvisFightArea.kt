package org.jesse.game.content.dt2.npc.vardorvis

import org.jesse.game.content.dt2.npc.coord
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.PolygonRegionArea
import org.jesse.game.world.region.RSPolygon

/**
 * @author Khaled Abdeljaber
 */
class VardorvisFightArea : PolygonRegionArea() {
    override fun enter(player: Player?) {
    }

    override fun leave(player: Player?, logout: Boolean) {
    }

    override fun name(): String {
        return "Vardorvis Fight"
    }

    override fun polygons(): Array<RSPolygon> = arrayOf(
        RSPolygon(
            coord(1124, 3413),
            coord(1134, 3423),
        )
    )
}