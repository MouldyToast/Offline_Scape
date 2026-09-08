package org.jesse.game.migrations.impl

import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M001_RemoveVyreChargesAndRefund : GameMigration {
    override fun run(player: Player) {
        val charges = (player.attributes["vyre well charges"] as Number?)?.toInt() ?: 0
        if (charges == 0) return
        val chargesIssued = minOf(charges, 20)
        player.sendMessage("Due to updates to the Vyre Well, ${chargesIssued * 270} blood runes have been returned to you.")
        player.tryAddInventoryThenBank(Item(BLOOD_RUNE, chargesIssued * 270))
        player.attributes["vyre well charges"] = 0
    }

    override fun id(): Int = 1

}