package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.dao.username
import org.jesse.api.model.PrivateMessageLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object PrivateMessageLogs : LongIdTable("private_message_logs_v2") {
    val time = datetime("time").index()
    val sender = reference("sender", Users.username).index()
    val receiver = username("receiver").index()
    val message = varchar("message", 255).index()
}

class PrivateMessageLogEntity(id: EntityID<Long>) : ModelEntity<PrivateMessageLog>(id) {
    companion object : LongEntityClass<PrivateMessageLogEntity>(PrivateMessageLogs) {
        fun new(log: PrivateMessageLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            sender = log.sender
            receiver = log.receiver
            message = log.message
        }
    }

    var time by PrivateMessageLogs.time
    var sender by PrivateMessageLogs.sender
    var receiver by PrivateMessageLogs.receiver
    var message by PrivateMessageLogs.message
    override fun toModel(): PrivateMessageLog = PrivateMessageLog(
        time = time.toInstant(defaultTimeZone),
        sender = sender,
        receiver = receiver,
        message = message
    )
}