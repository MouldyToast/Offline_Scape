package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class TraderSvenSBlackMarketGoods : ShopScript() {

    init {
        "Trader Sven's Black-market Goods"(173, ShopCurrency.COINS, STOCK_ONLY) {
            CITIZEN_TOP(10, 2, 6)
            CITIZEN_TROUSERS(10, 2, 6)
            CITIZEN_SHOES(10, 2, 6)
            VYREWATCH_TOP(10, 260, 650)
            VYREWATCH_LEGS(10, 260, 650)
            VYREWATCH_SHOES(10, 260, 650)
        }
    }
}
