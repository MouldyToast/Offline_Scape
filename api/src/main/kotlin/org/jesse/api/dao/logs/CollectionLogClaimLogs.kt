package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.model.CollectionLogClaimLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CollectionLogClaimLogs : LongIdTable("cl_reward_claim_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val logName = varchar("cl_name", 255).index()
}

class CollectionLogClaimLogEntity(id: EntityID<Long>) : ModelEntity<CollectionLogClaimLog>(id) {
    companion object : LongEntityClass<CollectionLogClaimLogEntity>(CollectionLogClaimLogs) {
        fun new(log: CollectionLogClaimLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            logName = log.logName
        }
    }

    var time by CollectionLogClaimLogs.time
    var username by CollectionLogClaimLogs.username
    var logName by CollectionLogClaimLogs.logName
    override fun toModel(): CollectionLogClaimLog = CollectionLogClaimLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        logName = logName
    )
}