package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.username
import com.near_reality.api.model.PublicMessageLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object PublicMessageLogs : LongIdTable("public_message_logs_v2") {
    val time = datetime("time").index()
    val sender = reference("sender", Users.username).index()
    val message = varchar("message", 255).index()
}

class PublicMessageLogEntity(id: EntityID<Long>) : ModelEntity<PublicMessageLog>(id) {
    companion object : LongEntityClass<PublicMessageLogEntity>(PublicMessageLogs) {
        fun new(log: PublicMessageLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            sender = log.sender
            message = log.message
        }
    }

    var time by PublicMessageLogs.time
    var sender by PublicMessageLogs.sender
    var message by PublicMessageLogs.message
    override fun toModel(): PublicMessageLog = PublicMessageLog(
        time = time.toInstant(defaultTimeZone),
        sender = sender,
        message = message
    )
}