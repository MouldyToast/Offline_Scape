package org.jesse.api.requests

import org.jesse.api.model.CreditPackageOrder
import org.jesse.api.model.CreditStoreCartItem
import kotlinx.serialization.Serializable

@Serializable
data class CreditPackageOrderCreateRequest(
    val userId: Long,
    val paymentMethod: CreditPackageOrder.PaymentMethod,
    val creditPackageId: Int,
    val creditPackageAmount: Int
)

@Serializable
data class StoreCheckoutRequest(
    val userId: Long,
    val cart: List<CreditStoreCartItem>
)
