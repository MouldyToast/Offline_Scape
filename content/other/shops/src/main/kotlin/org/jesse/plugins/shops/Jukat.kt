package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class Jukat : ShopScript() {

    init {
        "Jukat"(ShopCurrency.COINS, STOCK_ONLY) {
            DRAGON_LONGSWORD(2, 60000, 100000)
            DRAGON_DAGGER(2, 18000, 30000)
        }
    }
}
