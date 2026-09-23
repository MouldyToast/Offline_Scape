package org.jesse

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import cloud.rsps.game.hiscores.HiscoreEntries
import cloud.rsps.game.hiscores.HiscoresServer
import cloud.rsps.rsprot.Session
import cloud.rsps.worlds.WorldsServer
import com.google.common.base.Stopwatch
import org.jesse.api.GameDatabase
import org.jesse.game.world.info.WorldConfig
import org.jesse.game.world.info.WorldProfile
import org.jesse.network.NetworkServiceFactory
import org.jesse.osrsbox_db.ItemDefinitionDatabase
import org.jesse.osrsbox_db.MonsterDefinitionDatabase
import org.jesse.cores.CoresManager
import org.jesse.cores.ScheduledExternalizableManager
import org.jesse.game.GameConstants
import org.jesse.game.GameLoader
import org.jesse.game.content.consumables.Consumable
import org.jesse.game.content.grandexchange.GrandExchangeHandler
import org.jesse.game.content.multicannon.DwarfMultiCannon
import org.jesse.game.content.skills.mining.MiningDefinitions
import org.jesse.game.model.item.ItemActionHandler
import org.jesse.game.world.World
import org.jesse.game.world.entity.npc.actions.NPCPlugin
import org.jesse.game.world.entity.npc.combatdefs.NPCCDLoader
import org.jesse.game.world.entity.npc.drop.matrix.NPCDrops
import org.jesse.game.world.entity.npc.spawns.NPCSpawnLoader
import org.jesse.game.world.flooritem.GlobalItem
import org.jesse.game.world.`object`.Door
import org.jesse.game.world.`object`.ObjectExamineLoader
import org.jesse.game.world.region.GlobalAreaManager
import org.jesse.game.world.region.XTEALoader
import org.jesse.game.world.region.areatype.AreaTypes
import org.jesse.logger.NearRealityLogger
import org.jesse.plugins.PluginClasspathScan
import org.jesse.plugins.PluginManager
import org.jesse.plugins.PluginScanner
import org.jesse.plugins.events.PluginsLoadedEvent
import org.jesse.plugins.events.ServerLaunchEvent
import org.jesse.server.AttributesSerializable
import org.jesse.server.ServerAttributes
import org.jesse.utils.ElapsedTimes.runLogElapsed
import io.netty.util.ResourceLeakDetector
import mgi.types.Definitions
import mgi.types.config.items.ItemDefinitions
import net.rsprot.protocol.api.NetworkService
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

/**
 * @author Jire
 * @author Tommeh
 */
@OptIn(ExperimentalStdlibApi::class)
object Main {

    private val log = NearRealityLogger.getLogger(Main::class.java)

    @JvmStatic
    val serverStartTime = System.nanoTime()

    @JvmStatic
    lateinit var networkService: NetworkService<Session>

    @JvmStatic
    val networkServiceLock = Any()

