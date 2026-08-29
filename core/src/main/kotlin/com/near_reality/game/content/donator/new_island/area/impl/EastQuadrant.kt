package com.near_reality.game.content.donator.new_island.area.impl

import com.near_reality.game.content.donator.new_island.area.DonatorIslandQuadrant
import com.near_reality.game.content.donator.new_island.isInEasternQuadrant
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.`var`.VarCollection
import com.zenyte.game.world.region.RSPolygon

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-23
 */
open class EastQuadrant: DonatorIslandQuadrant() {
    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(
                arrayOf(
                    intArrayOf(1672, 2630),
                    intArrayOf(1672, 2617),
                    intArrayOf(1699, 2591),
                    intArrayOf(1729, 2591),
                    intArrayOf(1734, 2663),
                    intArrayOf(1710, 2669),
                    intArrayOf(1672, 2631)
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
        player.isInEasternQuadrant = true
    }

    override fun leave(player: Player?, logout: Boolean) {
        player ?: return
        player.sendDeveloperMessage("You left ${name()}")
        player.isInEasternQuadrant = false
    }

    override fun name(): String = "East Quadrant"

}