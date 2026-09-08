package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*
import org.jesse.game.content.universalshop.*
import org.jesse.game.content.universalshop.UnivShopItem
import org.jesse.game.content.universalshop.UnivShopItem.*

class OreSeller : ShopScript() {

    init {
        "Ore Seller"(189, ShopCurrency.COINS, STOCK_ONLY) {
            COPPER_ORE(1000, 1, 4, 5)
            TIN_ORE(1000, 1, 4, 5)
            IRON_ORE(1000, 6, 25, 5)
            MITHRIL_ORE(1000, 64, 243, 5)
            SILVER_ORE(1000, 30, 112, 5)
            GOLD_ORE(1000, 60, 225, 5)
            COAL(1000, 18, 67, 5)
        }
    }
}
