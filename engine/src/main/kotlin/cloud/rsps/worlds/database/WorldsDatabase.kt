package cloud.rsps.worlds.database

import cloud.rsps.worlds.WorldsManager
import com.near_reality.game.world.info.WorldProfile
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
object WorldsDatabase {

    lateinit var worldsDatabase: Database

    @JvmStatic
    fun init(worldProfile: WorldProfile, create: Boolean, dataSource: DataSource) {
        val worldsDatabase = Database.connect(dataSource).apply {
            transactionManager.apply {
                defaultMaxAttempts = 5
                defaultMinRetryDelay = 5
                defaultMaxRetryDelay = 50
            }
        }
        this.worldsDatabase = worldsDatabase

        if (create) {
            transaction(worldsDatabase) {
                SchemaUtils.create(WorldsTable)
            }
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            {
                runBlocking {
                    WorldsManager.updateInDatabase(worldProfile, worldsDatabase)
                    WorldsManager.updateFromDatabase(worldsDatabase)
                }
            },
            0L,
            5L,
            TimeUnit.SECONDS
        )
    }

}
