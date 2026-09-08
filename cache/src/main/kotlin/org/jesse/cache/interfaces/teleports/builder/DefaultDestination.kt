package org.jesse.cache.interfaces.teleports.builder

import org.jesse.cache.interfaces.teleports.Destination
import org.jesse.game.world.entity.Location

/**
 * @author Jire
 */
internal data class DefaultDestination(
    override val structID: Int,

    override val name: String,
    override val location: Location,
    override val spriteID: Int,
    override val wikiURL: String
) : Destination