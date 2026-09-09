package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class CandleShop : ShopScript() {

    init {
        "Candle Shop"(72, ShopCurrency.COINS, STOCK_ONLY) {
            CANDLE(5, 1, 3)
        }
    }
}
