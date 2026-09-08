package org.jesse.game.content.boss.abyssalsire.lairs

import org.jesse.game.content.boss.abyssalsire.AbyssalNexusArea
import org.jesse.game.content.boss.abyssalsire.AbyssalNexusCorner
import org.jesse.game.world.region.RSPolygon

/**
 * @author Jire
 */
class AbyssalNexusNorthWestLair : AbyssalNexusArea() {
	override fun polygons() = arrayOf(RSPolygon(AbyssalNexusCorner.NORTH_WEST.regionID))

	override fun name() = AbyssalNexusCorner.NORTH_WEST.areaName
}