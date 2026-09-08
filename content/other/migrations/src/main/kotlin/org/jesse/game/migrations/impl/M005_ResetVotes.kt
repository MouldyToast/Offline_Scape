package org.jesse.game.migrations.impl

import org.jesse.api.service.vote.lastVoteClaimTime
import org.jesse.api.service.vote.totalVoteCredits
import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M005_ResetVotes : GameMigration {
    override fun run(player: Player) {
        player.totalVoteCredits = 0;
        player.lastVoteClaimTime = 0L;
    }

    override fun id(): Int = 5
}