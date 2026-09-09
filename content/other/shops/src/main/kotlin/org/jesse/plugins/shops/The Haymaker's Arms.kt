package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class TheHaymakerSArms : ShopScript() {

    init {
        "The Haymaker's Arms"(204, ShopCurrency.COINS, STOCK_ONLY) {
            BEER(10, -1, 2)
            CIDER(10, -1, 2)
            JUG_OF_WINE(5, -1, 1)
            CUP_OF_TEA(5, -1, 10)
        }
    }
}
