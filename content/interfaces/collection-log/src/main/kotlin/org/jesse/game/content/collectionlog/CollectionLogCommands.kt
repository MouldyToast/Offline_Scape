package org.jesse.game.content.collectionlog

import com.google.common.eventbus.Subscribe
import org.jesse.game.world.entity.player.GameCommands.Command
import org.jesse.game.item.Item
import org.jesse.game.world.entity.player.collectionlog.CollectionLog
import org.jesse.game.world.entity.player.collectionlog.CollectionLogRewardHandler
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.plugins.events.ServerLaunchEvent

object CollectionLogCommands {

    @JvmStatic
    @Subscribe
    fun onServerLaunched(event: ServerLaunchEvent) {
        Command(PlayerPrivilege.DEVELOPER, "clforce") { p, args ->
            val struct = args[0].toInt()
            CollectionLogRewardHandler.forceComplete(struct, p)
        }
        Command(PlayerPrivilege.DEVELOPER, "clfill") { p, _ ->
            for (itemId in CollectionLog.COLLECTION_LOG_ITEMS) {
                p.collectionLog.add(Item(itemId, 1))
            }
            p.sendMessage("Collection log filled: ${p.collectionLog.container.size} / ${CollectionLog.COLLECTION_LOG_ITEMS.size} items.")
        }
        Command(PlayerPrivilege.DEVELOPER, "clclear") { p, _ ->
            p.collectionLog.container.clear()
            p.sendMessage("Collection log cleared.")
        }
    }
}
