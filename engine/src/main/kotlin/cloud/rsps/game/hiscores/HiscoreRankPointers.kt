package cloud.rsps.game.hiscores

import org.jetbrains.exposed.sql.Table

/**
 * @author Jire
 */
object HiscoreRankPointers : Table("hiscore_rank_pointers") {

    val modeIndex = byte("mode_index")

    val skillTable = varchar("skill_table", 255)
    val activityTable = varchar("activity_table", 255)

    override val primaryKey = PrimaryKey(modeIndex)

}
