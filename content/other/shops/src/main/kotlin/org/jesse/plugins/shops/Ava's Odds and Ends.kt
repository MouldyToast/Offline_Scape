package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class AvaSOddsAndEnds : ShopScript() {

    init {
        "Ava's Odds and Ends"(148, ShopCurrency.COINS, STOCK_ONLY) {
            FEATHER(1000, 0, 2)
            FEATHER_PACK(100, 80, 260)
            IRON_ARROW(40, 1, 3)
            STEEL_ARROW(10, 5, 16)
            IRON_ARROWTIPS(30, 0, 2)
            STEEL_ARROWTIPS(20, 2, 7)
        }
    }
}
