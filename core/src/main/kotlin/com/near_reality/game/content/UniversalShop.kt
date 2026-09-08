package com.near_reality.game.content

import com.near_reality.game.content.shop.ShopCurrencyHandler
import com.near_reality.game.content.shop.UniversalShopCategory
import com.near_reality.game.content.universalshop.UniversalShopTable
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.game.GameConstants
import com.zenyte.game.content.util.playerHasKilledAllNormalBossesOnce
import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.item._Item
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.world.entity.player.Player
import mgi.types.config.DBRowDefinition
import mgi.types.config.items.ItemDefinitions

object UniversalShop {
    @JvmStatic val priceOverrides = mutableMapOf<Pair<Int, Int>, Int>()
    @JvmStatic val disabledItems = mutableListOf<Int>()
    @JvmStatic val disabledCategory = mutableListOf<UniversalShopCategory>()
    private var cachedMappings = mutableMapOf<Int, Pair<Int, Int>>()


    val Melee = UniversalShopCategory(1, "universal_shop_tbl", 1001, 5, table = UniversalShopTable.Melee)
    val Ranged = UniversalShopCategory(2, "universal_shop_ranged", 1008, 9, table = UniversalShopTable.Ranged)
    val Magic = UniversalShopCategory(3, "universal_shop_magic", 1007, 13, table = UniversalShopTable.Magic)
    val Supplies = UniversalShopCategory(4, "universal_shop_supplies", 1011, 17, table = UniversalShopTable.Supplies)
    val Skilling = UniversalShopCategory(5, "universal_shop_skilling", 1010, 21, table = UniversalShopTable.Skilling)
    val Jewelry = UniversalShopCategory(6, "universal_shop_jewelry", 1012, 25, table = UniversalShopTable.Jewelry)
    val General = UniversalShopCategory(7, "universal_shop_general", 1003, 29, table = UniversalShopTable.General)
    val Slayer = UniversalShopCategory(8, "universal_shop_slayer", 1004, 33, table = UniversalShopTable.Slayer)
    val BountyHunter = UniversalShopCategory(9, "universal_shop_bounty_hunter", 1005, 37, table = UniversalShopTable.BountyHunter)
    val Capes = UniversalShopCategory(10, "universal_shop_capes", 1006, 41, table = UniversalShopTable.Capes)
    val BloodMoney = UniversalShopCategory(11, "universal_shop_bloodmoney", 1009, 45, table = UniversalShopTable.BloodMoney)
    val Loyalty = UniversalShopCategory(12, "universal_shop_loyalty", 1013,  49, table = UniversalShopTable.Loyalty)
    val Vote = UniversalShopCategory(13, "universal_shop_vote", 1014, 53, table = UniversalShopTable.Vote)


    val Categories = listOf(
        Melee,
        Ranged,
        Magic,
        Supplies,
        Skilling,
        Jewelry,
        General,
        Slayer,
        BountyHunter,
        Capes,
        BloodMoney,
        Loyalty,
        Vote
    )

    val categoriesToIds = mapOf(
        1 to 1001, //Melee
        2 to 1008, //Ranged
        3 to 1007, //Magic
        4 to 1011, //Supplies
        5 to 1010, //Skilling
        6 to 1012, //Jewelry
        7 to 1003, //General
        8 to 1004, //Slayer
        9 to 1005, //BountyHunter
        10 to 1006, //Capes
        11 to 1009, //Blood money
        12 to 1013, //Loyalty
        13 to 1014, //Vote
    )

    val categoriesToTable = mapOf(
        1 to Melee, //Melee
        2 to Ranged, //Ranged
        3 to Magic, //Magic
        4 to Supplies, //Supplies
        5 to Skilling, //Skilling
        6 to Jewelry, //Jewelry
        7 to General, //General
        8 to Slayer, //Slayer
        9 to BountyHunter, //BountyHunter
        10 to Capes, //Capes
        11 to BloodMoney, //Blood money
        12 to Loyalty, //Loyalty
        13 to Vote, //Vote
    )

    private fun shopIdToTableId(idx: Int): Int {
        return Categories.find { it.uniqueIndex == idx }?.tableId ?: 1001
    }

    fun getCategory(idx: Int) : UniversalShopCategory? {
        return categoriesToTable[idx]
    }

    fun getCategoryRowIndexPair(selectedCategory: Int, targetChildId: Int): Pair<Int, Int> {
        if (selectedCategory != 0 && selectedCategory != 1) {
            return selectedCategory to targetChildId
        }
        val category = Categories.first { it.componentIds.contains(targetChildId) }
        val rowIndex = targetChildId - category.componentIds.first
        return category.uniqueIndex to rowIndex
    }

    fun attemptPurchaseMenu(player: Player, opIndex: Int, categoryId: Int, itemId: Int, price: Int) {
        if(disabledCategory.contains(categoriesToTable[categoryId])) {
            player.sendMessage("This shop is currently disabled. Please try again later.")
            return
        }
        val table = UniversalShopTable.tables.first { it.category.index == categoryId }
        if(table.items.first { it.id == itemId }.ironmanRestricted && player.isIronman) {
            player.sendMessage("You cannot buy this item as an Ironman.")
            return
        }
        if(opIndex == 10) //Examine
            return
        val currency: ShopCurrency = getCategoryCurrency(categoryId)
        val availableCurrency = ShopCurrencyHandler.getAmount(currency, player)
        val quantity = when(opIndex) {
            2 -> 1
            3 -> 10
            6 -> 50
            else -> 1
        }
        val fullPrice: Long = price.toLong() * quantity
        val debit: Int
        if(fullPrice > Int.MAX_VALUE) {
            player.sendMessage("You cannot purchase that many of this item at once.")
            return
        } else {
            debit = fullPrice.toInt()
        }

        handlePurchase(player, debit, availableCurrency, itemId, quantity, price, currency)
    }

