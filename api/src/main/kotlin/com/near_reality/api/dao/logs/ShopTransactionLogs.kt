package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.item
import com.near_reality.api.model.ShopTransactionLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ShopTransactionLogs : LongIdTable("gameshop_tx_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val shopName = varchar("shop_name", 255).index()
    val txType = char("tx_type").index()
    val item = item("item").index()
    val price = integer("price")
    val currencyName = varchar("currency_name", 128)
}

class ShopTransactionLogEntity(id: EntityID<Long>) : ModelEntity<ShopTransactionLog>(id) {
    companion object : LongEntityClass<ShopTransactionLogEntity>(ShopTransactionLogs) {
        fun new(log: ShopTransactionLog) = ShopTransactionLogEntity.new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            shopName = log.shopName
            txType = log.txType
            item = log.item
            price = log.price
            currencyName = log.currencyName
        }
    }

    var time by ShopTransactionLogs.time
    var username by ShopTransactionLogs.username
    var shopName by ShopTransactionLogs.shopName
    var txType by ShopTransactionLogs.txType
    var item by ShopTransactionLogs.item
    var price by ShopTransactionLogs.price
    var currencyName by ShopTransactionLogs.currencyName

    override fun toModel() = ShopTransactionLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        shopName = shopName,
        txType = txType,
        item = item,
        price = price,
        currencyName = currencyName
    )
}