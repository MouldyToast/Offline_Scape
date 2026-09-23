package org.jesse.game.content.crystal

import org.jesse.game.content.skills.mining.OreDefinitions
import org.jesse.game.obj.ids.*

/**
 * Represents rocks that can be mined in Trahaearn mine.
 *
 * @author Stan van der Bend
 */
enum class TrahaearnMineRocks(
    val level: Int,
    val xp: Int,
    val amount: Int,
    val ore: OreDefinitions,
    vararg val objectIds: Int,
) {
    GOLD(40, 65, 14, OreDefinitions.GOLD, GOLD_ROCKS_36206),
    SOFT_CLAY(70, 5, 10, OreDefinitions.CLAY, SOFT_CLAY_ROCKS_36210),
    SILVER(20, 40, 8, OreDefinitions.SILVER, SILVER_ROCKS_36205),
    IRON(15, 35, 26, OreDefinitions.IRON, IRON_ROCKS_36203),
    RUNITE(85, 125, 4, OreDefinitions.RUNITE, RUNITE_ROCKS_36209),
    MITHRIL(55, 80, 7, OreDefinitions.MITHRIL, MITHRIL_ROCKS_36207),
    COAL(30, 50, 19, OreDefinitions.COAL, COAL_ROCKS_36204),
    ADAMANTITE(70, 95, 7, OreDefinitions.ADAMANTITE, ADAMANTITE_ROCKS_36208);

    companion object {
        fun getAllRockObjectIds() = values().flatMap { it.objectIds.toList() }.toIntArray()
    }
}
