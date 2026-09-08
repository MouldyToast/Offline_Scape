package org.jesse.api.dao.logs

import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.dao.item
import org.jesse.api.dao.itemContainer
import org.jesse.api.model.MiddleManLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object MiddleManLogs : LongIdTable("middleman_logs_v2") {
    val time = datetime("time").index()
    val requester = reference("requester", Users.username).index()
    val accepter = reference("accepter", Users.username).index()
    val middleman = reference("middleman", Users.username).index()
    val requesterDonatorPin = item("requester_donator_pin")
    val accepterItems = itemContainer("accepter_items")
    val accepterOSRSMillions = integer("accepter_osrs_millions")
}

class MiddleManLogEntity(id: EntityID<Long>) : ModelEntity<MiddleManLog>(id) {
    companion object : LongEntityClass<MiddleManLogEntity>(MiddleManLogs) {
        fun new(log: MiddleManLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            requester = log.requester
            accepter = log.accepter
            middleman = log.middleman
            requesterDonatorPin = log.requesterDonatorPin
            accepterItems = log.accepterItems
            accepterOSRSMillions = log.accepterOSRSMillions
        }
    }

    var time by MiddleManLogs.time
    var requester by MiddleManLogs.requester
    var accepter by MiddleManLogs.accepter
    var middleman by MiddleManLogs.middleman
    var requesterDonatorPin by MiddleManLogs.requesterDonatorPin
    var accepterItems by MiddleManLogs.accepterItems
    var accepterOSRSMillions by MiddleManLogs.accepterOSRSMillions

    override fun toModel(): MiddleManLog = MiddleManLog(
        time = time.toInstant(defaultTimeZone),
        requester = requester,
        accepter = accepter,
        middleman = middleman,
        requesterDonatorPin = requesterDonatorPin,
        accepterItems = accepterItems,
        accepterOSRSMillions = accepterOSRSMillions
    )
}