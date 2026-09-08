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

class SmithingSmithSShop : ShopScript() {

    init {
        "Smithing Smith's Shop"(230, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_SCIMITAR(5, 10, 32)
            IRON_SCIMITAR(3, 37, 112)
            STEEL_SCIMITAR(2, 133, 400)
            MITHRIL_SCIMITAR(1, 346, 1040)
            HAMMER(5, 0, 1)
        }
    }
}
