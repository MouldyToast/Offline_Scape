package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.model.GameServerLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object GameServerLogs : LongIdTable("server_logs") {
    val time = datetime("time").index()
    val severity = text("severity")
    val message = text("message")
}

class GameServerLogEntity(id: EntityID<Long>) : ModelEntity<GameServerLog>(id) {
    companion object : LongEntityClass<GameServerLogEntity>(GameServerLogs) {
        fun new(log: GameServerLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            severity = log.severity
            message = log.message
        }
    }
    var time by GameServerLogs.time
    var severity by GameServerLogs.severity
    var message by GameServerLogs.message

    override fun toModel() = GameServerLog(
        time = time.toInstant(defaultTimeZone),
        severity = severity,
        message = message
    )
}
