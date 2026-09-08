package org.jesse.game.content.boss.abyssalsire

import com.google.common.eventbus.Subscribe
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.region.GlobalAreaManager
import org.jesse.plugins.events.ServerLaunchEvent

/**
 * @author Jire
 */
object ServerLaunchSubscriber {

    @JvmStatic
    @Subscribe
    fun onServerLaunch(@Suppress("UNUSED_PARAMETER") event: ServerLaunchEvent) {
        WorldTasksManager.scheduleCreation {
            for (corner in AbyssalNexusCorner.values) {
                val lair = GlobalAreaManager[corner.areaName] as AbyssalNexusArea
                val sire = AbyssalSire(corner, lair)
                sire.spawn()
            }
        }
    }

}
