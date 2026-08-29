package com.near_reality.plugins.interfaces.death

import com.near_reality.game.content.UniversalShop
import com.near_reality.game.content.shop.ShopCurrencyHandler
import com.near_reality.game.content.shop.UniversalShopCategory
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.world.entity.player.selectedUniversalShopCategory
import com.zenyte.game.GameConstants
import com.zenyte.game.content.universalshop.UniversalShopInterface
import com.zenyte.game.item.Item
import com.zenyte.game.util.Colour
import com.zenyte.game.util.RSColour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.dialogue.start
import com.zenyte.plugins.dialogue.OptionsMenuD
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.model.ui.InterfacePosition.*
import com.zenyte.game.GameInterface
import com.zenyte.game.GameInterface.*
import com.zenyte.game.util.AccessMask
import com.zenyte.game.util.AccessMask.*
import mgi.types.config.enums.Enums
import mgi.types.config.enums.Enums.*

class UnivShopInvInterface : InterfaceScript() {

    fun Player.selectItem(option: Int, slotID: Int) {
        if(selectedUniversalShopCategory < 1) {
            sendMessage("You cannot use this while not in a specific shop.")
            return
        }
        val item = inventory.container.items[slotID] ?: return
        val itemId = item.id
        val category = UniversalShop.getCategory(selectedUniversalShopCategory) ?: return
        // Value
        if (option == 1) {
            if(category != UniversalShop.General) {
                val found = category.table.items.firstOrNull { it.id == itemId }
                    ?: run { sendMessage("This item cannot be sold to this shop."); return }
                if (found.sellPrice != -1) {
                    sendMessage("${item.name} can be sold for ${found.sellPrice} ${UniversalShop.getCategoryCurrency(selectedUniversalShopCategory)}"); return
                } else {
                    sendMessage("This item cannot be sold to this shop."); return
                }
            } else {
                if(GameConstants.RESTRICTED_TRADE_ITEMS.contains(item.id) || item.id == 995 || item.id == 13224 || !item.definitions.isGrandExchange) {
                    sendMessage("This item cannot be sold."); return
                }
                val price = (item.definitions.price / 3)
                val message = if(item.amount > 1 || item.isStackable)
                    "${item.name} can be sold for $price each (stack value: ${formatQuantity((item.amount * price).toLong())})"
                else
                    "${item.name} can be sold for $price"
                sendMessage(message)
                return
            }
        }
        // Sell
        else if(option == 2) {
            sendInputInt("How many would you like to sell? (max ${item.amount})") { requested ->
                val value = requested.coerceAtLeast(1).coerceAtMost(item.amount)

                if (category == UniversalShop.General) {
                    val potential = (value * (item.definitions.price / 3)).toLong()

                    if (value > 100_000_000) { sendMessage("You can not sell this many at any given time"); return@sendInputInt }
                    if (!inventory.hasFreeSlots()) { sendMessage("You must have at least one free inventory space to sell here."); return@sendInputInt }

                    if (potential > Int.MAX_VALUE) { sendMessage("You cannot sell this many at once."); return@sendInputInt }
                    else {
                        dialogueManager.start {
                            options("Are you sure you want to sell $value x ${item.name}",
                                Dialogue.DialogueOption("Yes") { attemptSaleGold(player, value, item) },
                                Dialogue.DialogueOption("No") { UniversalShopInterface.openInterfaceToTab(player, selectedUniversalShopCategory) }
                            )
                        }
                    }
                }
                // Selling to a specific store
                else {
                    val found = category.table.items.firstOrNull { it.id == itemId }
                        ?: run { sendMessage("This item cannot be sold to this shop."); return@sendInputInt }
                    if (found.sellPrice != -1) attemptSaleCategory(this, value, item, found, category)
                    else sendMessage("This item cannot be sold to this shop."); return@sendInputInt
                }
            }
        }
    }

    fun attemptSaleCategory(player: Player, requested: Int, item: Item, found: UnivShopItem, category: UniversalShopCategory) {
        val success = player.inventory.deleteItem(Item(item.id, requested)).succeededAmount
        ShopCurrencyHandler.add(UniversalShop.getCategoryCurrency(category.uniqueIndex), player, success * found.sellPrice)
        player.sendMessage(Colour.RS_GREEN.wrap("You have received ${success * found.sellPrice} ${UniversalShop.getCategoryCurrency(category.uniqueIndex)}"))
        UniversalShopInterface.openInterfaceToTab(player, player.selectedUniversalShopCategory)
    }

    fun attemptSaleGold(player: Player, requested: Int, item: Item) {
        val success = player.inventory.deleteItem(Item(item.id, requested)).succeededAmount
        player.inventory.addItem(Item(995, success * (item.definitions.price / 3)))
        player.sendMessage(Colour.RS_GREEN.wrap("You have received ${formatQuantity((success * (item.definitions.price / 3)).toLong())} coins"))
        UniversalShopInterface.openInterfaceToTab(player, player.selectedUniversalShopCategory)
    }

    fun formatQuantity(amount: Long): String {
        var format = "Too high!"
        if (amount in 0..999) {
            format = amount.toString()
        } else if (amount in 1000..999999) {
            format = (amount / 1000).toString() + "K"
        } else if (amount in 1000000..999999999) {
            format = (amount / 1000000).toString() + "M"
        } else if (amount in 1000000000L..999999999999) {
            format = (amount / 1000000000).toString() + "B"
        } else if (amount in 1000000000000L..9999999999999999) {
            format = (amount / 1000000000000L).toString() + "T"
        } else if (amount in 10000000000000000L..999999999999999999) {
            format = (amount / 1000000000000000L).toString() + "QD"
        } else if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
            format = (amount / 1000000000000000000L).toString() + "QT"
        }
        return format
    }

    init {
        UNIVERSAL_SHOP_INVENTORY {
            val itemLayer = "Item layer"(0) {
                player.selectItem(option, slotID)
            }
            opened {
                itemLayer.sendComponentSettings(this, 27, CLICK_OP1, CLICK_OP2, CLICK_OP10)
                packetDispatcher.sendClientScript(
                    149,
                    id shl 16 or itemLayer.componentID,
                    ContainerType.INVENTORY.id,
                    4,
                    7,
                    0,
                    -1,
                    "Value",
                    "Sell",
                    "",
                    "",
                    ""
                )
                sendInterface()
            }
        }
    }
}
