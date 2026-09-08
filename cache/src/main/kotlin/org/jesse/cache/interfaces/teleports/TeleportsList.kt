package org.jesse.cache.interfaces.teleports

import org.jesse.cache.interfaces.teleports.builder.TeleportsBuilder.Companion.teleports
import org.jesse.cache.interfaces.teleports.categories.*
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap

/**
 * @author Jire
 */
object TeleportsList {

    @JvmStatic
    val teleports: Teleports by lazy(LazyThreadSafetyMode.NONE) {
        teleports {
            training()
            cities()
            skilling()
            wilderness()
            bosses()
            dungeons()
            minigames()
            misc()
        }
    }

    @JvmStatic
    val allTeleports = teleports.categories.flatMap { it.destinations }

    @JvmStatic
    val categories: Map<String, Category> by lazy(LazyThreadSafetyMode.NONE) {
        val categories = teleports.categories
        val map: MutableMap<String, Category> = Object2ObjectOpenHashMap(categories.size)
        for (category in categories) {
            map[category.name.lowercase()] = category
        }
        map
    }

}