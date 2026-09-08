package org.jesse.api.dao

import org.jesse.api.model.SanctionLog
import org.jesse.api.model.SanctionType
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

/* Sanction logs are going to remain in V1, due to existing sanctions */
object SanctionLogs : LongIdTable("sanction_logs") {
    val time = datetime("time").index()
    val reporter = username("reporter").index()
    val offender = username("offender").index()
    val type = enumeration<SanctionType>("type").index()
    val reason = text("reason")
    val expiresAt = datetime("expires_at").nullable()
}

class SanctionLogEntity(id: EntityID<Long>) : ModelEntity<SanctionLog>(id) {
    companion object : LongEntityClass<SanctionLogEntity>(SanctionLogs) {
        fun new(log: SanctionLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            reporter = log.reporter
            offender = log.offender
            kind = log.kind
            reason = log.reason
            expiresAt = log.expiresAt?.toLocalDateTime(defaultTimeZone)
        }
    }
    var time by SanctionLogs.time
    var reporter by SanctionLogs.reporter
    var offender by SanctionLogs.offender
    var kind by SanctionLogs.type
    var reason by SanctionLogs.reason
    var expiresAt by SanctionLogs.expiresAt

    override fun toModel() = SanctionLog(
        time = time.toInstant(defaultTimeZone),
        reporter = reporter,
        offender = offender,
        kind = kind,
        reason = reason,
        expiresAt = expiresAt?.toInstant(defaultTimeZone)
    )
}