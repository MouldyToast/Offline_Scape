package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.username
import com.near_reality.api.model.Item
import com.near_reality.api.model.PrimalExchangeLog
import com.near_reality.api.model.RemnantExchangeLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object RemnantExchangeLogs : LongIdTable("remnant_exchange_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val itemId = integer("item_id").index()
    val amount = integer("item_amount")
    val value = integer("item_value")
}

class RemnantExchangeLogEntity(id : EntityID<Long>) : ModelEntity<RemnantExchangeLog>(id) {

    companion object : LongEntityClass<RemnantExchangeLogEntity>(RemnantExchangeLogs) {
        fun new(log: RemnantExchangeLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            itemId = log.item.id
            amount = log.item.amount
            value = log.value
        }
    }

    var time by RemnantExchangeLogs.time
    var username by RemnantExchangeLogs.username
    var itemId by RemnantExchangeLogs.itemId
    var amount by RemnantExchangeLogs.amount
    var value by RemnantExchangeLogs.value

    override fun toModel(): RemnantExchangeLog {
        return RemnantExchangeLog(
            time = time.toInstant(defaultTimeZone),
            username = username,
            item = Item(itemId, amount),
            value = value
        )
    }
}

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


object LegacyRemnantExchangeLogs : LongIdTable("remnant_exchange_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val itemId = integer("item_id").index()
    val amount = integer("item_amount")
    val value = integer("item_value")
}

class LegacyRemnantExchangeLogEntity(id : EntityID<Long>) : ModelEntity<RemnantExchangeLog>(id) {

    companion object : LongEntityClass<LegacyRemnantExchangeLogEntity>(LegacyRemnantExchangeLogs) {
        fun new(log: RemnantExchangeLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            itemId = log.item.id
            amount = log.item.amount
            value = log.value
        }
    }

    var time by LegacyRemnantExchangeLogs.time
    var username by LegacyRemnantExchangeLogs.username
    var itemId by LegacyRemnantExchangeLogs.itemId
    var amount by LegacyRemnantExchangeLogs.amount
    var value by LegacyRemnantExchangeLogs.value

    override fun toModel(): RemnantExchangeLog {
        return RemnantExchangeLog(
            time = time.toInstant(defaultTimeZone),
            username = username,
            item = Item(itemId, amount),
            value = value
        )
    }
}