    fun attemptPurchaseDialogue(player: Player, quantity: Int, categoryId: Int, itemId: Int, price: Int, isNote: Boolean) {
        if(disabledCategory.contains(categoriesToTable[categoryId])) {
            player.sendMessage("This shop is currently disabled. Please try again later.")
            return
        }
        val table = UniversalShopTable.tables.first { it.category.index == categoryId }
        if(table.items.first { it.id == itemId }.ironmanRestricted && player.isIronman) {
            player.sendMessage("You cannot buy this item as an Ironman.")
            return
        }
        val currency: ShopCurrency = getCategoryCurrency(categoryId)
        val availableCurrency = ShopCurrencyHandler.getAmount(currency, player)

        val fullPrice: Long = price.toLong() * quantity
        val debit: Int

        if(fullPrice > Int.MAX_VALUE || (price >= 1_000_000 && quantity >= 1_000)) {
            player.sendMessage("You cannot purchase that many of this item at once.")
            return
        } else {
            debit = fullPrice.toInt()
        }

        handlePurchase(player, debit, availableCurrency, itemId, quantity, price, currency, isNote)
    }

    private fun handlePurchase(
        player: Player,
        debit: Int,
        availableCurrency: Int,
        itemId: Int,
        quantity: Int,
        price: Int,
        currency: ShopCurrency,
        isNote: Boolean = false
    ) {
        if(!meetsPurchaseRequirements(player, itemId)) return

        var finalQuantity = quantity

        if (itemId == 2 && currency == ShopCurrency.SLAYER_POINTS) {
            finalQuantity *= 10
        }

        if (debit > availableCurrency) {
            player.sendMessage("You do not have enough $currency to buy that many.")
            return
        } else if (isNote) {
            performTransaction(currency, player, debit, Item.notedId(itemId), finalQuantity, price)
        } else if (ItemDefinitions.getOrThrow(itemId).isStackable()) {
            performTransaction(currency, player, debit, itemId, finalQuantity, price)
        } else if (player.inventory.checkSpace(finalQuantity)) {
            performTransaction(currency, player, debit, itemId, finalQuantity, price)
        } else {
            val newQuantity = player.inventory.freeSlots
            val newDebit = price * newQuantity
            performTransaction(currency, player, newDebit, itemId, newQuantity, price)
        }
    }

    private fun meetsPurchaseRequirements(player: Player, itemId: Int) : Boolean {
        if (disabledItems.contains(itemId) || Item.itemBlacklist.contains(itemId)) {
            player.sendMessage("This item is currently disabled. Please check discord for updates.")
            return false
        }
        if (itemId == RING_OF_SHADOWS_UNCHARGED && !player.playerHasKilledAllNormalBossesOnce()) {
            player.sendMessage("You need to kill all normal DT2 bosses once, before you can purchase this item.")
            return false
        }
        return true
    }

    private fun performTransaction(currency: ShopCurrency, player: Player, debit: Int, itemId: Int, quantity: Int, price: Int) {
        ShopCurrencyHandler.remove(currency, player, debit)
        player.inventory.addItem(itemId, quantity)
        checkAndLog(currency, player, debit, itemId, quantity, price)
        player.sendMessage("You purchase a total of $quantity items for $debit $currency")
    }

    private fun checkAndLog(currency: ShopCurrency, player: Player, debit: Int, itemId: Int, quantity: Int, price: Int) {
        var shouldLogToDatabase = false

        if(currency != ShopCurrency.COINS)
            shouldLogToDatabase = true
        else if(debit > 100_000)
            shouldLogToDatabase = true

        if(shouldLogToDatabase) {
            if (GameConstants.WORLD_PROFILE.isLogsDatabaseEnabled())
                GameLogger.log {
                    GameLogMessage.ShopTransaction.Purchase(
                        username = player.dbUsername,
                        shopName = "Universal Shop",
                        item = _Item(itemId, quantity),
                        currencyName = currency.toString(),
                        priceEach = price
                    )
                }
        }
    }

    fun getCategoryCurrency(categoryId: Int): ShopCurrency {
        return when (categoryId) {
            1, 2, 3, 4, 5, 6, 7, 10 -> ShopCurrency.COINS
            8 -> ShopCurrency.SLAYER_POINTS
            9 -> ShopCurrency.BH_POINTS
            11 -> ShopCurrency.BLOOD_MONEY
            12 -> ShopCurrency.LOYALTY_POINTS
            13 -> ShopCurrency.VOTE_POINTS
            else -> ShopCurrency.COINS
        }
    }


    fun defaultBuyLocation(itemId: Int): Pair<Int, Int> {
//        if(cachedMappings.contains(itemId))
//            return cachedMappings[itemId]!!
        val cat = Categories.find { DBRowDefinition.findItemOnTable(it.tableId, itemId) } ?:return 99 to 1
        return cat.uniqueIndex to DBRowDefinition.getRowIndex(cat.tableId, itemId)
    //        return when(cat.uniqueIndex) {
//            8,9,11,12,13 -> 100 to -1
//            else -> cat.uniqueIndex to DBRowDefinition.getRowIndex(cat.tableId, itemId)
//        }//.also { cachedMappings[itemId] = it }
    }

    fun determinePrice(category: Int, item: Int): Int {
        if(priceOverrides.containsKey(category to item) && priceOverrides[category to item] != null) return priceOverrides[category to item]!!
        return DBRowDefinition.getRowColumnByIndexesInt(shopIdToTableId(category), item, 4)
    }

    fun populateCategories() {
        Categories.forEach { tbl -> tbl.itemCount = DBRowDefinition.getRowCount(tbl.tableId) }
    }
}
