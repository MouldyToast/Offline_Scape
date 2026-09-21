package org.jesse.game.content.skills.agility.pyramid.area

import org.jesse.game.content.skills.agility.pyramid.AgilityPyramid
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.RSPolygon
import org.jesse.game.world.region.area.SouthernDesertArea
import org.jesse.game.world.region.area.plugins.CycleProcessPlugin
import org.jesse.game.world.region.area.plugins.FullMovementPlugin
import org.jesse.game.world.region.area.plugins.TeleportMovementPlugin

class AgilityPyramidArea : SouthernDesertArea(), CycleProcessPlugin, FullMovementPlugin, TeleportMovementPlugin {
    public override fun polygons(): Array<RSPolygon?> {
        return arrayOf<RSPolygon?>(
            RSPolygon(
                arrayOf<IntArray?>(
                    intArrayOf(3353, 2830),
                    intArrayOf(3353, 2853),
                    intArrayOf(3376, 2853),
                    intArrayOf(3376, 2830)
                )
            ), RSPolygon(
                arrayOf<IntArray?>(
                    intArrayOf(3033, 4686),
                    intArrayOf(3033, 4709),
                    intArrayOf(3056, 4709),
                    intArrayOf(3056, 4686)
                )
            )
        )
    }

    private var ticks = 0

    override fun process() {
        super.process()
        if (++ticks % 16 == 0) {
            MovingBlock.Companion.moveBlocks()
        }
    }

    override fun processMovement(player: Player, x: Int, y: Int): Boolean {
        RollingBlock.roll(player, x, y)
        return true
    }

    public override fun name(): String {
        return "Agility Pyramid"
    }

    override fun processMovement(player: Player, destination: Location) {
        val insideLowerPyramid = getPolygon(0).contains(destination)
        val insideHigherPyramid = getPolygon(1).contains(destination)
        player.getVarManager().sendBit(
            AgilityPyramid.Companion.MOVING_BLOCK_VARBIT,
            if (insideLowerPyramid) destination.plane else if (insideHigherPyramid) destination.plane + 2 else 0
        )
    }

    companion object {
        @JvmStatic
        fun getHigherTile(location: Location): Location? {
            if (location.plane == 3) {
                return location.transform(-320, 1856, -1)
            }
            return location.transform(0, 0, 1)
        }

        @JvmStatic
        fun getLowerTile(location: Location): Location? {
            if (location.y >= 4686 && location.y <= 4709 && location.plane == 2) {
                return location.transform(320, -1856, 1)
            }
            return location.transform(0, 0, -1)
        }
    }
}
