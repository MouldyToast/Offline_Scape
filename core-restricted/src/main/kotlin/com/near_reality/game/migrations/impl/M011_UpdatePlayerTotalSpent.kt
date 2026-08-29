package com.near_reality.game.migrations.impl

import com.near_reality.api.service.store.StorePlayerHandler
import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.near_reality.game.world.entity.player.totalDonatedAfterLaunch
import com.zenyte.game.GameConstants
import com.zenyte.game.world.entity.player.Player

@ActiveMigration
class M011_UpdatePlayerTotalSpent  : GameMigration {
    override fun run(player: Player) {
        if (!GameConstants.WORLD_PROFILE.isMainDatabaseEnabled()) {
            return
        }
        player.totalDonatedAfterLaunch = StorePlayerHandler.creditPurchasesSinceRelaunch[player.user.id] ?: 0
    }

    override fun id(): Int = 11
}
