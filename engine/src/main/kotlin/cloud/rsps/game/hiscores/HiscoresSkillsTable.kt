package cloud.rsps.game.hiscores

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

/**
 * @author Jire
 */
class HiscoresSkillsTable(
    mode: HiscoreMode
) : Table("skills_${mode.paramName}") {

    val playerId = long("player_id")

    val skillName = varchar("skill_name", 32)

    val xp = integer("xp")
    val level = short("level")

    val updatedAt = timestamp("updated_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(playerId, skillName)

    val createIndexQuery =
        "CREATE INDEX IF NOT EXISTS idx_skill_name_xp_desc_${mode.paramName}" +
                " ON $tableName(skill_name, xp DESC)"

}
