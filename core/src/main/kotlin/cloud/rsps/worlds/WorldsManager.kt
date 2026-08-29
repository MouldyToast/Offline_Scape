package cloud.rsps.worlds

import cloud.rsps.worlds.database.WorldsTable
import com.near_reality.api.dao.Db.dbQuery
import com.near_reality.api.model.WorldType
import com.near_reality.game.world.info.WorldProfile
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.sql.upsert
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

/**
 * @author Jire
 */
object WorldsManager {

    @Volatile
    @JvmStatic
    var ourWorldPlayerCount = 0

    @Volatile
    @JvmStatic
    var jagexWorldsFile: ByteArray? = null

    @Volatile
    @JvmStatic
    var runeliteWorlds: RuneliteWorlds? = null

    suspend fun updateInDatabase(profile: WorldProfile, database: Database) {
        val settings = WorldType.toMask(profile.types.toTypedArray())
        dbQuery(database) {
            WorldsTable.upsert {
                it[WorldsTable.id] = profile.number.toUShort()
                it[WorldsTable.settings] = settings
                it[WorldsTable.host] = profile.host
                it[WorldsTable.activity] = profile.activity
                it[WorldsTable.countryFlag] = profile.location.id.toByte()
                it[WorldsTable.playerCount] = ourWorldPlayerCount.toUShort()
                it[WorldsTable.lastUpdatedDate] = CurrentTimestampWithTimeZone
            }
        }
    }

    suspend fun updateFromDatabase(
        database: Database,

        maxAge: Long = 10L,
        maxAgeUnit: TemporalUnit = ChronoUnit.SECONDS
    ) {
        val worlds = dbQuery(database) {
            WorldsTable.select(
                WorldsTable.id,
                WorldsTable.host, WorldsTable.activity, WorldsTable.settings,
                WorldsTable.countryFlag, WorldsTable.playerCount
            ).where {
                WorldsTable.lastUpdatedDate greaterEq
                        OffsetDateTime.now().minus(maxAge, maxAgeUnit)
            }.map { row ->
                object : World {
                    override val id: UShort = row[WorldsTable.id].value
                    override val settings: Int = row[WorldsTable.settings]
                    override val host: String = row[WorldsTable.host]
                    override val activity: String = row[WorldsTable.activity]
                    override val countryFlag: Byte = row[WorldsTable.countryFlag]
                    override val playerCount: UShort = row[WorldsTable.playerCount]
                }
            }
        }

        jagexWorldsFile = JagexWorldsFile.generateJagexWorldsFile(worlds = worlds)
        runeliteWorlds = RuneliteWorlds(worlds.map {
            val types: List<RuneliteWorldType> =
                RuneliteWorldType
                    .forSettings(it.settings)
            RuneliteWorld(
                it.id, types,
                it.host, it.activity, it.countryFlag,
                it.playerCount
            )
        })
    }

}
