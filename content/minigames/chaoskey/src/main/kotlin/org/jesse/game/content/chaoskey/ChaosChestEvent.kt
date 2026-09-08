package org.jesse.game.content.chaoskey

import com.google.common.eventbus.Subscribe
import org.jesse.game.obj.ids.*
import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject
import org.jesse.plugins.events.ServerLaunchEvent
import java.util.function.Consumer


/**
 * @author Alycia <https:></https:>//github.com/alycii>
 * Represents an event for spawning a Chaos Chest in the game world.
 */
class ChaosChestEvent : WorldTask {
    private val CHEST_SPAWN = CHAOS_CHEST_SPAWN
    private val LOOTABLE_CHEST = CHAOS_CHEST_SPAWNED

    private var cycle = 0

    /**
     * The Chaos Chest object in the game world.
     */
    var chaosChest: WorldObject? = null

    /**
     * The location where the Chaos Chest is spawned.
     */
    private var location: ChaosChestLocations = ChaosChestLocations.random

    /**
     * Flag indicating if the Chaos Chest event is currently active.
     */
    var active: Boolean = false

    override fun run() {
        if (instance!!.cycle == START_TIME) {
            // TODO: Turn this into an "above-chat" broadcast that has a countdown
            World.getPlayers().forEach(Consumer { p: Player ->
                p.sendMessage(" <img=53>  Event: The Chaos Chest has spawned at <col=800002>" + location.getName() + "</col>!")
            })
            instance!!.chaosChest = WorldObject(
                CHEST_SPAWN,
                10,
                location.rotation,
                location.getLocation().x,
                location.getLocation().y,
                location.getLocation().plane
            )
            instance!!.active = true
        }
        // Cycle the events
        when (instance!!.cycle) {
            START_TIME + 1 -> World.spawnObject(
                instance!!.chaosChest, false
            )

            START_TIME + 3 -> World.spawnObject(instance!!.chaosChest!!.transform(LOOTABLE_CHEST), false)
            START_TIME + 300 -> {
                World.removeObject(instance!!.chaosChest)
                World.getPlayers().forEach(Consumer { p: Player ->
                    p.sendMessage(" <img=53>  The Chaos Chest event has finished and the chest has de-spawned.")
                })
                resetState()
            }
        }
        instance!!.cycle++
    }

    /**
     * Resets the state of the Chaos Chest event.
     */
    fun resetState() {
        instance!!.cycle = 0
        instance!!.active = false
        instance!!.location = ChaosChestLocations.random
    }


    /**
     * Gets the current cycle of the Chaos Chest event.
     *
     * @return The current cycle.
     */
    fun getCycle(): Int {
        return instance!!.cycle
    }

    /**
     * Gets the location.
     *
     * @return The location.
     */
    fun getLocation(): ChaosChestLocations {
        return instance!!.location
    }

    companion object {
        /**
         * The starting time (in cycles) when the Chaos Chest should spawn.
         */
        const val START_TIME: Int = 12000
        private var instance: ChaosChestEvent? = null

        /**
         * Gets the instance of the ChaosChestEvent.
         *
         * @return The instance of ChaosChestEvent.
         */
        fun getInstance(): ChaosChestEvent {
            return if (instance == null) ChaosChestEvent() else instance!!
        }

        /**
         * Event handler for server launch event. Schedules the ChaosChestEvent task.
         *
         * @param event The server launch event.
         */
        @Subscribe
        fun boot(event: ServerLaunchEvent?) {
            instance = ChaosChestEvent()
            instance!!.resetState()
            schedule(instance!!, Utils.random(1000, 1500), 0)
        }
    }
}