package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.item
import com.near_reality.api.model.GrandExchangeOfferLog
import com.near_reality.api.model.GrandExchangeOfferType
import com.near_reality.api.model.GrandExchangeTransactionLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object GrandExchangeOfferLogs : LongIdTable("grand_exchange_offer_logs_v2") {
    val time = datetime("time").index()
    val creator = reference("creator", Users.username).index()
    val offerItem = item("offer_item")
    val offerPrice = integer("offer_price")
    val offerType = enumeration<GrandExchangeOfferType>("offer_type")
    val itemId = integer("item_id").index().default(-1)
}

class GrandExchangeOfferLogEntity(id: EntityID<Long>) : ModelEntity<GrandExchangeOfferLog>(id) {
    companion object : LongEntityClass<GrandExchangeOfferLogEntity>(GrandExchangeOfferLogs) {
        fun new(log: GrandExchangeOfferLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            creator = log.creator
            offerItem = log.offerItem
            offerPrice = log.offerPrice
            offerType = log.offerType
            itemId = log.offerItem.id
        }
    }
    var time by GrandExchangeOfferLogs.time
    var creator by GrandExchangeOfferLogs.creator
    var offerItem by GrandExchangeOfferLogs.offerItem
    var offerPrice by GrandExchangeOfferLogs.offerPrice
    var offerType by GrandExchangeOfferLogs.offerType
    var itemId by GrandExchangeOfferLogs.itemId

    override fun toModel() = GrandExchangeOfferLog(
        time = time.toInstant(defaultTimeZone),
        creator = creator,
        offerItem = offerItem,
        offerPrice = offerPrice,
        offerType = offerType
    )
}

object GrandExchangeTransactionLogs : LongIdTable("grand_exchange_tx_logs_v2") {
    val time = datetime("time").index()
    val creator = reference("creator", Users.username).index()
    val accepter = reference("accepter", Users.username).index()
    val offerItem = item("offer_item")
    val offerPrice = integer("offer_price")
    val offerType = enumeration<GrandExchangeOfferType>("offer_type")
    val itemId = integer("item_id").index().default(-1)
}

class GrandExchangeTransactionLogEntity(id: EntityID<Long>) : ModelEntity<GrandExchangeTransactionLog>(id) {
    companion object : LongEntityClass<GrandExchangeTransactionLogEntity>(GrandExchangeTransactionLogs) {
        fun new(log: GrandExchangeTransactionLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            creator = log.creator
            accepter = log.accepter
            offerItem = log.offerItem
            offerPrice = log.offerPrice
            offerType = log.offerType
            itemId = log.offerItem.id
        }
    }
    var time by GrandExchangeTransactionLogs.time
    var creator by GrandExchangeTransactionLogs.creator
    var accepter by GrandExchangeTransactionLogs.accepter
    var offerItem by GrandExchangeTransactionLogs.offerItem
    var offerPrice by GrandExchangeTransactionLogs.offerPrice
    var offerType by GrandExchangeTransactionLogs.offerType
    var itemId by GrandExchangeTransactionLogs.itemId

    override fun toModel() = GrandExchangeTransactionLog(
        time = time.toInstant(defaultTimeZone),
        creator = creator,
        accepter = accepter,
        offerItem = offerItem,
        offerPrice = offerPrice,
        offerType = offerType
    )

}
