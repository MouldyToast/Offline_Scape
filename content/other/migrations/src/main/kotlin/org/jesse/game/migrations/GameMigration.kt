package org.jesse.game.migrations

import org.jesse.game.world.entity.player.Player

interface GameMigration {
    fun run(player: Player)
    fun id() : Int
}
