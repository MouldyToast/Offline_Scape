package org.jesse.api.facade

import org.jesse.api.model.CreditPackage
import org.jesse.api.model.CreditPackageOrder
import org.jesse.api.model.CreditStoreCartItem
import org.jesse.api.responses.StoreCheckoutResponse
import org.jesse.api.responses.StoreOrderCreateResponse
import org.jesse.api.responses.StoreOrderUpdateResponse

interface StoreFacade {

    /**
     * Creates a new [CreditPackageOrder] of a [StoreCreditPackage] associated with he [creditPackageId],
     * for the [GameAccount] associated with [userId].
     *
     * The payment transaction is handled through the argued [paymentMethod].
     */
    suspend fun createCreditPackageOrder(
        creditPackageId: Int,
        creditPackageAmount: Int,
        userId: Long,
        paymentMethod: CreditPackageOrder.PaymentMethod
    ) : StoreOrderCreateResponse

    suspend fun updateCreditPackageOrder(
        orderId: Int,
        status: CreditPackageOrder.Status,
        transactionId: String? = null
    ) : StoreOrderUpdateResponse

    suspend fun findCreditPackageOrder(orderId: Int): CreditPackageOrder?

    suspend fun findCreditPackageOrderByTransactionId(transactionId: String): CreditPackageOrder?

    suspend fun listCreditPackages(): List<CreditPackage>

    suspend fun checkout(userId: Long, cart: List<CreditStoreCartItem>): StoreCheckoutResponse
}
