package cloud.rsps.game.hiscores

import cloud.rsps.game.hiscores.HiscoreRankTablePointer.Companion.getTablePointer
import cloud.rsps.game.hiscores.HiscoreRankTablePointer.Companion.withToTable
import cloud.rsps.game.hiscores.HiscoresDatabase.hiscoresDatabase
import cloud.rsps.util.Base37
import com.github.michaelbull.logging.InlineLogger
import org.jesse.api.dao.Db.dbQuery
import org.jesse.game.world.entity.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.statements.BatchUpsertStatement

/**
 * @author Jire
 */
class HiscoresManager(val mode: HiscoreMode) {

    suspend fun loadResponse(playerId: Long): HiscoreResponse = dbQuery(hiscoresDatabase) {
        val (skillTable, activityTable) = getTablePointer(mode)!!

        val skills = exec("SELECT skill_name, xp, level, rank FROM $skillTable WHERE player_id = $playerId") {
            buildList {
                while (it.next()) {
                    add(
                        HiscoreSkill(
                            it.getString(1),
                            it.getLong(4),
                            it.getShort(3),
                            it.getInt(2)
                        )
                    )
                }
            }
        }!!

        val activities = exec("SELECT activity_name, score, rank FROM $activityTable WHERE player_id = $playerId") {
            buildList {
                while (it.next()) {
                    add(
                        HiscoreActivity(
                            it.getString(1),
                            it.getLong(3),
                            it.getLong(2)
                        )
                    )
                }
            }
        }!!

        HiscoreResponse(skills, activities)
    }

    @JvmOverloads
    fun updatePlayer(
        player: Player,
        playerId: Long = Base37.encode(player.username),
    ) {
        val skills: MutableList<HiscoreSkillData> = mutableListOf()
        HiscoreEntries.skills.values.forEach { entry ->
            val xp = entry.getXp(player)
            val level = entry.getLevel(player)
            skills.add(
                HiscoreSkillData(entry.name, level, xp)
            )
        }

        val activities: MutableList<HiscoreActivityData> = mutableListOf()
        HiscoreEntries.activities.values.forEach { entry ->
            val score = entry.getScore(player)
            activities.add(
                HiscoreActivityData(entry.name, score)
            )
        }

        coroutineScope.launch {
            dbQuery(hiscoresDatabase) {
                val table = mode.skillsTable
                val statement = BatchUpsertStatement(
                    table = table,
                    keys = arrayOf(table.playerId, table.skillName),
                    onUpdateExclude = null,
                    where = null
                )
                for (skill in skills) {
                    statement.addBatch()
                    statement[table.playerId] = playerId
                    statement[table.skillName] = skill.name
                    statement[table.level] = skill.level
                    statement[table.xp] = skill.xp
                    statement[table.updatedAt] = CurrentTimestamp
                }
                statement.execute(this)
            }

            dbQuery(hiscoresDatabase) {
                val table = mode.activityTable
                val statement = BatchUpsertStatement(
                    table = table,
                    keys = arrayOf(table.playerId, table.activityName),
                    onUpdateExclude = null,
                    where = null
                )
                for (activity in activities) {
                    statement.addBatch()
                    statement[table.playerId] = playerId
                    statement[table.activityName] = activity.name
                    statement[table.score] = activity.score
                    statement[table.updatedAt] = CurrentTimestamp
                }
                statement.execute(this)
            }
        }
    }

    suspend fun updateSkillRanks() {
        for (mode in HiscoreMode.entries) {
            val index = mode.index
            val fromTable = mode.skillsTable.tableName
            dbQuery(hiscoresDatabase) {
                withToTable("skill_table", index) { toTable ->
                    exec("TRUNCATE TABLE $toTable")
                    exec(
                        """
                        INSERT INTO $toTable (player_id, skill_name, xp, level, rank)
                        SELECT player_id, skill_name, xp, level,
                            RANK() OVER (PARTITION BY skill_name ORDER BY xp DESC) AS rank
                        FROM $fromTable
                    """.trimIndent()
                    )
                }
            }
        }
    }

    suspend fun updateActivityRanks() {
        for (mode in HiscoreMode.entries) {
            val index = mode.index
            val fromTable = mode.activityTable.tableName
            dbQuery(hiscoresDatabase) {
                withToTable("activity_table", index) { toTable ->
                    exec("TRUNCATE TABLE $toTable")
                    exec(
                        """
                        INSERT INTO $toTable (player_id, activity_name, score, rank)
                        SELECT player_id, activity_name, score,
                               RANK() OVER (PARTITION BY activity_name ORDER BY score DESC) AS rank
                        FROM $fromTable
                    """.trimIndent()
                    )
                }
            }
        }
    }

    private companion object {

        private val logger = InlineLogger()

        private val coroutineScope = CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        )

    }

}
