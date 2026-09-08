package cloud.rsps.game.hiscores

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

/**
 * @author Jire
 */
class HiscoresActivityTable(
    mode: HiscoreMode
) : Table("activity_${mode.paramName}") {

    val playerId = long("player_id")

    val activityName = varchar("activity_name", 64)

    val score = long("score")

    val updatedAt = timestamp("updated_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(playerId, activityName)

    val createIndexQuery =
        "CREATE INDEX IF NOT EXISTS idx_activity_name_score_desc_${mode.paramName}" +
                " ON $tableName(activity_name, score DESC)"

}
