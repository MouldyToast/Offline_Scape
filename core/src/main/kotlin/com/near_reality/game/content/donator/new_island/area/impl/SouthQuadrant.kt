package com.near_reality.game.content.donator.new_island.area.impl

import com.near_reality.game.content.donator.new_island.area.DonatorIslandQuadrant
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
class SouthQuadrant: DonatorIslandQuadrant() {
    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(
                arrayOf(
                    intArrayOf(1669, 2615),
                    intArrayOf(1654, 2614),
                    intArrayOf(1625, 2583),
                    intArrayOf(1625, 2560),
                    intArrayOf(1711, 2552),
                    intArrayOf(1715, 2577),
                    intArrayOf(1670, 2615)
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
        player.isInSouthernQuadrant = true
    }

    override fun leave(player: Player?, logout: Boolean) {
        player ?: return
        player.sendDeveloperMessage("You left ${name()}")
        player.isInSouthernQuadrant = false
    }

    override fun name(): String = "South Quadrant"

}