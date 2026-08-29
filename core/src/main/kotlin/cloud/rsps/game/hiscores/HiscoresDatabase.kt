package cloud.rsps.game.hiscores

import com.github.michaelbull.logging.InlineLogger
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionManager
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.sql.DataSource

/**
 * @author Jire
 */
object HiscoresDatabase {

    private val logger = InlineLogger()

    lateinit var hiscoresDatabase: Database

    @JvmStatic
    @JvmOverloads
    fun init(
        create: Boolean,
        dataSource: DataSource,

        updatePeriod: Long = 10L,
        updatePeriodUnit: TimeUnit = TimeUnit.SECONDS
    ) {
        hiscoresDatabase = Database.connect(dataSource).apply {
            transactionManager.apply {
                defaultMaxAttempts = 5
                defaultMinRetryDelay = 5
                defaultMaxRetryDelay = 50
            }
        }

        if (create) {
            val allTables = buildList {
                add(HiscoreRankPointers)
                for (mode in HiscoreMode.entries) {
                    add(mode.skillsTable)
                    add(mode.skillsRankTableA)
                    add(mode.skillsRankTableB)

                    add(mode.activityTable)
                    add(mode.activityRankTableA)
                    add(mode.activityRankTableB)
                }
            }

            val indexSQL = HiscoreMode.entries.flatMap { mode ->
                listOf(
                    mode.skillsTable.createIndexQuery,
                    mode.activityTable.createIndexQuery
                )
            }
            transaction(hiscoresDatabase) {
                SchemaUtils.create(*allTables.toTypedArray())

                for (sql in indexSQL) exec(sql)

                HiscoreMode.entries.forEach { mode ->
                    val skillTableName = mode.skillsRankTableA.tableName
                    val activityTableName = mode.activityRankTableA.tableName

                    exec(
                        """
        INSERT INTO hiscore_rank_pointers (mode_index, skill_table, activity_table)
        VALUES (${mode.index}, '$skillTableName', '$activityTableName')
        ON CONFLICT (mode_index)
        DO NOTHING
        """.trimIndent()
                    )
                }
            }
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            runBlocking {
                for (mode in HiscoreMode.entries) {
                    val manager = mode.manager
                    try {
                        manager.updateSkillRanks()
                    } catch (e: Exception) {
                        logger.error(e) { "Failed to update hiscores skill ranks." }
                    }

                    try {
                        manager.updateActivityRanks()
                    } catch (e: Exception) {
                        logger.error(e) { "Failed to update hiscores activity ranks." }
                    }
                }
            }
        }, 0L, updatePeriod, updatePeriodUnit)
    }

}
