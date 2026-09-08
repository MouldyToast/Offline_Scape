@file:Suppress("unused")

package org.jesse.game.content.boss.nex

import com.google.common.eventbus.Subscribe
import org.jesse.game.world.PlayerEvent
import org.jesse.game.world.hook
import org.jesse.cores.CoresManager
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Direction
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.plugins.events.ServerLaunchEvent
import org.jesse.utils.TimeUnit
import org.slf4j.LoggerFactory

/**
 * Handles the global mechanics of the Nex fight.
 *
 * @author Stan van der bend
 */
object NexModule {

    /**
     * The number of ticks it takes for Nex to spawn once at least one person has passed the Ancient barrier.
     */
    var NEX_SPAWN_DELAY = 40 // in ticks

    /**
     * Contains global statistics related to Nex kills/deaths.
     */
    lateinit var statistics: NexStatistics

    internal val logger = LoggerFactory.getLogger(NexModule::class.java)

    /**
     * The currently active Nex instance spawned.
     */
    var nex: NexNPC? = null

    /**
     * `true` when the [spawnTask] has been queued.
     */
    private var spawning: Boolean = false

    /**
     * The [WorldTask] that [spawns nex][spawnNex] if the Ancient Prison room is not empty.
     */
    private val spawnTask = object : WorldTask {
        var tick = 0
        override fun run() {
            logger.debug("Sequencing spawn task (tick = {})", tick)
            if (AncientChamberArea.countPlayersInPrison() <= 0) {
                logger.debug("Stopping spawn task because not enough players in prison.")
                stop()
                return
            } else if (++tick >= NEX_SPAWN_DELAY) {
                spawnNex()
                stop()
            } else if (tick % 10 == 0) {
                AncientChamberArea.getPlayers().forEach {
                    it.sendMessage("Nex will spawn in ${TimeUnit.TICKS.toSeconds((NEX_SPAWN_DELAY - tick).toLong())} seconds.")
                }
            }
        }

        override fun stop() {
            super.stop()
            tick = 0
            spawning = false
        }
    }

    /**
     * Registers Nex-related commands.
     *
     * @see NexCommands.register the commands.
     */
    @JvmStatic
    @Subscribe
    fun onServerLaunched(event: ServerLaunchEvent) {
        NexCommands.register()
        event.worldThread.hook<PlayerEvent.Process> {
            it.player.processChoking()
        }
    }

    /**
     * Updates the active [statistics] after [nex] is killed and writes it to a file.
     */
    @JvmStatic
    fun updateKillStatistics(startTime: Long) {
        statistics.globalKillCount++
        val duration = System.currentTimeMillis() - startTime
        val lengthInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration).toInt()
        logger.debug("Nex is killed in {}ms ({}s)", duration, lengthInSeconds)
        val oldRecord = statistics.globalBestKillTimeSeconds
        if (oldRecord <= 0 || lengthInSeconds < oldRecord) {
            statistics.globalBestKillTimeSeconds = lengthInSeconds
        }
        CoresManager.slowExecutor.execute(NexStatistics::write)
    }

    /**
     * Invoked when someone passes the Ancient Barrier and upon death of Nex.
     */
    fun tryStartSpawnTimer() {
        if (!isNexSpawned()) {
            if (!spawning) {
                logger.debug("Spawning nex")
                spawning = true
                WorldTasksManager.schedule(spawnTask, 0, 1)
            } else
                logger.debug("Nex is already spawning")
        } else
            logger.debug("Nex is already spawned")
    }

    /**
     * Creates a new instance of [NexNPC] and sets [nex] to it.
     */
    fun spawnNex() {
        logger.debug("Spawning nex, last = {}", nex)
        WorldTasksManager.stop(spawnTask)
        spawning = false
        nex?.finish()
        nex = World.spawnNPC(
            NexNPC.NO_ATK,
            Location(2924, 5202),
            Direction.WEST,
            0
        ) as NexNPC
        nex!!.init(null)
        nex!!.switchStage(NexStage.SPAWN)
    }

    fun getNexNPC(): NexNPC? {
        return nex
    }

    /**
     * `true` when [nex] is not [finished][NexNPC.finished].
     */
    fun isNexSpawned() = nex?.isFinished == false

    /**
     * Get the name of the [NexNPC.phase] or an empty string if [nex] is null or does not have a phase assigned.
     */
    fun getNexStatus(): String = nex?.phase?.name?.lowercase() ?: ""
}
