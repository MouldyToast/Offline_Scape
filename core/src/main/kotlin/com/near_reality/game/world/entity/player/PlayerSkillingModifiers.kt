package com.near_reality.game.world.entity.player

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.area.wilderness.WildernessResourceArea
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
            player.inventory.addOrDrop(Item(ItemId.BLOOD_MONEY, bloodMoneyAmount))
            player.sendMessage("You receive $bloodMoneyAmount blood money.")
        }
    }
    val chance: Int = Utils.random(250)
    if (Utils.random(chance) == 1) {
        val artifactRoll: Int = Utils.random(100)
        val quantity = 1
        if(artifactRoll <= 66) {
            player.inventory.addOrDrop(Item(ItemId.REMNANT_WHEEL, quantity))
            player.sendMessage(Colour.RS_GREEN.wrap("You find a remnant wheel among the supplies you gathered."))
        } else if (artifactRoll <= 98) {
            player.inventory.addOrDrop(Item(ItemId.REMNANT_COG, quantity))
            player.sendMessage(Colour.RS_GREEN.wrap("You find a remnant cog among the supplies you gathered."))
        } else if (artifactRoll == 99) {
            player.inventory.addOrDrop(Item(ItemId.REMNANT_TOOLS, quantity))
            player.sendMessage(Colour.RS_GREEN.wrap("You find some remnant tools among the supplies you gathered!"))
        }
    }
}

fun onBurn(player: Player) {
    val chance: Int = Utils.random(400)
    if (Utils.random(chance) == 1) {
        val artifactRoll: Int = Utils.random(100)
        val quantity = 1
        if(artifactRoll <= 66) {
            player.inventory.addOrDrop(Item(ItemId.REMNANT_WHEEL, quantity))
            player.sendMessage(Colour.RS_GREEN.wrap("You find a remnant wheel among the logs you just burned."))
        } else if (artifactRoll <= 98) {
            player.inventory.addOrDrop(Item(ItemId.REMNANT_COG, quantity))
            player.sendMessage(Colour.RS_GREEN.wrap("You find a remnant cog among the logs you just burned."))
        } else if (artifactRoll == 99) {
            player.inventory.addOrDrop(Item(ItemId.REMNANT_TOOLS, quantity))
            player.sendMessage(Colour.RS_GREEN.wrap("You find some remnant tools among the logs you just burned!"))
        }
    }
}
