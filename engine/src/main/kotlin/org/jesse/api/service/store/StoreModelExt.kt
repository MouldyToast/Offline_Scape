package org.jesse.api.service.store

import org.jesse.api.model.CreditPackage
import org.jesse.api.model.CreditPackageOrder
import org.jesse.api.responses.StoreOrderCreateResponse
import org.jesse.game.item.Item

internal fun StoreOrderCreateResponse.asUpdate() = when(this) {
    is StoreOrderCreateResponse.Created -> StoreOrderUpdate.Submitted
    is StoreOrderCreateResponse.AwaitingPayment -> StoreOrderUpdate.AwaitingPayment(payUrl)
    is StoreOrderCreateResponse.Failed -> StoreOrderUpdate.Failed(reason)
    StoreOrderCreateResponse.AccountNotFound -> StoreOrderUpdate.Failed("Account not found")
    StoreOrderCreateResponse.ProductNotFound -> StoreOrderUpdate.Failed("Product not found")
}

internal fun CreditPackageOrder.asUpdate(payUrl: String? = null) = when(status) {
    CreditPackageOrder.Status.CREATED -> StoreOrderUpdate.Submitted
    CreditPackageOrder.Status.PENDING -> StoreOrderUpdate.AwaitingPayment(payUrl!!)
    CreditPackageOrder.Status.CANCELED -> StoreOrderUpdate.Canceled
    CreditPackageOrder.Status.PAID -> StoreOrderUpdate.Claimed(this)
    CreditPackageOrder.Status.DISPUTED -> StoreOrderUpdate.Failed("Failed! Order was disputed...")
}

sealed class StoreOrderUpdate {
    data object Submitted : StoreOrderUpdate()
    data class AwaitingPayment(val url: String) : StoreOrderUpdate()
    data object Canceled : StoreOrderUpdate()
    data class Claimed(val order: CreditPackageOrder) : StoreOrderUpdate()
    data class Failed(val reason: String) : StoreOrderUpdate()
}

internal val CreditPackageOrder.products: Pair<Item, Int>
    get() = (creditPackage.itemFromCreditPackage() to creditPackage.bonusCreditFromCreditPackage())

internal fun CreditPackage.itemFromCreditPackage(): Item =
    throw Exception("Invalid credit package title")

internal fun CreditPackage.bonusCreditFromCreditPackage(): Int =
    when (title) {
        "$5 DPin" -> 0
        "$10 DPin" -> 10
        "$25 DPin" -> 25
        "$35 DPin" -> 35
        "$50 DPin" -> 70
        "$100 DPin" -> 200
        else -> throw Exception("Invalid credit package title")
    }