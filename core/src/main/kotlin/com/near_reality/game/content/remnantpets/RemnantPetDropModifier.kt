package com.near_reality.game.content.remnantpets

import com.zenyte.game.content.skills.magic.spells.arceuus.Reanimation
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Skills
import kotlin.math.floor

object RemnantPetDropModifier {
    @JvmStatic
    fun process(player: Player, item: Item): Boolean {
        val itemName = item.name
        if(itemName.contains(" seed") || itemName.contains("Grimy "))
            modifySeedOrHerbDrop(player, item)

        if(item.isNotedItem && item.isSupplies())
            modifyNotedSuppliesDrop(player, item)

        if(item.isEnsouledHead())
            checkConsumeEnsouledHead(player, item)

        if(item.isBloodMoney())
            updateBloodMoneyQuantity(player, item)

        return false
    }

    private fun updateBloodMoneyQuantity(player: Player, item: Item) {
        val originalAmt = item.amount
        item.amount = player.remnantPetManager.modifyBloodMoneyDrop(originalAmt)
    }

    private fun modifySeedOrHerbDrop(player: Player, item: Item) {
        val manager = player.remnantPetManager
        if(manager.doubleHerbAndSeedDrops())
            item.amount *= 2
    }

    private fun modifyNotedSuppliesDrop(player: Player, item: Item) {
        val manager = player.remnantPetManager
        val modificationFactor = manager.getNotedSuppliesModFactor()
        if(modificationFactor != 1.0) {
            player.sendDeveloperMessage("Modified noted drops by: $modificationFactor")
            val newQuantity = floor(((item.amount).toDouble() * modificationFactor)).toInt()
            item.amount = newQuantity
        }
    }

    private fun checkConsumeEnsouledHead(player: Player, item: Item) {
        val manager = player.remnantPetManager
        if(manager.checkEnsouledHeadConsume()) {
            val ensouled = Reanimation.forItemId(item.unnotedId)
            if (ensouled.isPresent) {
                player.sendFilteredMessage("Your pet consumes the ensouled head for prayer xp")
                val quantity = item.amount
                player.skills.addXp(Skills.PRAYER, ensouled.get().prayerExperience * quantity)
                item.amount = 0
            }
        }

    }
}