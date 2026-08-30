package com.zenyte.game.content.boss.abyssalsire

import com.google.common.eventbus.Subscribe
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.plugins.events.ServerLaunchEvent

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
