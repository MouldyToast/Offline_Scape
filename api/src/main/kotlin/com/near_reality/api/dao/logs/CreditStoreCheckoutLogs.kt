package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.username
import com.near_reality.api.model.CreditStoreCartItem
import com.near_reality.api.model.CreditStoreCheckoutLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CreditStoreCheckoutLogs : LongIdTable("credit_store_checkout_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val cart = jsonb<List<CreditStoreCartItem>>("cart", Json)
}

class CreditStoreCheckoutLogEntity(id: EntityID<Long>) : ModelEntity<CreditStoreCheckoutLog>(id) {
    companion object : LongEntityClass<CreditStoreCheckoutLogEntity>(CreditStoreCheckoutLogs) {
        fun new(log: CreditStoreCheckoutLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            cart = log.cart
        }
    }

    var time by CreditStoreCheckoutLogs.time
    var username by CreditStoreCheckoutLogs.username
    var cart by CreditStoreCheckoutLogs.cart
    override fun toModel(): CreditStoreCheckoutLog = CreditStoreCheckoutLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        cart = cart
    )
}

object LegacyCreditStoreCheckoutLogs : LongIdTable("credit_store_checkout_logs") {
    val time = datetime("time").index()
    val username = username("username").index()
    val cart = jsonb<List<CreditStoreCartItem>>("cart", Json)
}

class LegacyCreditStoreCheckoutLogEntity(id: EntityID<Long>) : ModelEntity<CreditStoreCheckoutLog>(id) {
    companion object : LongEntityClass<LegacyCreditStoreCheckoutLogEntity>(LegacyCreditStoreCheckoutLogs) {
        fun new(log: CreditStoreCheckoutLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            cart = log.cart
        }
    }

    var time by LegacyCreditStoreCheckoutLogs.time
    var username by LegacyCreditStoreCheckoutLogs.username
    var cart by LegacyCreditStoreCheckoutLogs.cart
    override fun toModel(): CreditStoreCheckoutLog = CreditStoreCheckoutLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        cart = cart
    )
}