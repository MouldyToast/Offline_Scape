package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class FineFashions : ShopScript() {

    init {
        "Fine Fashions"(92, ShopCurrency.COINS, STOCK_ONLY) {
            PINK_HAT(5, 88, 160)
            GREEN_HAT(5, 88, 160)
            BLUE_HAT(5, 88, 160)
            CREAM_HAT(5, 88, 160)
            TURQUOISE_HAT(5, 88, 160)
            PINK_ROBE_TOP(5, 99, 180)
            GREEN_ROBE_TOP(5, 99, 180)
            BLUE_ROBE_TOP(5, 99, 180)
            CREAM_ROBE_TOP(5, 99, 180)
            TURQUOISE_ROBE_TOP(5, 99, 180)
            PINK_ROBE_BOTTOMS(5, 99, 180)
            GREEN_ROBE_BOTTOMS(5, 99, 180)
            BLUE_ROBE_BOTTOMS(5, 99, 180)
            CREAM_ROBE_BOTTOMS(5, 99, 180)
            TURQUOISE_ROBE_BOTTOMS(5, 99, 180)
            PINK_BOOTS(5, 110, 200)
            GREEN_BOOTS(5, 110, 200)
            BLUE_BOOTS(5, 110, 200)
            CREAM_BOOTS(5, 110, 200)
            TURQUOISE_BOOTS(5, 110, 200)
        }
    }
}
