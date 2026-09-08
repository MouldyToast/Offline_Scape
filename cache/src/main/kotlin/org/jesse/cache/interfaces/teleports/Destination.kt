package org.jesse.cache.interfaces.teleports

import org.jesse.game.world.entity.Location

/**
 * @author Jire
 */
interface Destination {

    val structID: Int

    val name: String
    val location: Location
    val spriteID: Int
    val wikiURL: String

}