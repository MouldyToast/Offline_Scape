package org.jesse.game.model.ui.loyaltytitles

import com.google.common.eventbus.Subscribe
import org.jesse.game.GameInterface
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.ui.Interface
import org.jesse.game.util.AccessMask
import org.jesse.game.world.entity.masks.UpdateFlag
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.plugins.events.ServerLaunchEvent
import mgi.types.config.InventoryDefinitions
import mgi.types.config.StructDefinitions
import mgi.types.config.enums.EnumDefinitions
import mgi.types.config.enums.StringEnum
import kotlin.jvm.optionals.getOrNull

/**
 * @author <a href="https://github.com/heavens">mack</a>
 */
class LoyaltyTitleShop : Interface() {

    private val Player.selectedTitleLocked: Boolean get() {
        val selected = getSelectedTitle() ?: return true
        val index = selected.id - NONE_ID
        return loyaltyTitleUnlocks.get(index)?.id == BURNT_BONES
    }

    override fun attach() {
        put(15, "apply")
        put(22, "select")
        put(25, "categories")
    }

    override fun build() {
        bind("select") { player, slotId, _, _ ->
            player.selectTitle(slotId)
        }
        bind("apply") { player ->
            player.applySelectedTitle()
        }
        bind("categories") { player, slotId, _, _ ->
            val index = (slotId - 1) / 2
            val category = loyaltyCategories.getValue(index).getOrNull()
            player.sendMessage("$index - $category")
            if (category == "Locked" || category == "Unlocked") {
                player.loyaltyTitleUnlocks.refresh(player)
            }
            player.packetDispatcher.sendClientScript(45575, index)
        }
    }

    override fun open(player: Player) {
        // Note(Mack): This should be removed and only exists for demonstrative purposes regarding how to transmit
        // locked/unlocked state.

        for (index in 1 until loyaltyTitles.size) {
            val status = player.playerTitleStatus.getOrDefault(index, false)
            if (!status)
                player.setTitleLocked(index)
            else
                player.setTitleUnlocked(index)
        }
        super.open(player)
        player.loyaltyTitleUnlocks.refresh(player)
        player.packetDispatcher.sendComponentSettings(
            id,
            getComponent("categories"),
            0,
            (loyaltyCategories.size * 2) + 1,
            AccessMask.CLICK_OP1
        )
        player.packetDispatcher.sendComponentSettings(
            id,
            getComponent("select"),
            0,
            loyaltyTitles.size * 15,
            AccessMask.CLICK_OP1
        )
    }

    override fun close(player: Player) {
        super.close(player)
        player.temporaryAttributes["selectedTitle"] = null
    }

    private fun Player.selectTitle(slotId: Int) {
        val index = slotId / 15
        val title = loyaltyTitles[index]
        addTemporaryAttribute("selectedTitle", title)
    }

    private fun Player.getSelectedTitle(): StructDefinitions? =
        temporaryAttributes.getOrDefault("selectedTitle", null) as StructDefinitions?

    private fun Player.applySelectedTitle() {
        if (selectedTitleLocked) {
            val req = getUnlockRequirement(getTitleNameByDefinitions(getSelectedTitle()))
            if (req.isNotEmpty() && req.startsWith("Purchasable")) {
                val titleName = getTitleNameByDefinitions(getSelectedTitle())
                if (successfullyPurchasedTitle(this, titleName)) {
                    open(this)
                    return
                }
                // else they cannot afford
                else return
            }
            sendMessage("You have not yet unlocked this title.")
            return
        }
        val title = getSelectedTitle()
        attributes["title"] = title?.id ?: NONE_ID
        updateFlags.flag(UpdateFlag.APPEARANCE)
        packetDispatcher.sendClientScript(830)
    }

    private fun getUnlockRequirement(titleName: String): String {
        val def = getTitleStructDefinition(titleName)
        return def?.parameters?.get(11829) as String
    }

    private fun Player.setTitleLocked(index: Int) {
        loyaltyTitleUnlocks.set(index, Item(BURNT_BONES))
    }

    private fun Player.setTitleUnlocked(index: Int) {
        loyaltyTitleUnlocks.set(index, Item(BONES))
    }

