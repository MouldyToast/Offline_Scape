package org.jesse.game.world.entity.player

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.area.wilderness.WildernessResourceArea
import java.util.*

fun determineGatheringMultiplier(player: Player): OptionalDouble {
    var transformedAmount = 0.0
    if (player.inArea(WildernessResourceArea::class.java))
        transformedAmount += WildernessResourceArea.GATHER_QUANTITY_MULTIPLIER
    return if (transformedAmount <= 0.0)
        OptionalDouble.empty()
    else
        OptionalDouble.of(transformedAmount)
}

fun onGather(player: Player) {
    if (player.inArea(WildernessResourceArea::class.java)) {
        if (Utils.randomBoolean(WildernessResourceArea.BLOOD_MONEY_REWARD_CHANCE)) {
            val bloodMoneyAmount = (WildernessResourceArea.BLOOD_MONEY_REWARD_AMOUNT_MIN..WildernessResourceArea.BLOOD_MONEY_REWARD_AMOUNT_MAX).random()
            player.inventory.addOrDrop(Item(BLOOD_MONEY, bloodMoneyAmount))
            player.sendMessage("You receive $bloodMoneyAmount blood money.")
        }
    }
}
