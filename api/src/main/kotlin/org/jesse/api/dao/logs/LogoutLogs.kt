package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.model.LogoutLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object LogoutLogs : LongIdTable("logout_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val ip = varchar("ip", 15).index()
}

class LogoutLogEntity(id: EntityID<Long>) : ModelEntity<LogoutLog>(id) {
    companion object : LongEntityClass<LogoutLogEntity>(LogoutLogs) {
        fun new(log: LogoutLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            ip = log.ip
        }
    }
    var time by LogoutLogs.time
    var username by LogoutLogs.username
    var ip by LogoutLogs.ip

    override fun toModel() = LogoutLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        ip = ip
    )
}