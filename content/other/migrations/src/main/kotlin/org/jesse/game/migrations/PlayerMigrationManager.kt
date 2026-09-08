package org.jesse.game.migrations

import com.google.common.base.Stopwatch
import com.google.common.eventbus.Subscribe
import org.jesse.game.world.entity.player.migrationVersion
import org.jesse.game.world.entity.player.Player
import org.jesse.logger.NearRealityLogger
import org.jesse.plugins.events.LoginEvent
import org.jesse.plugins.events.ServerLaunchEvent
import org.jesse.utils.ClassInitializer
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * A system used to migrate certain variables on various different triggers like login
 * @author John J. Woloszyk / Kryeus
 */
@Suppress("Unused")
object PlayerMigrationManager {
    private val log = NearRealityLogger.getLogger(PlayerMigrationManager::class.java)
    private val migrations = mutableMapOf<Int, GameMigration>()
    private lateinit var sortedMigrations : SortedMap<Int, GameMigration>

    @Subscribe
    @JvmStatic
    fun onServerLaunch(event: ServerLaunchEvent) {
        scanMigrations()
    }

    @Subscribe
    @JvmStatic
    fun onPlayerLogin(event: LoginEvent) {
        performPlayerLoginMigrations(event.player)
    }

    private fun scanMigrations() {
        val stopwatch = Stopwatch.createStarted()

        val scanner = ClassGraph()
        scanner.ignoreMethodVisibility()
        scanner.enableAnnotationInfo()
        scanner.enableMethodInfo()
        scanner.enableClassInfo()
        scanner.enableExternalClasses()
        scanner.acceptPackages("org.jesse.game.migrations.impl")

        log.debug("Scanning for migrations in classpath.")
        scanner.scan().use { result ->
            val callables =
                ObjectArrayList<Callable<Void?>>(1000)
            val lock = Any()
            result.allClasses
                .forEach(Consumer { clazz: ClassInfo ->
                    callables.add(
                        Callable<Void?> {
                            if (clazz.hasAnnotation(ActiveMigration::class.java)) {
                                log.trace("Loaded migration: " + clazz.simpleName)
                                ClassInitializer.initialize(clazz.loadClass())
                                val obj = clazz.loadClass()
                                synchronized(lock) {
                                    val migration = obj.getDeclaredConstructor().newInstance() as GameMigration
                                    migrations.put(migration.id(), migration)
                                }
                            }
                            null
                        })
                })
            ForkJoinPool.commonPool().invokeAll(callables)
            log.info(
                "Loaded a total of {} migrations from the classpath in {}ms.",
                migrations.size,
                stopwatch.elapsed(TimeUnit.MILLISECONDS)
            )
        }
        sortedMigrations = migrations.toSortedMap()
    }

    @JvmStatic
    private fun performPlayerLoginMigrations(player: Player) {
        for((id, migration) in sortedMigrations) {
            if(player.migrationVersion < id) {
                log.debug("Performing player migration #$id for ${player.username}")
                player.sendDeveloperMessage("Running migration: " + migration.javaClass.simpleName)
                migration.run(player)
                player.migrationVersion = id
            }
        }
    }
}
