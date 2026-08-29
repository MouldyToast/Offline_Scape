package com.near_reality.game.content.donator.new_island.area.impl

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.`var`.VarCollection
import com.zenyte.game.world.region.RSPolygon

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-23
 */
class EastQuadrantFountainOfRune: EastQuadrant() {
    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(
                arrayOf(
                    intArrayOf(1698, 2611),
                    intArrayOf(1712, 2611),
                    intArrayOf(1712, 2620),
                    intArrayOf(1698, 2620),
                    intArrayOf(1698, 2610)
                )
            )
        )
    }

    override fun enter(player: Player?) {
        player ?: return
        VarCollection.FOUNTAIN_OF_RUNE.send(player, 1)
    }

    override fun leave(player: Player?, logout: Boolean) {
        player ?: return
        VarCollection.FOUNTAIN_OF_RUNE.send(player, 0)
    }

    override fun name(): String = "East Quadrant FoR"

}