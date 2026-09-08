package org.jesse.game.world.region.areatype.cachebased

import org.jesse.utils.MapLocations
import org.jesse.utils.efficientarea.Area

/**
 * @author Jire
 */
object SinglesPlusAreaTypeCB : CacheBasedAreaType() {
    override fun loadPlaneArea(plane: Int): Area = MapLocations.getSinglesPlusZones(plane)
}