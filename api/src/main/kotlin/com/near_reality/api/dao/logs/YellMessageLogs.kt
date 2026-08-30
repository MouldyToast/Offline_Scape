package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.model.YellMessageLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object YellMessageLogs : LongIdTable("yell_message_logs_v2") {
    val time = datetime("time").index()
    val sender = reference("sender", Users.username).index()
    val message = varchar("message", 255).index()
}

class YellMessageLogEntity(id: EntityID<Long>) : ModelEntity<YellMessageLog>(id) {
    companion object : LongEntityClass<YellMessageLogEntity>(YellMessageLogs) {
        fun new(log: YellMessageLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            sender = log.sender
            message = log.message
        }
    }

    var time by YellMessageLogs.time
    var sender by YellMessageLogs.sender
    var message by YellMessageLogs.message
    override fun toModel(): YellMessageLog = YellMessageLog(
        time = time.toInstant(defaultTimeZone),
        sender = sender,
        message = message
    )
}