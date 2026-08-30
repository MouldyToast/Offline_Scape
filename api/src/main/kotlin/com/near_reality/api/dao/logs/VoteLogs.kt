package com.near_reality.api.dao.logs

import com.near_reality.api.dao.ModelEntity
import com.near_reality.api.dao.username
import com.near_reality.api.model.VoteLog
import com.near_reality.api.util.defaultTimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object VoteLogs : LongIdTable("vote_logs_v2") {
    val time = datetime("time").index()
    val username = username("username")
    val ipAddress = varchar("ip", 128).index()
    val votesClaimed = integer("votes_claimed").default(0)
    val votesBonus = integer("votes_bonus").default(0)
}

class VoteLogEntity(id: EntityID<Long>) : ModelEntity<VoteLog>(id) {
    companion object : LongEntityClass<VoteLogEntity>(VoteLogs) {
        fun new(log: VoteLog) = new {
            time = log.time.toLocalDateTime(defaultTimeZone)
            username = log.username
            ip = log.ipAddress
            votesClaimed = log.votesClaimed
            votesBonus = log.votesBonus
        }
    }

    var time by VoteLogs.time
    var username by VoteLogs.username
    var ip by VoteLogs.ipAddress
    var votesClaimed by VoteLogs.votesClaimed
    var votesBonus by VoteLogs.votesBonus
    override fun toModel(): VoteLog = VoteLog(
        time = time.toInstant(defaultTimeZone),
        username = username,
        ipAddress = ip,
        votesClaimed = votesClaimed,
        votesBonus = votesBonus
    )
}