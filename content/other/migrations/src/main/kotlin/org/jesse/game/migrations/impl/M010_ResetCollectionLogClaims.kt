package org.jesse.game.migrations.impl

import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.world.entity.player.Player

@ActiveMigration
class M010_ResetCollectionLogClaims : GameMigration {
    override fun run(player: Player) {
        player.collectionLogRewardManager.reset()
    }

    override fun id(): Int = 10
}