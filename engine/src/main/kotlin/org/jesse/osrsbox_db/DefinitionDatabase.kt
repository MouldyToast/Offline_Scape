package org.jesse.osrsbox_db

import cloud.rsps.util.JacksonObjectMapper
import cloud.rsps.util.io.ByteBufferBackedInputStream
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Stopwatch
import org.slf4j.Logger
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.concurrent.TimeUnit

/**
 * @author Jire
 */
interface DefinitionDatabase<T> {

    var definitions: Map<Int, T>

    val definitionClass: Class<T>

    val logger: Logger

    fun fileName(): String

    fun loadFromFile(objectMapper: ObjectMapper) {
        val stopwatch = Stopwatch.createStarted()

        val mapType = objectMapper.typeFactory.constructMapType(
            Map::class.java,
            Int::class.java,
            definitionClass
        )

        FileChannel.open(Path.of(fileName()), StandardOpenOption.READ).use { channel ->
            val buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
            val inputStream = ByteBufferBackedInputStream(buf)
            definitions = objectMapper.readValue(inputStream, mapType)
        }

        val elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        logger.info(
            "Loaded {} {} in {} ms.",
            definitions.size, definitionClass.simpleName, elapsed
        )
    }

    fun loadFromFile() = loadFromFile(JacksonObjectMapper())

    fun buildConfigs()

    operator fun get(id: Int): T?

}
