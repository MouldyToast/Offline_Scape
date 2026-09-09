package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class FortunatoSFineWine : ShopScript() {

    init {
        "Fortunato's Fine Wine"(151, ShopCurrency.COINS, STOCK_ONLY) {
            JUG_OF_WINE(50, 0, 1)
            JUG(3, 0, 1)
            EMPTY_JUG_PACK(5, -1, 140)
            BOTTLE_OF_WINE(2, 300, 500)
            JUG_OF_VINEGAR(500, -1, 1)
        }
    }
}
