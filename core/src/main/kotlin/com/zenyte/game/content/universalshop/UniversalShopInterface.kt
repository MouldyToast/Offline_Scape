package com.zenyte.game.content.universalshop

import com.near_reality.api.service.vote.totalVoteCredits
import com.near_reality.game.content.UniversalShop
import com.near_reality.game.content.universalshop.UniversalShopTable
import com.near_reality.game.world.entity.player.bountyHunterPoints
import com.near_reality.game.world.entity.player.selectedUniversalShopCategory
import com.near_reality.game.world.entity.player.univShopDoubleProcess
import com.near_reality.game.world.entity.player.univShopSearchActive
import com.zenyte.GameToggles.UNIVERSAL_SHOP_FLOODGATE
import com.zenyte.game.GameInterface
import com.zenyte.game.item.Item
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.Colour
import com.zenyte.game.util.ItemUtil
import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.Plugin
import mgi.types.config.DBRowDefinition
import java.util.*

@Suppress("unused")
class UniversalShopInterface : Interface(), Plugin {
    companion object{
        @JvmStatic fun openInterfaceToTab(player: Player, tab: Int) {
            // check if we require a PIN to be "Unlocked"
            if (player.bankPin.requiresVerification(player) { openInterfaceToTab(player, tab) }) return

            if(!UNIVERSAL_SHOP_FLOODGATE) {
                player.sendMessage("Universal Shop has been toggled off. Please check discord for updates.")
                return
            }
            if (player.isInDonorIsland())
                player.openUniversalShop()
            if (player.location.regionId != 12342) {
                player.sendMessage("You can only open this at Home")
                return;
            }
            player.openUniversalShop()
        }

        private fun Player.isInDonorIsland(): Boolean {
            return getBooleanAttribute("isInWesternQuadrant") ||
                    getBooleanAttribute("isInEasternQuadrant") ||
                    getBooleanAttribute("isInNorthernQuadrant") ||
                    getBooleanAttribute("isInSouthernQuadrant")
        }

        private fun Player.openUniversalShop() {
            GameInterface.UNIVERSAL_SHOP.open(this)
            this.varManager.sendVarInstantLowPri(9998, 0)
            this.selectedUniversalShopCategory = 0
        }
    }

    override fun close(player: Player?, replacement: Optional<GameInterface>?) {
        super.close(player, replacement)
        player?.dialogueManager?.finish()
    }
    override fun open(player: Player) {
        player.univShopSearchActive = false
        player.univShopDoubleProcess = false
        GameInterface.UNIVERSAL_SHOP_INVENTORY.open(player)
        super.open(player)

        val totalItemCount = UniversalShopTable.tables.sumOf { it.items.size } + 100

        player.packetDispatcher.sendComponentSettings(
            getInterface(),
            getComponent("stock_list"),
            0,
            totalItemCount,
            AccessMask.CLICK_OP1,
            AccessMask.CLICK_OP2,
            AccessMask.CLICK_OP3,
            AccessMask.CLICK_OP4,
            AccessMask.CLICK_OP5,
            AccessMask.CLICK_OP6,
            AccessMask.CLICK_OP10,
        )
        player.packetDispatcher.sendComponentSettings(
            getInterface(),
            getComponent("categories_list"),
            0,
            53,
            AccessMask.CLICK_OP1
        )
        player.packetDispatcher.sendComponentSettings(
            getInterface(),
            getComponent("search_button"),
            -1,
            -1,
            AccessMask.CLICK_OP1
        )
    }

    override fun attach() {
        UniversalShop.populateCategories()
        put(3, "search_button")
        put(7, "categories_list")
        put(12, "stock_list")
    }

        override fun build() {
            bind("search_button") { player: Player, _: Int, _: Int, _: Int ->
                if(!player.univShopDoubleProcess) {
                    player.univShopSearchActive = !player.univShopSearchActive
                    player.univShopDoubleProcess = true
                } else {
                    player.univShopDoubleProcess = false
                }
            }
            bind("categories_list") { player: Player, slotId: Int, _: Int, _: Int ->
                player.selectedUniversalShopCategory = ((slotId - 1) / 4)
                player.univShopSearchActive = false
                player.varManager.sendVarInstant(9998, player.selectedUniversalShopCategory)
            }
            bind("stock_list") { player: Player, slotId: Int, interfaceItem: Int, option: Int ->
                val trueTarget = if(player.selectedUniversalShopCategory > 1) slotId - 4 else slotId
                val categoryRow =
                    if(player.univShopSearchActive) UniversalShop.defaultBuyLocation(interfaceItem)
                    else UniversalShop.getCategoryRowIndexPair(player.selectedUniversalShopCategory, trueTarget)

                val categoryId = categoryRow.first
                val itemId = DBRowDefinition.getRowColumnByIndexesInt(UniversalShop.categoriesToIds.getOrDefault(categoryId, 1001), categoryRow.second, 3)

                if (option == 10) {
                    ItemUtil.sendItemExamine(player, Item(itemId))
                }

                if(categoryId == 100) {
                    player.sendMessage("You cannot interact with items from this shop while searching.")
                    return@bind
                }


                when (categoryRow.first) {
                    99 -> {}
                    in 1..13 -> {
                        val price = UniversalShop.determinePrice(categoryRow.first, categoryRow.second)
                        if (option == 1) {
                            val item = Item(itemId)
                            player.sendMessage("A ${item.name} costs $price ${UniversalShop.getCategoryCurrency(categoryId)}${appendCategoryCurrency(player, categoryId)}")
                        }
                        else if (option != 4 && option != 5) {
                            UniversalShop.attemptPurchaseMenu(player, option, categoryId, itemId, price)
                        } else {
                            player.sendInputInt("How many would you like to buy?") {
                                UniversalShop.attemptPurchaseDialogue(player, it, categoryId, itemId, price, option == 5)
                            }
                        }
                    }
                }
            }

        }

    private fun appendCategoryCurrency(player: Player, categoryId: Int): String {
        return when(categoryId) {
            8 -> " (You have ${Colour.RED.wrap(player.slayer.slayerPoints)})"
            9 -> " (You have ${Colour.RED.wrap(player.bountyHunterPoints)})"
            12 -> " (You have ${Colour.RED.wrap(player.loyaltyManager.loyaltyPoints)})"
            13 -> " (You have ${Colour.RED.wrap(player.totalVoteCredits)})"
            else -> ""
        }
    }

    override fun getInterface(): GameInterface {
            return GameInterface.UNIVERSAL_SHOP
        }
}