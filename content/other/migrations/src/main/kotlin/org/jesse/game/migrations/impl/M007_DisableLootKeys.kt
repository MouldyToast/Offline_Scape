package org.jesse.game.migrations.impl

import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M007_DisableLootKeys : GameMigration {
    override fun run(player: Player) {
        if (player.lootkeySettings != null)
            player.lootkeySettings = null
    }

    override fun id(): Int = 7
}