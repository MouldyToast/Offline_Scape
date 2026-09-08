package org.jesse.game.migrations.impl

import org.jesse.api.service.store.StorePlayerHandler
import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.world.entity.player.totalDonatedAfterLaunch
import org.jesse.game.GameConstants
import org.jesse.game.world.entity.player.Player

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
