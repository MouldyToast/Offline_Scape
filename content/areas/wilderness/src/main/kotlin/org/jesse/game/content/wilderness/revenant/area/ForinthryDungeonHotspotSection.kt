package org.jesse.game.content.wilderness.revenant.area

import org.jesse.game.world.Position
import org.jesse.game.world.region.RSPolygon
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin

/**
 * @author Jire
 */
@Suppress("unused")
class ForinthryDungeonHotspotSection : org.jesse.game.content.wilderness.revenant.area.ForinthryDungeon(), CannonRestrictionPlugin {

    override fun name(): String = "Forinthry Dungeon: Hotspot"

    override fun polygons(): Array<RSPolygon> = arrayOf(
        RSPolygon(
            arrayOf(
                intArrayOf(3246, 10221),
                intArrayOf(3256, 10222),
                intArrayOf(3262, 10222),
                intArrayOf(3260, 10135),
                intArrayOf(3248, 10135),
                intArrayOf(3241, 10142),
                intArrayOf(3241, 10153),
                intArrayOf(3227, 10162),
                intArrayOf(3225, 10173),
                intArrayOf(3211, 10180),
                intArrayOf(3202, 10192),
                intArrayOf(3202, 10200),
                intArrayOf(3238, 10220),
                intArrayOf(3246, 10221)
            )
        )
    )

    override fun isMultiwayArea(position: Position): Boolean = true
}
