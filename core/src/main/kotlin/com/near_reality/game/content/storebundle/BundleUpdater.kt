package com.near_reality.game.content.storebundle

import com.google.common.eventbus.Subscribe
import com.near_reality.api.GameDatabase
import com.near_reality.api.model.CreditStoreBundleItem
import com.near_reality.game.model.ui.credit_store.CreditStoreModel
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.logger.NearRealityLogger
import com.zenyte.plugins.events.ServerLaunchEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.6.2025
 */
object BundleUpdater {
    @JvmStatic
    var bundleItemsByBundleChest = mapOf<BundleChest, List<CreditStoreBundleItem>>()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val logger = NearRealityLogger.getLogger(CreditStoreModel::class.java)
    @JvmField var lastUpdate = Instant.DISTANT_PAST

    @Subscribe
    @JvmStatic
    fun onGameLaunched(serverLaunchEvent: ServerLaunchEvent) {
        if (!serverLaunchEvent.worldProfile.isMainDatabaseEnabled()) {
            return
        }

        logger.info("Starting store product fetcher, fetching products every 10 seconds.")

        scope.launch {
            delay(10.seconds)
            while(true) {
                try {
                    requestBundlesUpdate()
                } catch (e: Exception) {
                    logger.error("Error fetching store products", e)
                } finally {
                    delay(60.seconds)
                }
            }
        }
    }

    fun requestBundlesUpdate() {
        scope.launch {
            try {
                fetchProductFromDatabase()
            } catch (e: Exception) {
                logger.error("Error fetching store products", e)
            }
        }
    }

    private suspend fun fetchProductFromDatabase() {
        val products = GameDatabase.retrieveBundleItems()
        WorldTasksManager.schedule {
            updateBundles(products)
        }
    }

    private fun updateBundles(products: List<CreditStoreBundleItem>) {
        bundleItemsByBundleChest = buildMap {
            BundleChest.entries.forEach { chest ->
                put(chest, products.filter {it.bundle == chest.ordinal }.toList())
            }
        }

        lastUpdate = Clock.System.now()
    }

}
