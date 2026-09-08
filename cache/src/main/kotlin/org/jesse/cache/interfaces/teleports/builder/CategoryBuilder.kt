package org.jesse.cache.interfaces.teleports.builder

import org.jesse.cache.interfaces.teleports.Category
import org.jesse.cache.interfaces.teleports.Destination
import org.jesse.game.world.entity.Location
import it.unimi.dsi.fastutil.objects.ObjectArrayList

/**
 * @author Jire
 */
internal class CategoryBuilder(
    override val name: String,
    override val enumID: Int,
    override val id: Int
) : Category {

    override val destinations: MutableList<Destination> = ObjectArrayList()

    operator fun String.invoke(spriteID: Int, x: Int, y: Int, z: Int = 0, wikiURL: String = ""): Destination {
        val destination = DefaultDestination(
            TeleportsBuilder.nextStructID++, this,
            Location(x, y, z), spriteID, wikiURL
        )
        destinations.add(destination)
        return destination
    }

}