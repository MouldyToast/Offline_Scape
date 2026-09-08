package org.jesse.cache.interfaces.teleports

/**
 * @author Jire
 */
interface Category {

    val name: String
    val enumID: Int
    val id: Int

    val destinations: List<Destination>

}