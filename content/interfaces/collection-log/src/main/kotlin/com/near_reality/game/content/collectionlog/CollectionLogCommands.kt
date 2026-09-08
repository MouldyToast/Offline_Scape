package com.near_reality.game.content.collectionlog

import com.google.common.eventbus.Subscribe
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.collectionlog.CollectionLogRewardHandler
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.plugins.events.ServerLaunchEvent

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
