package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.dao.item
import org.jesse.api.dao.location
import org.jesse.api.model.TeleGrabItemLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object TeleGrabItemLogs : LongIdTable("telegrab_item_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val item = item("item")
    val itemId = integer("item_id").index().default(-1)
    val location = location("location")
}

class TeleGrabItemLogEntity(id: EntityID<Long>) : ModelEntity<TeleGrabItemLog>(id) {
    companion object : LongEntityClass<TeleGrabItemLogEntity>(TeleGrabItemLogs) {
        fun new(log: TeleGrabItemLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            item = log.item
            itemId = log.item.id
            location = log.location
        }
    }
    var time by TeleGrabItemLogs.time
    var username by TeleGrabItemLogs.username
    var item by TeleGrabItemLogs.item
    var location by TeleGrabItemLogs.location
    var itemId by TeleGrabItemLogs.itemId

    override fun toModel() = TeleGrabItemLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        item = item,
        location = location
    )
}