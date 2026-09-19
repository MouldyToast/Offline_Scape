package org.jesse.game.content.skills.agility.wildernesscourse

import org.jesse.game.content.skills.agility.AbstractAgilityCourse
import org.jesse.game.item.Item
import org.jesse.game.item.ids.BLOOD_MONEY
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.player.Player
import java.util.function.Consumer

class WildernessCourse : AbstractAgilityCourse() {
    private val completeConsumer =
        Consumer { player: Player? -> player!!.getInventory().addItem(Item(BLOOD_MONEY, Utils.random(2, 6))) }

    override fun getAdditionalCompletionXP(): Double = 499.0

    override fun onComplete(): Consumer<Player?> {
        return completeConsumer
    }
}
