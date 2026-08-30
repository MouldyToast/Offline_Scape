package com.near_reality.api.dao.logs

import com.near_reality.api.dao.DetailedItemsTable
import com.near_reality.api.dao.FlowerPokerSessionLogItems
import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.Users
import com.near_reality.api.dao.itemContainer
import com.near_reality.api.dao.username
import com.near_reality.api.model.FlowerPokerSessionLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@DetailedItemsTable(FlowerPokerSessionLogItems::class)
object FlowerPokerSessionLogs : LongIdTable("flower_poker_session_logs_v2") {
    val time = datetime("time").index()
    val player1 = reference("player1", Users.username).index()
    val player2 = reference("player2", Users.username).index()
    val player1Items = itemContainer("player1_items")
    val player2Items = itemContainer("player2_items")
    val winner = username("winner")
}

class FlowerPokerSessionLogEntity(id: EntityID<Long>) : ModelEntity<FlowerPokerSessionLog>(id) {
    companion object : LongEntityClass<FlowerPokerSessionLogEntity>(FlowerPokerSessionLogs) {
        fun new(log: FlowerPokerSessionLog): FlowerPokerSessionLogEntity {
            val logEntity = new {
                time = log.time.toLocalDateTime(defaultTimeZone)
                player1 = log.player1
                player2 = log.player2
                player1Items = log.player1Items
                player2Items = log.player2Items
                winner = log.winner
            }
            FlowerPokerSessionLogItems.insert(logEntity.id.value, 1, log.player1, log.player1Items)
            FlowerPokerSessionLogItems.insert(logEntity.id.value, 2, log.player2, log.player2Items)
            return logEntity
        }
    }
    var time by FlowerPokerSessionLogs.time
    var player1 by FlowerPokerSessionLogs.player1
    var player2 by FlowerPokerSessionLogs.player2
    var player1Items by FlowerPokerSessionLogs.player1Items
    var player2Items by FlowerPokerSessionLogs.player2Items
    var winner by FlowerPokerSessionLogs.winner

    override fun toModel() = FlowerPokerSessionLog(
        time = time.toInstant(defaultTimeZone),
        player1 = player1,
        player2 = player2,
        player1Items = player1Items,
        player2Items = player2Items,
        winner = winner
    )
}
