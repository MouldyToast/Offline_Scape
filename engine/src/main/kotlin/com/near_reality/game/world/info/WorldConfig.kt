package com.near_reality.game.world.info

import cloud.rsps.util.JacksonObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.MapType
import kotlinx.serialization.Serializable
import java.nio.file.Files
import java.nio.file.Path

/**
 * Represents the configuration for the worlds.
 *
 * @param worlds the map of world id's to world profiles.
 *
 * @see WorldProfile for more information.
 *
 * @author Stan van der Bend
 * @author Jire
 */
@Serializable
data class WorldConfig(
    val worlds: Map<String, WorldProfile>
) {

    operator fun get(key: String) : WorldProfile? = worlds[key]

    companion object {

        @JvmStatic
        @JvmOverloads
        fun fromJson(
            filePath: Path,

            objectMapper: ObjectMapper = JacksonObjectMapper(),
            valueType: MapType = objectMapper.typeFactory.constructMapType(
                Map::class.java,

                String::class.java,
                WorldProfile::class.java
            )
        ): WorldConfig {
            val worlds: Map<String, WorldProfile>
            Files.newBufferedReader(filePath, Charsets.UTF_8).use { reader ->
                worlds = objectMapper.readValue(reader, valueType)
            }
            return WorldConfig(worlds)
        }

    }
}
