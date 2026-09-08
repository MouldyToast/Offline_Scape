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

class OreStore : ShopScript() {

    init {
        "Ore store"(39, ShopCurrency.COINS, STOCK_ONLY) {
            COPPER_ORE(20, 2, 3)
            TIN_ORE(10, 2, 3)
            IRON_ORE(10, 11, 18)
            SILVER_ORE(5, 52, 82)
            COAL(10, 31, 49)
            GOLD_ORE(5, 105, 165)
            MITHRIL_ORE(0, 113, 178)
            ADAMANTITE_ORE(0, 280, 440)
        }
    }
}