    @JvmStatic
    fun main(args: Array<String>) {
        Thread.currentThread().name = "Main Launch Thread"
        initializeServerAttributes()
        try {
            launch(args)
        } catch (t: Throwable) {
            log.error("", t)
            t.printStackTrace()
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun launch(args: Array<String>) {
        val stopwatch = Stopwatch.createStarted()

        Locale.setDefault(Locale.US)
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"))

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED) // XXX: in production, use DISABLED
        System.setProperty("io.netty.allocator.maxCachedBufferCapacity", "65535")

        val worldProfile = configureWorldProfile(*args)

        logElapsed("Initialized game database.") { GameDatabase.init(worldProfile) }

        val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

        logElapsed("Loaded game loader.") { GameLoader.load(pool) }

        logElapsed("Built network service.") {
            System.setProperty("net.rsprot.protocol.internal.networkLogging", "info")
            System.setProperty("net.rsprot.protocol.internal.js5Logging", "warn")
            System.setProperty("net.rsprot.protocol.internal.npcAvatarMaxId", "65534")
            System.setProperty("net.rsprot.protocol.internal.development", "false")

            networkService = NetworkServiceFactory(worldProfile, CacheManager.getCache()).build()
        }

        logElapsed("Initialized cores manager.") { CoresManager.init(worldProfile, networkService) }

        logElapsed("Loaded game.") { GameLoader.load(pool) }
        logElapsed("Loaded item definitions.") {
            ItemDefinitions.loadDefinitions(
                pool,
                ItemDefinitionDatabase::loadFromFile
            ) { ItemDefinitionDatabase.buildConfigs() }

        }
        logElapsed("Created game engine.") {
            pool.invokeAll(World::init, Consumable::initialize, GlobalItem::load)
        }

        logElapsed("Initialized hiscore entries.") {
            HiscoreEntries.init()
        }

        load(pool)

        val worldThread = CoresManager.startWorldThread()
        val serverLaunchEvent = ServerLaunchEvent(worldProfile, worldThread)
        logElapsed("Posted server launch event.") {
            PluginManager.post(serverLaunchEvent)
        }

        logElapsed("Started network service.") {
            networkService.start()
        }

        if (worldProfile.isHiscoreDatabaseEnabled()) {
            logElapsed("Started hiscores server.") {
                HiscoresServer
                    .create(worldProfile.hiscoresServerConfig)
                    .start(wait = false)
            }
        }

        if (worldProfile.isWorldsDatabaseEnabled()) {
            logElapsed("Started worlds server.") {
                WorldsServer
                    .create(worldProfile.worldsServerConfig)
                    .start(wait = false)
            }
        }

        val elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        log.info("${GameConstants.SERVER_NAME} took {} milliseconds to launch.", elapsed)

        pool.execute(::notifyErrorLogs)

        pool.shutdown()
        if (!pool.awaitTermination(5, TimeUnit.MINUTES)) {
            log.error("Could not finish loading server within timeout!")
            exitProcess(-1)
        }
    }

    private lateinit var serverAttributes: ServerAttributes

    private fun initializeServerAttributes() {
        serverAttributes = ServerAttributes()
        serverAttributes = AttributesSerializable.getFromFile(
            ServerAttributes.getSaveFile(),
            serverAttributes
        )
    }

    fun load(pool: ExecutorService) {
        logElapsed("Loaded low priority definitions.") {
            pool.invokeAll(
                *Definitions.serverLowPriorityDefinitions
                    .map(Definitions::load)
                    .toTypedArray()
            )
        }
        logElapsed("Loaded mining definitions.") {
            MiningDefinitions.load()
        }
        logElapsed("Loaded dwarf multi cannon.") {
            DwarfMultiCannon.init()
        }
        logElapsed("Loaded NPC combat definitions.") {
            NPCCDLoader.parse(pool)

            MonsterDefinitionDatabase.loadFromFile()
            MonsterDefinitionDatabase.buildConfigs()
        }
        logElapsed("Initialized world tasks") { World.initTasks() }
        logElapsed("Loaded shops, examines, drops, grand exchange, XTEAs, and area types.") {
            pool.invokeAll(
                ObjectExamineLoader::loadExamines,
                NPCDrops::init,
                GrandExchangeHandler::init,
                Door::load,
                { XTEALoader.load("cache/data/objects/xteas.json") },
                AreaTypes.MULTIWAY,
                AreaTypes.SINGLES_PLUS
            )
        }
        logElapsed("Loaded plugins.") { PluginScanner.scanAndLoad(PluginClasspathScan.scan) }
        logElapsed("Posted plugin manager loaded event.") { PluginManager.post(PluginsLoadedEvent()) }

        logElapsed("Set defaults for item action handler.") { ItemActionHandler.setDefaults() }
        logElapsed("Set inheritance for global area manager.") { GlobalAreaManager.setInheritance() }
        logElapsed("Filtered NPC plugin.") { NPCPlugin.filter() }
        logElapsed("Loaded NPC spawns.") { NPCSpawnLoader.loadNPCSpawns() }
        //logElapsed("Initialized SQL manager.") { SQLManager.init() }

        logElapsed("Mapped global area manager.") {
            GlobalAreaManager.map()
        }

        logElapsed("Launched login manager.") {
            CoresManager.getLoginManager().launch()
        }
    }

    private fun notifyErrorLogs() {
        val errorLogPath = Path.of("data", "logs", "error.log")
        if (Files.exists(errorLogPath) && Files.size(errorLogPath) > 0) {
            log.warn("Some errors from previous session(s) are logged at {}; review and delete them.", errorLogPath)
        }
    }

    @JvmStatic
    fun configureWorldProfile(vararg args: String): WorldProfile {
        val key = if (args.isNotEmpty()) args[0] else "localhost"
        val worldConfig = WorldConfig.fromJson(Path.of("worlds.json"))
        GameConstants.WORLD_CONFIG = worldConfig
        val worldProfile = worldConfig[key] ?: error("World profile '$key' not found.")
        GameConstants.WORLD_PROFILE = worldProfile
        CacheManager.DEVELOPMENT_MODE = worldProfile.isDevelopment()
        configureLogging(worldProfile)
        return worldProfile
    }

    private fun configureLogging(worldProfile: WorldProfile) {
        val file = File("./app/${worldProfile.logback}.xml")
        val context: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val jc = JoranConfigurator()
        jc.context = context
        context.reset()
        jc.doConfigure(file.absolutePath)
    }

    private fun ExecutorService.invokeAll(vararg runnables: Runnable) = invokeAll(runnables.map(::callable))

    private fun callable(runnable: Runnable): Callable<Void> = Callable {
        try {
            runnable.run()
        } catch (e: Exception) {
            log.error("Failure loading callable", e)
        }
        null
    }

    private fun logElapsed(message: String, runnable: Runnable) = runLogElapsed(log, message, runnable = runnable)

}
