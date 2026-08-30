package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.item
import com.near_reality.api.model.RareDropLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object RareDropLogs : LongIdTable("rare_drop_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val item = item("item").index()
    val itemId = integer("item_id").index().default(-1)
    val itemSource = varchar("source", 255).index()
}

class RareDropLogEntity(id: EntityID<Long>) : ModelEntity<RareDropLog>(id) {
    companion object : LongEntityClass<RareDropLogEntity>(RareDropLogs) {
        fun new(log: RareDropLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            item = log.item
            itemSource = log.itemSource
            itemId = log.item.id
        }
    }

    var time by RareDropLogs.time
    var username by RareDropLogs.username
    var item by RareDropLogs.item
    var itemSource by RareDropLogs.itemSource
    var itemId by RareDropLogs.itemId
    override fun toModel(): RareDropLog = RareDropLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        item = item,
        itemSource = itemSource
    )
}