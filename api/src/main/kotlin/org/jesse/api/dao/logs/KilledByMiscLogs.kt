package org.jesse.api.dao.logs

import org.jesse.api.dao.DetailedItemsTable
import org.jesse.api.dao.MiscDeathLogItems
import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.dao.location
import org.jesse.api.model.DeathResult
import org.jesse.api.model.MiscDeathLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@DetailedItemsTable(MiscDeathLogItems::class)
object MiscDeathLogs : LongIdTable("misc_death_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val location = location("location")
    val deathResult = jsonb<DeathResult>("death_result", Json)
}

class MiscDeathLogEntity(id: EntityID<Long>) : ModelEntity<MiscDeathLog>(id) {
    companion object : LongEntityClass<MiscDeathLogEntity>(MiscDeathLogs) {
        fun new(log: MiscDeathLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            location = log.location
            deathResult = log.deathResult
        }
    }
    var time by MiscDeathLogs.time
    var username by MiscDeathLogs.username
    var location by MiscDeathLogs.location
    var deathResult by MiscDeathLogs.deathResult

    override fun toModel() = MiscDeathLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        location = location,
        deathResult = deathResult
    )
}
