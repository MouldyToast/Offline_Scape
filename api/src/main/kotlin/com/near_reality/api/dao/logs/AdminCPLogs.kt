package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.model.AdminCPLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object AdminCPLogs : LongIdTable("admin_cp_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val actionTaken = varchar("action", 255)
}

class AdminCPLogEntity(id: EntityID<Long>) : ModelEntity<AdminCPLog>(id) {
    companion object : LongEntityClass<AdminCPLogEntity>(AdminCPLogs) {
        fun new(log: AdminCPLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            actionTaken = log.action
        }
    }

    var time by AdminCPLogs.time
    var username by AdminCPLogs.username
    var actionTaken by AdminCPLogs.actionTaken

    override fun toModel() =  AdminCPLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        action = actionTaken
    )
}