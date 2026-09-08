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

class PieShop : ShopScript() {

    init {
        "Pie Shop"(57, ShopCurrency.COINS, STOCK_ONLY) {
            PIE_RECIPE_BOOK(50, -1, 5)
            REDBERRY_PIE(5, -1, 12)
            MEAT_PIE(4, -1, 15)
            MUD_PIE(0, -1, 54)
            APPLE_PIE(3, -1, 30)
            GARDEN_PIE(2, -1, 24)
            FISH_PIE(1, -1, 100)
            ADMIRAL_PIE(0, -1, 310)
            WILD_PIE(0, -1, 182)
            SUMMER_PIE(0, -1, 140)
        }
    }
}
