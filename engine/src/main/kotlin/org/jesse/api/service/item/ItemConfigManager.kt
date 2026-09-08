package org.jesse.api.service.item

import com.google.common.eventbus.Subscribe
import org.jesse.api.GameDatabase
import org.jesse.api.model.ItemConfig
import org.jesse.game.item.Item
import org.jesse.game.task.WorldTasksManager
import org.jesse.logger.NearRealityLogger
import org.jesse.plugins.events.ServerLaunchEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

object ItemConfigManager {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Subscribe
    @JvmStatic
    fun onGameLaunched(serverLaunchEvent: ServerLaunchEvent) {
        if (!serverLaunchEvent.worldProfile.isMainDatabaseEnabled()) {
            return
        }

        logger.info("Starting item config manager, fetching item data every 10 seconds.")

        scope.launch {
            delay(10.seconds)
            while(true) {
                try {
                    refresh()
                } catch (e: Exception) {
                    logger.error("Error fetching item configs", e)
                } finally {
                    delay(120.seconds)
                }
            }
        }
    }

    private val logger = NearRealityLogger.getLogger(ItemConfigManager::class.java)
    @JvmStatic val itemConfigs = ConcurrentHashMap<Int, ItemConfig>()

    fun refresh() {
        logger.info("Refreshing item configs...")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newItemConfigs = GameDatabase.retrieveItemConfigs()
                logger.info("Retrieved ${newItemConfigs.size} item configs from the database.")
                WorldTasksManager.schedule(1) {
                    runItemConfigChange(newItemConfigs)
                }
            } catch (e: Exception) {
                logger.error("Failed to refresh item configs, keeping old ones.", e)
            }
        }
    }

    /**
     * This updates itemConfigs being accessed by the WorldThread and as such
     * should only be updated on the world thread. It should NOT be touched
     * in a coroutine which may potentially cause a ConcurrentModificationException
     */
    @JvmStatic fun runItemConfigChange(newItemConfigs: List<ItemConfig>) {
        itemConfigs.clear()
        Item.itemConfigs.clear()
        newItemConfigs.forEach { itemConfigs[it.id] = it; Item.itemConfigs[it.id] = it }
    }

    fun hasPresentConfig(id: Int): Boolean = itemConfigs.containsKey(id)
    fun sellPrice(id: Int): Optional<Int> = itemConfigs[id]?.generalStore?.let { Optional.of(it) } ?: Optional.empty()
    fun protectValue(id: Int): Optional<Int> = itemConfigs[id]?.protectionValue?.let { Optional.of(it) } ?: Optional.empty()
    fun tradeable(id: Int): Optional<Boolean> = itemConfigs[id]?.tradeable?.let { Optional.of(it) } ?: Optional.empty()

    operator fun get(itemId: Int) = itemConfigs[itemId]?: error("Item config not found for item id: $itemId")
}
