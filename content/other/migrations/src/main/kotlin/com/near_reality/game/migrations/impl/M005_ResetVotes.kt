package com.near_reality.game.migrations.impl

import com.near_reality.api.service.vote.lastVoteClaimTime
import com.near_reality.api.service.vote.totalVoteCredits
import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.zenyte.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M005_ResetVotes : GameMigration {
    override fun run(player: Player) {
        player.totalVoteCredits = 0;
        player.lastVoteClaimTime = 0L;
    }

    override fun id(): Int = 5
}