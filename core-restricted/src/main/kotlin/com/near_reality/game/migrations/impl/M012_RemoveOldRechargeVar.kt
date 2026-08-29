package com.near_reality.game.migrations.impl

import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.zenyte.game.world.entity.player.Player

@ActiveMigration
class M012_RemoveOldRechargeVar: GameMigration {
    override fun run(player: Player) {
        player.attributes.remove("box of restoration delay")
    }
    override fun id(): Int = 12
}