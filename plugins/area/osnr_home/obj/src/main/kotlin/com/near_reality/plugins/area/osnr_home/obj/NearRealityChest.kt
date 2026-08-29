package com.near_reality.plugins.area.osnr_home.obj

import com.near_reality.game.content.elven.obj.NewCrystalChestLoot
import com.near_reality.game.item.CustomObjectId
import com.zenyte.game.content.boons.impl.Locksmith
import com.zenyte.game.content.kebos.konar.plugins.objects.BrimstoneChest
import com.zenyte.game.content.kebos.konar.plugins.objects.BrimstoneChest.ChestReward
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Analytics
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin
import com.zenyte.plugins.dialogue.SkillDialogue
import mgi.utilities.StringFormatUtil
import kotlin.math.min

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-14
 */
class NearRealityChest: ObjectAction {

    private val enhancedCrystalKey = Item(ItemId.ENHANCED_CRYSTAL_KEY)
    private val crystalKey = Item(ItemId.CRYSTAL_KEY)
    private val brimstoneKey = Item(ItemId.BRIMSTONE_KEY)


    private fun Player.sendMessageMissingKeys() =
        sendMessage("You don't appear to have any keys on you.")

    private fun Player.getKeys(): Array<Item> {
        val keys = ArrayList<Item>()
        if (inventory.containsItem(enhancedCrystalKey))
            keys.add(enhancedCrystalKey)
        if (inventory.containsItem(crystalKey))
            keys.add(crystalKey)
        if (inventory.containsItem(brimstoneKey))
            keys.add(brimstoneKey)

        return keys.toTypedArray()
    }

    private fun Player.openNearRealityChest(key: Item) {
        animation = Animation(832)
        lock(2)
        if (boonManager.hasBoon(Locksmith::class.java) && Locksmith.roll()) {
            sendFilteredMessage(Colour.DARK_BLUE.wrap("Your Locksmith perk saves your key from being consumed."))
            openChest(key)
        }
        else if (inventory.deleteItem(key).result == RequestResult.SUCCESS) {
            sendFilteredMessage(Colour.RED.wrap("Your key was consumed upon unlocking the chest."))
            openChest(key)
        }
    }

    private fun Player.openChest(key: Item) {
        if (key.id == enhancedCrystalKey.id)
            openEnhancedCrystalChest()
        if (key.id == crystalKey.id)
            openCrystalChest()
        if (key.id == brimstoneKey.id)
            openBrimstoneChest()
    }

    private fun Player.openEnhancedCrystalChest() {
        variables.timesOpenedEnhancedCrystalChest++
        if (memberRank.equalToOrGreaterThan(MemberRank.SAPPHIRE) && Utils.random(getChance()) == 0) {
            sendMessage(Colour.RS_GREEN.wrap("You find double the loot from the crystal chest."))
            NewCrystalChestLoot.rollTable(this, true).forEach(inventory::addOrDrop)
        }
        NewCrystalChestLoot.rollTable(this, true).forEach(inventory::addOrDrop)
        inventory.addOrDrop(Item(995, Utils.random(25_000, 100_000)))
        val bonusRolls = getLootRollsForRank()
        if (bonusRolls > 0)
            repeat(bonusRolls) { NewCrystalChestLoot.rollTable(this, false).forEach(inventory::addOrDrop) }
        Analytics.flagInteraction(this, Analytics.InteractionType.NEAR_REALITY_CHEST)
    }

    private fun Player.openCrystalChest() {
        if (memberRank.equalToOrGreaterThan(MemberRank.SAPPHIRE) && Utils.random(getChance()) == 0) {
            sendMessage(Colour.RS_GREEN.wrap("You find double the loot from the crystal chest."))
            NewCrystalChestLoot.rollTable(this, false).forEach(inventory::addOrDrop)
        }
        NewCrystalChestLoot.rollTable(this, false).forEach(inventory::addOrDrop)
        val bonusRolls = getLootRollsForRank()
        if (bonusRolls > 0)
            repeat(bonusRolls) { NewCrystalChestLoot.rollTable(this, false).forEach(inventory::addOrDrop) }
        Analytics.flagInteraction(this, Analytics.InteractionType.NEAR_REALITY_CHEST)
    }

    private fun Player.getLootRollsForRank(): Int {
        return when(memberRank) {
            MemberRank.DRAGONSTONE,
            MemberRank.ONYX,
            MemberRank.ZENYTE,
            MemberRank.DIAMOND -> 1

            MemberRank.ENCHANTED,
            MemberRank.GOLD,
            MemberRank.ETERNAL,
            MemberRank.NEBULA -> 2

            MemberRank.CATALYTIC -> 3
            else -> 0
        }
    }

    private fun Player.openBrimstoneChest() {
        ChestReward.randomReward(this).ifPresent { reward: Item ->
            val price = reward.sellPrice * reward.amount
            inventory.addOrDrop(reward)
            sendMessage("You find some treasure in the chest!")
            sendMessage(Colour.RED.wrap("Valuable drop: ${reward.amount} x ${reward.name} (${StringFormatUtil.format(price)} coins)"))
            LootBroadcastPlugin.fireEvent(name, reward, location, false, false)
            addAttribute("brimstone_chest_open_count", getNumericAttribute("brimstone_chest_open_count").toInt() + 1)
            inventory.addOrDrop(Item(995, Utils.random(25_000, 150_000)))
            BrimstoneChest.sendOpenedCount(this)
            collectionLog.add(reward)
        }
    }

    private fun Player.getChance(): Int {
        return when {
            memberRank.equalToOrGreaterThan(MemberRank.ONYX) -> 3
            memberRank.equalToOrGreaterThan(MemberRank.DRAGONSTONE) -> 3
            memberRank.equalToOrGreaterThan(MemberRank.DIAMOND) -> 4
            memberRank.equalToOrGreaterThan(MemberRank.RUBY) -> 6
            memberRank.equalToOrGreaterThan(MemberRank.EMERALD) -> 6
            memberRank.equalToOrGreaterThan(MemberRank.SAPPHIRE) -> 9
            else -> 9
        }
    }

    private fun Player.startChestDialogue(keys: Array<Item>) {
        dialogueManager.start(object: SkillDialogue(this, "Which key will you use?", *keys) {
            override fun run(slotId: Int, amount: Int) {
                val key = keys[slotId]
                val amountToMake = min(amount, inventory.getAmountOf(key.id))
                repeat(amountToMake) { openNearRealityChest(key) }
            }
        })
    }


    override fun handleObjectAction(player: Player?, `object`: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return
        val keys = player.getKeys()
        if (keys.isEmpty()) {
            player.sendMessageMissingKeys()
            return
        }
        player.startChestDialogue(keys)
    }

    override fun getObjects(): Array<Any> = arrayOf(CustomObjectId.NEAR_REALITY_CHEST)
}