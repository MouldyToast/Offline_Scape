package org.jesse.game.migrations.impl

import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M004_FixUnlimitedTentacleWhips : GameMigration {
    override fun run(player: Player) {
        for (item in player.inventory.container.items.values) {
            if(item.id == ABYSSAL_TENTACLE && item.charges == 0) {
                item.charges = 10_000
            }
        }

        for (item in player.equipment.container.items.values) {
            if(item.id == ABYSSAL_TENTACLE && item.charges == 0) {
                item.charges = 10_000
            }
        }

        for (item in player.bank.container.items.values) {
            if(item.id == ABYSSAL_TENTACLE && item.charges == 0) {
                item.charges = 10_000
            }
        }
    }

    override fun id(): Int = 4
}