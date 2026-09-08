package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.dao.item
import org.jesse.api.dao.location
import org.jesse.api.model.PickupItemLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object PickupItemLogs : LongIdTable("pickup_item_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val item = item("item")
    val itemId = integer("item_id").index().default(-1)
    val location = location("location")
}

class PickupItemLogEntity(id: EntityID<Long>) : ModelEntity<PickupItemLog>(id) {
    companion object : LongEntityClass<PickupItemLogEntity>(PickupItemLogs) {
        fun new(log: PickupItemLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            item = log.item
            itemId = log.item.id
            location = log.location
        }
    }
    var time by PickupItemLogs.time
    var username by PickupItemLogs.username
    var item by PickupItemLogs.item
    var location by PickupItemLogs.location
    var itemId by PickupItemLogs.itemId

    override fun toModel() = PickupItemLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        item = item,
        location = location
    )
}