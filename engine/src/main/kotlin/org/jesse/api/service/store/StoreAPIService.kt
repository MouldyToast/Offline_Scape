@file:Suppress("unused")

package org.jesse.api.service.store

import org.jesse.api.model.CreditPackageOrder
import org.jesse.api.resources.Store
import org.jesse.api.service.APIService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Handles the Store API service.
 *
 * @author Stan van der Bend
 */
internal object StoreAPIService : APIService() {

    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun Routing.orderCallback() {
        post<Store.Order.Callback> {
            try {
                val order = call.receive<CreditPackageOrder>()
                logger.info("Received store order callback: $order")
                ioScope.launch {
                    if (order.status == CreditPackageOrder.Status.PAID) {
                        logger.info("Claiming store order: $order")
                        StorePlayerHandler.tryClaim(order)
                    }
                }
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                logger.error("Failed to handle store order callback", e)
            }
        }
    }
}
