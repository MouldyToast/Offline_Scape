package org.jesse.game.world.region.areatype

import org.jesse.game.world.region.areatype.filebased.MultiwayAreaTypeFB
import org.jesse.game.world.region.areatype.filebased.SinglesPlusAreaTypeFB

/**
 * @author Jire
 */
enum class AreaTypes(
    private val areaType: AreaType
) : AreaType by areaType, Runnable {

    SINGLE_WAY(SingleWayAreaType),
    SINGLES_PLUS(SinglesPlusAreaTypeFB),
    MULTIWAY(MultiwayAreaTypeFB);

    override fun run() {
        load()
        map()
    }

    companion object {
        @JvmField
        val values = entries.toTypedArray()
    }

}