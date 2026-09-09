package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class FossilIslandGeneralStore : ShopScript() {

    init {
        "Fossil Island General Store"(228, ShopCurrency.COINS, STOCK_ONLY) {
            POT(5, 0, 1)
            JUG(2, 0, 1)
            BUCKET(3, 0, 2)
            BOWL(2, 1, 5)
            CAKE_TIN(2, 4, 13)
            TINDERBOX(2, 1, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            ROPE(5, 7, 23)
        }
    }
}
