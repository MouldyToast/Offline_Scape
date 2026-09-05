package com.near_reality.game.content.chaoskey

import com.zenyte.game.world.entity.Location
import java.util.*


/**
 * @author Alycia <https:></https:>//github.com/alycii>
 * Represents locations for Chaos Chest spawns.
 */
enum class ChaosChestLocations(

    val locationName: String, location: Location,
    /**
     * Gets the rotation of the chest object.
     *
     * @return The rotation.
     */
    val rotation: Int
) {
    DARK_WARRIORS_FORTRESS("Dark Warriors' Fortress", Location(3027, 3631), 4),
    REVENANT_DRAGONS("Revenant Dragons", Location(3232, 10205), 4),
    SOUTH_WEST_LAVA_DRAGON_ISLE("South-west Lava Dragon Isle", Location(3244, 3791), 4),
    FROZEN_WASTE_PLATEAU("Frozen Waste Plateau", Location(2984, 3961), 2),
    ;

    private val location: Location = location

    /**
     * Gets the location.
     *
     * @return The location.
     */
    fun getLocation(): Location {
        return location
    }

    fun getName() = locationName

    companion object {
        val random: ChaosChestLocations
            /**
             * Gets a random ChaosChestLocations enum.
             *
             * @return A random ChaosChestLocations enum.
             */
            get() {
                val random = Random()
                val values = entries.toTypedArray()
                return values[random.nextInt(values.size)]
            }
    }
}