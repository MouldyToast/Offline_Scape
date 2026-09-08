package org.jesse.game.content.collectionlog

import com.google.common.eventbus.Subscribe
import org.jesse.game.world.entity.player.GameCommands.Command
import org.jesse.game.world.entity.player.collectionlog.CollectionLogRewardHandler
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.plugins.events.ServerLaunchEvent

object CollectionLogCommands {

    @JvmStatic
    @Subscribe
    fun onServerLaunched(event: ServerLaunchEvent) {
        Command(PlayerPrivilege.TRUE_DEVELOPER, "clforce") { p, args ->
            val struct = args[0].toInt()
            CollectionLogRewardHandler.forceComplete(struct, p)
        }
    }
}
