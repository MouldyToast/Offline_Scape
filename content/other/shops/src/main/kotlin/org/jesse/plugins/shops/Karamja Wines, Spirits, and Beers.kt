package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class KaramjaWinesSpiritsAndBeers : ShopScript() {

    init {
        "Karamja Wines, Spirits, and Beers"(105, ShopCurrency.COINS, STOCK_ONLY) {
            BEER(3, 1, 2)
            KARAMJAN_RUM(3, 0, 30)
            JUG_OF_WINE(1, 0, 1)
        }
    }
}
