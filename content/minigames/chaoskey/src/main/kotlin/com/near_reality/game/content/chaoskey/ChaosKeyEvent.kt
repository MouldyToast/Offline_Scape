package com.near_reality.game.content.chaoskey

import com.google.common.eventbus.Subscribe
import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.HintArrow
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import com.zenyte.plugins.events.LogoutEvent
import com.zenyte.plugins.events.ServerLaunchEvent
import java.util.function.Consumer

/**
 * @author Alycia <https:></https:>//github.com/alycii>
 * Represents an event for spawning a Chaos Key in the game world.
 */
class ChaosKeyEvent : WorldTask {
    private var cycle = 0

    /**
     * The Chaos Key item in the game world.
     */
    var chaosKey: Item = Item(CHAOS_KEY_ACTIVE)

    /**
     * The location where the Chaos Key is spawned.
     */
    private var location: ChaosKeyLocations = ChaosKeyLocations.random

    override fun run() {
        if (instance!!.cycle == START_TIME) {
            // TODO: Turn this into an "above-chat" broadcast that has a countdown
            World.getPlayers().forEach(Consumer { p: Player ->
                p.sendMessage("<img=54> Event: The Chaos Key has spawned at <col=800002>" + location.getName() + "</col>! It will de-spawn in 5 minutes.")
            })
            World.spawnFloorItem(chaosKey, location.getLocation(), null, 0, 500)
            // Reset the event
            instance!!.resetState()
        }

        // Handle Chaos Key holder logic
        val looter = World.getPlayers().stream()
            .filter { p: Player -> p.inventory.containsItem(CHAOS_KEY_ACTIVE) }
            .findFirst()

        looter.ifPresent { player: Player ->
            if (WildernessArea.isWithinWilderness(player.position)) {
                // TODO: Enable this when chatbox messages are no longer broadcasted
                // WorldBroadcasts.sendMessage("<img=54> " + player.getUsername() + " has the Chaos Key at Lvl " + WildernessArea.getWildernessLevel(player.getLocation()).orElse(0) + " wilderness!", BroadcastType.WELL_OF_GOODWILL, true);

                // TODO: only send if the player doesn't have a BH target/or similar

                World.getPlayers().stream()
                    .filter { p: Player ->
                        WildernessArea.isWithinWilderness(
                            p.position
                        ) && !p.temporaryAttributes.containsKey("last hint arrow")
                    }
                    .forEach { p: Player ->
                        p.packetDispatcher.sendHintArrow(
                            HintArrow(
                                player
                            )
                        )
                    }
            } else {
                if (player.inventory.deleteItem(
                        CHAOS_KEY_ACTIVE,
                        1
                    ).result == RequestResult.SUCCESS
                ) {
                    player.inventory.addItem(CHAOS_KEY, 1)
                    World.getPlayers()
                        .forEach(Consumer { p: Player ->
                            p.sendMessage("<img=54> Event: " + player.username + " made it out of the Wilderness with the <col=800002>Chaos Key</col>.")
                            // TODO: handle checking if the hint arrow is pointed at someone other than the looter
                            if (p.temporaryAttributes.containsKey("last hint arrow")) {
                                p.packetDispatcher.resetHintArrow()
                            }
                        })
                }
            }
        }

        instance!!.cycle++
    }


    /**
     * Resets the state of the Chaos Key event.
     */
    fun resetState() {
        instance!!.cycle = 0
        instance!!.location = ChaosKeyLocations.random
    }


    /**
     * Gets the current cycle of the Chaos Key event.
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
    fun getLocation(): ChaosKeyLocations {
        return instance!!.location
    }

    companion object {
        /**
         * The starting time (in cycles) when the Chaos Key should spawn.
         */
        const val START_TIME: Int = 6000

        private var instance: ChaosKeyEvent? = null

        /**
         * Gets the instance of the ChaosKeyEvent.
         *
         * @return The instance of ChaosKeyEvent.
         */
        fun getInstance(): ChaosKeyEvent {
            return if (instance == null) ChaosKeyEvent() else instance!!
        }

        /**
         * Event handler for server launch event. Schedules the ChaosKeyEvent task.
         *
         * @param event The server launch event.
         */
        @Subscribe
        fun boot(event: ServerLaunchEvent) {
            instance = ChaosKeyEvent()
            instance!!.resetState()
            schedule(instance!!, Utils.random(500), 0)
        }

        @Subscribe
        @JvmStatic fun onLogout(ev: LogoutEvent) {
            val player = ev.player
            if (player.inventory.containsItem(CHAOS_KEY_ACTIVE)) {
                player.inventory.deleteItem(CHAOS_KEY_ACTIVE, 1)
                World.spawnFloorItem(Item(CHAOS_KEY_ACTIVE), player.location, null, 0, 500)
                World.getPlayers().forEach(Consumer<Player> { p: Player ->
                    p.sendMessage("<img=54> Event: " + player.username + " logged out with the <col=800002>Chaos Key</col>.")
                    p.sendMessage(
                        "<img=54> Event: It has been dropped in the wild at <col=800002>Lvl " + WildernessArea.getWildernessLevel(
                            player.location
                        ).orElse(0) + " </col>."
                    )
                })
            }
        }

    }
}