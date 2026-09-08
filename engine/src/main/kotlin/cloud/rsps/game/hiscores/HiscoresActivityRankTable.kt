package cloud.rsps.game.hiscores

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

/**
 * @author Jire
 */
class HiscoresActivityRankTable(
    mode: HiscoreMode,
    version: String
) : Table("activity_rank_${mode.paramName}_$version") {

    val playerId = long("player_id")

    val activityName = varchar("activity_name", 64)

    val score = long("score")

    val rank = long("rank")

    val updatedAt = timestamp("updated_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(playerId, activityName)

}
