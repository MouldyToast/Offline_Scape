package org.jesse.api.service.store

import org.jesse.api.GameDatabase
import org.jesse.api.model.CreditPackageOrder
import org.jesse.game.world.entity.player.totalDonatedAfterLaunch
import org.jesse.cores.CoresManager
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.logger.NearRealityLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Handles player related store actions.
 *
 * @author Stan van der Bend
 */
object StorePlayerHandler {

    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val logger = NearRealityLogger.getLogger(StorePlayerHandler::class.java)

    lateinit var creditPurchasesSinceRelaunch: Map<Long, Int>

    internal fun tryClaim(order: CreditPackageOrder, onSuccess: () -> Unit = {}) {
        fun Player.tryClaim() {
            WorldTasksManager.schedule {
                if (storeClaimedOrders.add(order.id)) {
                    sendDeveloperMessage("Claimed order: ${order.id}")
                    this.totalDonatedAfterLaunch += Math.round(order.creditPackage.price)
                    user = order.user
                    onSuccess()
                } else
                    sendDeveloperMessage("Failed to claim order: ${order.id}")
            }
        }

        val user = order.user
        val onlinePlayer = World.getPlayer(user.name)
        if (onlinePlayer.isPresent) {
            onlinePlayer.get().tryClaim()
        } else {
            CoresManager.getLoginManager().load(true, order.user.name) { offlinePlayer ->
                if (offlinePlayer.isPresent)
                    offlinePlayer.get().tryClaim()
                else
                    logger.error("Failed to claim order: ${order.id} for user: ${order.user.name}, user not found.")
            }
        }
    }

    fun populateOrderData() {
        ioScope.launch {
            creditPurchasesSinceRelaunch = GameDatabase.getCreditPurchaseSinceRelaunch()
        }
    }
}
