package org.jesse.game.migrations.impl

import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.Player
import mgi.types.config.items.ItemDefinitions

@Suppress("unused", "ClassName")
@ActiveMigration
class M006_RefundPvpWeapons : GameMigration {

    private val weapons = intArrayOf(22613, 22622, 22610, 22647)

    override fun run(player: Player) {
        if (player.containsAny(*weapons)) {
            weapons.forEach { weapon ->
                if (player.containsItem(weapon)) {
                    val totalRemoved = player.forcedRemoved(Item(weapon))
                    val initialRefund = weapon.getRefund()
                    val refundAmount = initialRefund.amount * totalRemoved
                    if (refundAmount > 0) {
                        val weaponDef = ItemDefinitions.get(weapon)
                        val refund = initialRefund.copy(refundAmount.toInt())
                        player.bank.add(refund)
                        player.sendMessage("Your ${weaponDef.name} was removed from your account.")
                        player.sendMessage("You have been refunded $refundAmount blood money.")
                    }
                }
            }
        }
    }

    private fun Int.getRefund(): Item =
        if (this == VESTAS_LONGSWORD) Item(BLOOD_MONEY, 16_000)
        else Item(BLOOD_MONEY, 10_000)

    override fun id(): Int = 6
}