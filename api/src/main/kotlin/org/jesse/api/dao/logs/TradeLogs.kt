package org.jesse.api.dao.logs

import org.jesse.api.dao.*
import org.jesse.api.model.TradeLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@DetailedItemsTable(TradeLogItems::class)
object TradeLogs : LongIdTable("trade_logs_v2") {
    val time = datetime("time").index()
    val user1 = reference("first_user", Users.username).index()
    val user2 = reference("second_user", Users.username).index()
    val items1 = itemContainer("items")
    val items2 = itemContainer("other_items")
    val location = location("location")
}

class TradeLogEntity(id: EntityID<Long>) : ModelEntity<TradeLog>(id) {
    companion object : LongEntityClass<TradeLogEntity>(TradeLogs) {
        fun new(log: TradeLog): TradeLogEntity {
            val logEntity = new {
                time = log.time.toLocalDateTime(defaultTimeZone)
                user1 = log.user1
                user2 = log.user2
                items1 = log.items1
                items2 = log.items2
                location = log.location
            }
            TradeLogItems.insert(logEntity.id.value, 1, log.user1, log.items1)
            TradeLogItems.insert(logEntity.id.value, 2, log.user2, log.items2)
            return logEntity
        }
    }
    var time by TradeLogs.time
    var user1 by TradeLogs.user1
    var user2 by TradeLogs.user2
    var items1 by TradeLogs.items1
    var items2 by TradeLogs.items2
    var location by TradeLogs.location

    override fun toModel() = TradeLog(
        time = time.toInstant(defaultTimeZone),
        user1 = user1,
        user2 = user2,
        items1 = items1,
        items2 = items2,
        location = location
    )
}