package com.near_reality.game.content.dt2.npc.vardorvis

import com.near_reality.game.content.dt2.npc.coord
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.PolygonRegionArea
import com.zenyte.game.world.region.RSPolygon

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