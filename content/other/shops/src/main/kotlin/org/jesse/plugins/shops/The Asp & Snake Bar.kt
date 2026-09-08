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

class TheAspSnakeBar : ShopScript() {

    init {
        "The Asp & Snake Bar"(138, ShopCurrency.COINS, STOCK_ONLY) {
            BEER(83, 1, 2)
            WHISKY(10, 1, 5)
            JUG_OF_WINE(13, 0, 1)
            VODKA(5, 1, 5)
            BRANDY(4, 1, 5)
            GROG(12, 1, 3)
        }
    }
}
