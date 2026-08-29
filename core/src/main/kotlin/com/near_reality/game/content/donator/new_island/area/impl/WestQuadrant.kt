package com.near_reality.game.content.donator.new_island.area.impl

import com.near_reality.game.content.donator.new_island.area.DonatorIslandQuadrant
import com.near_reality.game.content.donator.new_island.isInWesternQuadrant
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.RSPolygon

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-23
 */
class WestQuadrant: DonatorIslandQuadrant() {
    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(
                arrayOf(
                    intArrayOf(1600, 2591),
                    intArrayOf(1629, 2591),
                    intArrayOf(1655, 2619),
                    intArrayOf(1655, 2630),
                    intArrayOf(1640, 2638),
                    intArrayOf(1633, 2652),
                    intArrayOf(1600, 2652),
                    intArrayOf(1600, 2592)
                )
            )
        )
    }

    override fun getHiddenSkillLevelBoost(): Int = 7
    override fun getOreDepletionChance(): Int = 0
    override fun getTreeDepletionChance(): Int = 0
    override fun getDungeonDropRateBoost(): Int = 5

    override fun enter(player: Player?) {
        player ?: return
        player.sendDeveloperMessage("You entered ${name()}")
        player.isInWesternQuadrant = true
    }

    override fun leave(player: Player?, logout: Boolean) {
        player ?: return
        player.sendDeveloperMessage("You left ${name()}")
        player.isInWesternQuadrant = false
    }

    override fun name(): String = "West Quadrant"

}