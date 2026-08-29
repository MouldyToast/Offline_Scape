package com.near_reality.game.content.donator.new_island.area.impl

import com.near_reality.game.content.donator.new_island.area.DonatorIslandQuadrant
import com.near_reality.game.content.donator.new_island.isInNorthernQuadrant
import com.near_reality.game.content.donator.new_island.isInSouthernQuadrant
import com.near_reality.game.content.donator.new_island.isInWesternQuadrant
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.RSPolygon

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-23
 */
class NorthQuadrant: DonatorIslandQuadrant() {
    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(
                arrayOf(
                    intArrayOf(1658, 2633),
                    intArrayOf(1671, 2633),
                    intArrayOf(1701, 2663),
                    intArrayOf(1683, 2688),
                    intArrayOf(1621, 2690),
                    intArrayOf(1621, 2660),
                    intArrayOf(1634, 2652),
                    intArrayOf(1654, 2633)
                )
            )
        )
    }

    override fun getHiddenSkillLevelBoost(): Int = 11
    override fun getOreDepletionChance(): Int = 30
    override fun getTreeDepletionChance(): Int = 0
    override fun getDungeonDropRateBoost(): Int = 15

    override fun enter(player: Player?) {
        player ?: return
        player.sendDeveloperMessage("You entered ${name()}")
        player.isInNorthernQuadrant = true
    }

    override fun leave(player: Player?, logout: Boolean) {
        player ?: return
        player.sendDeveloperMessage("You left ${name()}")
        player.isInNorthernQuadrant = false
    }

    override fun name(): String = "North Quadrant"

}