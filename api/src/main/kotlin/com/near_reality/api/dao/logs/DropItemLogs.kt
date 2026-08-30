package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.item
import com.near_reality.api.dao.location
import com.near_reality.api.model.DropItemLog
import com.near_reality.api.util.defaultTimeZone
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