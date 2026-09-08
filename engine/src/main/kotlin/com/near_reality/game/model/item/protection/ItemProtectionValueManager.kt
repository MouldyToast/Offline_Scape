package com.near_reality.game.model.item.protection

import com.near_reality.api.service.item.ItemConfigManager
import com.google.common.base.Stopwatch
import com.zenyte.cores.CoresManager
import com.zenyte.logger.NearRealityLogger
import java.io.File
import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.TimeUnit

object ItemProtectionValueManager {

    private val logger: Logger =
        LoggerFactory.getLogger(ItemProtectionValueManager::class.java)

    private val protectionValuesFilePath =
        Path.of("data", "items", "protection_values.csv")

    private lateinit var protectionValues: Int2IntMap

    internal fun loadProtectionValues() {
        val stopwatch = Stopwatch.createStarted()

        if (Files.exists(protectionValuesFilePath)) {
            val lines: List<String>
            Files.newBufferedReader(protectionValuesFilePath).use { reader ->
                lines = reader.readLines()
            }

            protectionValues = Int2IntOpenHashMap(lines.size)

            for (line in lines) {
                if (!line.contains(',')) continue
                val split = line.split(',')
                try {
                    val id = split.first().toInt()
                    val value = split.last().toInt()

                    protectionValues.put(id, value)
                } catch (e: Exception) {
                    logger.error("Failed to load protection value at line `'$line'`", e)
                }
            }
        }

        val elapsedMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        logger.info("Loaded {} protection values in {}ms.", protectionValues.size, elapsedMillis)
    }

    private fun saveProtectionValues() {
        val all = protectionValues.toMap()
        CoresManager.slowExecutor.execute {
            try {
                Files.newBufferedWriter(protectionValuesFilePath).use { writer ->
                    for ((id, value) in all) {
                        writer.write("$id,$value")
                        writer.newLine()
                    }
                }
                logger.info("Saved protection values to {}", protectionValuesFilePath)
            } catch (e: Exception) {
                logger.error("Failed to save protection values", e)
            }
        }
    }

    fun updateProtectionValue(id: Int, value: Int) {
        protectionValues[id] = value
        logger.info("Updated protection value for item $id to $value")
        saveProtectionValues()
    }

    fun getProtectionValue(id: Int): Int {
        if(ItemConfigManager.hasPresentConfig(id)) {
            val protectionValue = ItemConfigManager.protectValue(id)
            if(protectionValue.isPresent) {
                updateProtectionValue(id, protectionValue.get())
                return protectionValue.get()
            }
        }
        return protectionValues[id] ?: 0
    }
}
