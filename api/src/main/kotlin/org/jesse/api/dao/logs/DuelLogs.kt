package org.jesse.api.dao.logs

import org.jesse.api.dao.DetailedItemsTable
import org.jesse.api.dao.DuelLogItems
import org.jesse.api.dao.ModelEntity
import org.jesse.api.dao.Users
import org.jesse.api.dao.itemContainer
import org.jesse.api.model.DuelLog
import org.jesse.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@DetailedItemsTable(DuelLogItems::class)
object DuelLogs : LongIdTable("duel_logs_v2") {
    val time = datetime("time").index()
    val winner = reference("winner", Users.username)
    val player1 = reference("player1", Users.username).index()
    val player2 = reference("player2", Users.username).index()
    val player1Items = itemContainer("player1_items")
    val player2Items = itemContainer("player2_items")
}

class DuelLogEntity(id: EntityID<Long>) : ModelEntity<DuelLog>(id) {
    companion object : LongEntityClass<DuelLogEntity>(DuelLogs) {
        fun new(log: DuelLog): DuelLogEntity {
            val logEntity = new {
                time = log.time.toLocalDateTime(defaultTimeZone)
                winner = log.winner
                player1 = log.player1
                player2 = log.player2
                player1Items = log.player1Items
                player2Items = log.player2Items
            }
            DuelLogItems.insert(logEntity.id.value, 1, log.player1, log.player1Items)
            DuelLogItems.insert(logEntity.id.value, 2, log.player2, log.player2Items)
            return logEntity
        }
    }
    var time by DuelLogs.time
    var winner by DuelLogs.winner
    var player1 by DuelLogs.player1
    var player2 by DuelLogs.player2
    var player1Items by DuelLogs.player1Items
    var player2Items by DuelLogs.player2Items

    override fun toModel() = DuelLog(
        time = time.toInstant(defaultTimeZone),
        winner = winner,
        player1 = player1,
        player2 = player2,
        player1Items = player1Items,
        player2Items = player2Items
    )
}
