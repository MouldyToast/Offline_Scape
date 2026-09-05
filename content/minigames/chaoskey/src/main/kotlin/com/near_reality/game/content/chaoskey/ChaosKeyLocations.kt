package com.near_reality.game.content.chaoskey

import com.zenyte.game.world.entity.Location
import java.util.*

enum class ChaosKeyLocations(
    private val locationName: String, location: Location
) {
    BANDIT_CAMP("Bandit Camp", Location(3039, 3700)),
    LAVA_DRAGONS("Lava Dragons", Location(3198, 3824)),

    OBELISK("Obelisk (Lvl 44)", Location(2980, 3866)),
    DEMONIC_RUINS("Demonic Ruins", Location(2984, 3961)),
    LAVA_BRIDGE("Lava Bridge", Location(3367, 3936)),
    ;

    private val location: Location = location

    fun getName(): String {
        return locationName
    }
    /**
     * Gets the location.
     *
     * @return The location.
     */
    fun getLocation(): Location {
        return location
    }

    companion object {
        val random: ChaosKeyLocations
            /**
             * Gets a random ChaosKeyLocations enum.
             *
             * @return A random ChaosKeyLocations enum.
             */
            get() {
                val random = Random()
                val values = entries.toTypedArray()
                return values[random.nextInt(values.size)]
            }
    }
}