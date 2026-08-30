package com.near_reality.api.dao.logs

import com.near_reality.api.dao.DetailedItemsTable
import com.near_reality.api.dao.KilledByNpcLogItems
import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.location
import com.near_reality.api.dao.npcName
import com.near_reality.api.model.DeathResult
import com.near_reality.api.model.KilledByNpcLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@DetailedItemsTable(KilledByNpcLogItems::class)
object KilledByNpcLogs : LongIdTable("killed_by_npc_logs_v2") {
    val time = datetime("time").index()
    val username = reference("username", Users.username).index()
    val npc = npcName("npc").index()
    val npcId = integer("npc_id").index()
    val location = location("location")
    val deathResult = jsonb<DeathResult>("death_result", Json)
}

class KilledByNpcLogEntity(id: EntityID<Long>) : ModelEntity<KilledByNpcLog>(id) {
    companion object : LongEntityClass<KilledByNpcLogEntity>(KilledByNpcLogs) {
        fun new(log: KilledByNpcLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.victim
            npc = log.killer
            npcId = log.killerId
            location = log.location
            deathResult = log.deathResult
        }
    }
    var time by KilledByNpcLogs.time
    var username by KilledByNpcLogs.username
    var npc by KilledByNpcLogs.npc
    var npcId by KilledByNpcLogs.npcId
    var location by KilledByNpcLogs.location
    var deathResult by KilledByNpcLogs.deathResult

    override fun toModel() = KilledByNpcLog(
        time = time.toInstant(defaultTimeZone),
        victim = username,
        killer = npc,
        killerId = npcId,
        location = location,
        deathResult = deathResult
    )
}