    private fun Player.clearSelection() {
        attributes["title"] = NONE_ID
        updateFlags.flag(UpdateFlag.APPEARANCE)
    }

    override fun getInterface(): GameInterface = GameInterface.LOYALTY_TITLES

    companion object {

        const val NONE_ID: Int = 30000

        private lateinit var loyaltyCategories: StringEnum

        @JvmStatic
        lateinit var loyaltyTitles: List<StructDefinitions>

        @JvmStatic
        @Subscribe
        fun onServerLaunch(ev: ServerLaunchEvent) {
            var id = NONE_ID
            loyaltyTitles = mutableListOf<StructDefinitions>()
            repeat(InventoryDefinitions.get(2500)?.size ?: 0) {
                loyaltyTitles += StructDefinitions.get(id++)
            }
            loyaltyCategories = EnumDefinitions.getStringEnum(23000)!!
            //println("Title categories: ${loyaltyCategories.size}")
            //println("Total titles: ${loyaltyTitles.size}")
        }

        fun unlockTitle(player: Player, titleName: String) {
            val index = getTitleIndexByName(titleName)
            if (index == -1) {
                player.sendMessage("Title not found.")
                return
            }
            if (player.playerTitleStatus.isEmpty()) {
                for (i in 0..loyaltyTitles.size)
                    player.playerTitleStatus[i] = false
            }
            if (player.playerTitleStatus[index] == false) {
                player.playerTitleStatus[index] = true
                player.sendDeveloperMessage("Unlocked title: $titleName")
                player.sendFilteredMessage("Congratulations, you've just unlocked the title: $titleName")
            } else {
                player.sendFilteredMessage("You have already unlocked: $titleName")
            }
        }

        fun hasUnlockedTitle(player: Player, titleName: String): Boolean {
            val index = getTitleIndexByName(titleName)
            if (index == -1) {
                return false
            }
            return player.playerTitleStatus[index] == true
        }

        private fun successfullyPurchasedTitle(player: Player, titleName: String): Boolean {
            val currency = getCurrency(titleName)
            val cost = getTitleCost(titleName)
            if (currency <= 0) {
                logger.error("Title $titleName has no currency set.")
                return false
            }
            if (cost <= 0) {
                logger.error("Title $titleName has no cost set.")
                return false
            }
            val tender = Item(currency, cost)
            //Verify/validate the player has the specific currency & cost in their inventory
            if (!player.inventory.containsItem(tender)) {
                player.sendMessage("You don't have enough ${tender.name} to purchase this title.")
                return false
            }
            //Delete the cost from the players inventory
            if (player.inventory.deleteItem(tender).result == RequestResult.SUCCESS) {
                //Unlock the title
                unlockTitle(player, titleName)
                return true
            }
            return false
        }

        private fun getCurrency(titleName: String): Int {
            val def = getTitleStructDefinition(titleName)
            return def?.parameters?.get(11832) as Int
        }

        private fun getTitleCost(titleName: String): Int {
            val def = getTitleStructDefinition(titleName)
            return def?.parameters?.get(11831) as Int
        }

        private fun getTitleIndexByName(titleName: String): Int {
            loyaltyTitles.forEachIndexed { index, structDefinitions ->
                val cachedTitle = getTitleNameByDefinitions(structDefinitions)
                if (cachedTitle.equals(titleName, ignoreCase = true))
                    return index
            }
            return -1
        }

        private fun getTitleStructDefinition(titleName: String): StructDefinitions? {
            loyaltyTitles.forEachIndexed { _, structDefinitions ->
                val cachedTitle = getTitleNameByDefinitions(structDefinitions)
                if (cachedTitle.equals(titleName, ignoreCase = true))
                    return structDefinitions
            }
            return null
        }

        private fun getTitleNameByDefinitions(def: StructDefinitions?): String {
            def ?: return ""
            for (i in 11825..11827) {
                val param = def.parameters[i] ?: continue
                val paramString = param.toString() // '<col=C86402>Master </col>'
                if (paramString.isEmpty()) continue
                if (paramString.startsWith("<col="))
                    return paramString.stripColourTag()
            }
            return ""
        }

        private fun String.stripColourTag(): String {
            return this.replace("<col=(.*?)>".toRegex(), "")
                .replace("</col>".toRegex(), "")
                .trim()
        }
    }

}
