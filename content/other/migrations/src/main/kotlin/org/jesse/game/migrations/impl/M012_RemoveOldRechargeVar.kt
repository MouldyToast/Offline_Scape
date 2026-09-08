package org.jesse.game.migrations.impl

import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.world.entity.player.Player

@ActiveMigration
class M012_RemoveOldRechargeVar: GameMigration {
    override fun run(player: Player) {
        player.attributes.remove("box of restoration delay")
    }
    override fun id(): Int = 12
}