package com.near_reality.game.migrations.impl

import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.zenyte.game.world.entity.player.Player

@ActiveMigration
class M010_ResetCollectionLogClaims : GameMigration {
    override fun run(player: Player) {
        player.collectionLogRewardManager.reset()
    }

    override fun id(): Int = 10
}