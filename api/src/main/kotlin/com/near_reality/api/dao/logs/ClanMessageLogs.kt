package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.username
import com.near_reality.api.model.ClanMessageLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ClanMessageLogs : LongIdTable("clan_message_logs_v2") {
    val time = datetime("time").index()
    val sender = reference("sender", Users.username).index()
    val channelName = varchar("channel_name", 20).index()
    val channelOwner = username("channel_owner").index()
    val message = varchar("message", 255).index()
}

class ClanMessageLogEntity(id: EntityID<Long>) : ModelEntity<ClanMessageLog>(id) {
    companion object : LongEntityClass<ClanMessageLogEntity>(ClanMessageLogs) {
        fun new(log: ClanMessageLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            sender = log.sender
            channelName = log.channelName
            channelOwner = log.channelOwner
            message = log.message
        }
    }

    var time by ClanMessageLogs.time
    var sender by ClanMessageLogs.sender
    var channelName by ClanMessageLogs.channelName
    var channelOwner by ClanMessageLogs.channelOwner
    var message by ClanMessageLogs.message
    override fun toModel(): ClanMessageLog = ClanMessageLog(
        time = time.toInstant(defaultTimeZone),
        sender = sender,
        channelName = channelName,
        channelOwner = channelOwner,
        message = message
    )
}