package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.model.LoginLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object LoginLogs : LongIdTable("login_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val ip = varchar("ip", 15).index()
}

class LoginLogEntity(id: EntityID<Long>) : ModelEntity<LoginLog>(id) {
    companion object : LongEntityClass<LoginLogEntity>(LoginLogs) {
        fun new(log: LoginLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            ip = log.ip
        }
    }
    var time by LoginLogs.time
    var username by LoginLogs.username
    var ip by LoginLogs.ip

    override fun toModel() = LoginLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        ip = ip
    )
}