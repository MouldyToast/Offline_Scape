package com.near_reality.game.migrations.impl

import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.zenyte.game.content.lootkeys.lootkeySettings
import com.zenyte.game.content.lootkeys.setLootkeySettings
import com.zenyte.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M007_DisableLootKeys : GameMigration {
    override fun run(player: Player) {
        if (player.lootkeySettings() != null)
            player.setLootkeySettings(null)
    }

    override fun id(): Int = 7
}