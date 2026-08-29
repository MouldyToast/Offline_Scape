package com.near_reality.game.content.donator.new_island.npc

import com.google.common.eventbus.Subscribe
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.plugins.events.ServerLaunchEvent
import com.zenyte.utils.TimeUnit

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-26
 */
object AvatarEvent : WorldTask {

    private val avatar = AvatarOfCreation()

    private const val FIRST_SPAWN_MINUTES = 15
    private const val REPEAT_SPAWN_MINUTES = 120

    var minutesLeft = FIRST_SPAWN_MINUTES

    private val warningMinutes = setOf(10, 5, 1)

    @Subscribe
    @JvmStatic
    fun boot(event: ServerLaunchEvent) {
        WorldTasksManager.schedule(
            this,
            TimeUnit.MINUTES.toTicks(1).toInt(),
            TimeUnit.MINUTES.toTicks(1).toInt()
        )
    }

    override fun run() {
        minutesLeft--

        if (minutesLeft in warningMinutes) {
            WorldBroadcasts.sendMessage(
                "<img=68><col=00FF00><shad=000000>Avatar of Creation will be spawning in $minutesLeft minute(s) (::onyx dz)",
                BroadcastType.WORLD_BOSS,
                true
            )
        }

        if (minutesLeft <= 0) {
            if (!avatar.isFinished) {
                avatar.remove()
            }
            WorldTasksManager.schedule({
                avatar.spawn()
                minutesLeft = REPEAT_SPAWN_MINUTES }
                , 2)
        }
    }
}
