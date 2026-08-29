package com.near_reality.game.content.donator.new_island.area

import com.near_reality.game.content.donator.new_island.area.impl.EastQuadrant
import com.near_reality.game.content.donator.new_island.area.impl.NorthQuadrant
import com.near_reality.game.content.donator.new_island.area.impl.SouthQuadrant
import com.near_reality.game.content.donator.new_island.area.impl.WestQuadrant
import com.near_reality.game.content.donator.new_island.isInEasternQuadrant
import com.near_reality.game.content.donator.new_island.isInNorthernQuadrant
import com.near_reality.game.content.donator.new_island.isInSouthernQuadrant
import com.near_reality.game.content.donator.new_island.isInWesternQuadrant
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.PolygonRegionArea

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-23
 */
abstract class DonatorIslandQuadrant: PolygonRegionArea() {

    private val playersInQuadrant = mutableSetOf<Player>()

    /**
     * Retrieves the hidden boost applied to skill levels in the context of a specific implementation.
     *
     * @return An integer value representing the hidden skill level boost.
     */
    abstract fun getHiddenSkillLevelBoost(): Int

    /**
     * Determines the chance of ore depletion within the current context or implementation.
     *
     * @return An integer representing the likelihood of ore depletion, where higher values
     *         indicate greater probability of not depleting the ore.
     */
    abstract fun getOreDepletionChance(): Int

    /**
     * Determines the chance of a tree being depleted after it is interacted with, specific to the
     * current implementation.
     *
     * @return An integer value representing the likelihood of tree depletion, where higher values
     *         indicate a lower probability of not depletion the tree.
     */
    abstract fun getTreeDepletionChance(): Int

    /**
     * Retrieves the boost percentage applied to the drop rates within dungeons, specific to the implementation.
     *
     * @return An integer value representing the percentage increase in dungeon drop rates.
     */
    abstract fun getDungeonDropRateBoost(): Int


    companion object {

        fun Player.getQuadrantHiddenSkillBoost(): Int =
            getQuadrant()?.getHiddenSkillLevelBoost() ?: 0

        fun Player.getQuadrantDungeonDropBoost(): Int =
            getQuadrant()?.getDungeonDropRateBoost() ?: 0

        fun Player.getQuadrantTreeDepletion(): Double =
            getQuadrant()?.getTreeDepletionChance()?.div(100.0) ?: 0.0

        fun Player.getQuadrantOreDepletion(): Double =
            getQuadrant()?.getOreDepletionChance()?.div(100.0) ?: 0.0

        private fun Player.getQuadrant(): DonatorIslandQuadrant? =
            if (isInWesternQuadrant) WestQuadrant()
            else if (isInNorthernQuadrant) NorthQuadrant()
            else if (isInEasternQuadrant) EastQuadrant()
            else if (isInSouthernQuadrant) SouthQuadrant()
            else null

    }

}