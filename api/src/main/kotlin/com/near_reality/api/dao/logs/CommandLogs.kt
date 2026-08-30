package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.model.CommandLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CommandLogs : LongIdTable("command_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val commandName = varchar("command_name", 50)
    val commandParameters = array<String>("command_parameters")
}

class CommandLogEntity(id: EntityID<Long>) : ModelEntity<CommandLog>(id) {
    companion object : LongEntityClass<CommandLogEntity>(CommandLogs) {
        fun new(log: CommandLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            commandName = log.commandName
            commandParameters = log.commandParameters
        }
    }
    var time by CommandLogs.time
    var username by CommandLogs.username
    var commandName by CommandLogs.commandName
    var commandParameters by CommandLogs.commandParameters

    override fun toModel() = CommandLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        commandName = commandName,
        commandParameters = commandParameters.toList()
    )
}