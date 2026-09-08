package cloud.rsps.game.hiscores

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

/**
 * @author Jire
 */
class HiscoresSkillsRankTable(
    mode: HiscoreMode,
    version: String
) : Table("skills_rank_${mode.paramName}_$version") {

    val playerId = long("player_id")

    val skillName = varchar("skill_name", 32)

    val xp = integer("xp")
    val level = short("level")

    val rank = long("rank")

    val updatedAt = timestamp("updated_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(playerId, skillName)

}
