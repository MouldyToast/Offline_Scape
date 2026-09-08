package org.jesse.game.content.boss.abyssalsire.lairs

import org.jesse.game.content.boss.abyssalsire.AbyssalNexusArea
import org.jesse.game.content.boss.abyssalsire.AbyssalNexusCorner
import org.jesse.game.world.region.RSPolygon

/**
 * @author Jire
 */
class AbyssalNexusSouthEastLair : AbyssalNexusArea() {
	override fun polygons() = arrayOf(RSPolygon(AbyssalNexusCorner.SOUTH_EAST.regionID))

	override fun name() = AbyssalNexusCorner.SOUTH_EAST.areaName
}