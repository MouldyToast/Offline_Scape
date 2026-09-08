package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.model.Item
import org.jesse.api.model.PrimalExchangeLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object PrimalExchangeLogs : LongIdTable("primal_exchange_logs") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val itemId = integer("item_id").index()
    val amount = integer("item_amount")
    val value = integer("item_value")
}

class PrimalExchangeLogEntity(id : EntityID<Long>) : ModelEntity<PrimalExchangeLog>(id) {

    companion object : LongEntityClass<PrimalExchangeLogEntity>(PrimalExchangeLogs) {
        fun new(log: PrimalExchangeLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            itemId = log.item.id
            amount = log.item.amount
            value = log.value
        }
    }

    var time by PrimalExchangeLogs.time
    var username by PrimalExchangeLogs.username
    var itemId by PrimalExchangeLogs.itemId
    var amount by PrimalExchangeLogs.amount
    var value by PrimalExchangeLogs.value

    override fun toModel(): PrimalExchangeLog {
        return PrimalExchangeLog(
            time = time.toInstant(defaultTimeZone),
            username = username,
            item = Item(itemId, amount),
            value = value
        )
    }
}
