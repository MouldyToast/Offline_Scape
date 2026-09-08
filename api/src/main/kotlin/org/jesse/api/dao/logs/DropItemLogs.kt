package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.dao.item
import org.jesse.api.dao.location
import org.jesse.api.model.DropItemLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object DropItemLogs : LongIdTable("drop_item_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val item = item("item")
    val itemId = integer("item_id").index().default(-1)
    val location = location("location")
}

class DropItemLogEntity(id: EntityID<Long>) : ModelEntity<DropItemLog>(id) {
    companion object : LongEntityClass<DropItemLogEntity>(DropItemLogs) {
        fun new(log: DropItemLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            item = log.item
            itemId = log.item.id
            location = log.location
        }
    }
    var time by DropItemLogs.time
    var username by DropItemLogs.username
    var item by DropItemLogs.item
    var itemId by DropItemLogs.itemId
    var location by DropItemLogs.location

    override fun toModel() = DropItemLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        item = item,
        location = location
    )
}