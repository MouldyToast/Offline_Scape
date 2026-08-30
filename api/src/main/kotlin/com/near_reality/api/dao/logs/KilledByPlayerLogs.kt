package com.near_reality.api.dao.logs

import com.near_reality.api.dao.DetailedItemsTable
import com.near_reality.api.dao.KilledByPlayerLogItems
import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.location
import com.near_reality.api.model.DeathResult
import com.near_reality.api.model.KilledByPlayerLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@DetailedItemsTable(KilledByPlayerLogItems::class)
object KilledByPlayerLogs : LongIdTable("killed_by_player_logs_v2") {
    val time = datetime("time").index()
    val killer = reference("killer", Users.username).index()
    val victim = reference("victim", Users.username).index()
    val location = location("location")
    val deathResult = jsonb<DeathResult>("death_result", Json)
}

class KilledByPlayerLogEntity(id: EntityID<Long>) : ModelEntity<KilledByPlayerLog>(id) {
    companion object : LongEntityClass<KilledByPlayerLogEntity>(KilledByPlayerLogs) {
        fun new(log: KilledByPlayerLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            killer = log.killer
            victim = log.victim
            location = log.location
            deathResult = log.deathResult
        }
    }
    var time by KilledByPlayerLogs.time
    var killer by KilledByPlayerLogs.killer
    var victim by KilledByPlayerLogs.victim
    var location by KilledByPlayerLogs.location
    var deathResult by KilledByPlayerLogs.deathResult

    override fun toModel() = KilledByPlayerLog(
        time = time.toInstant(defaultTimeZone),
        killer = killer,
        victim = victim,
        location = location,
        deathResult = deathResult
    )
}
