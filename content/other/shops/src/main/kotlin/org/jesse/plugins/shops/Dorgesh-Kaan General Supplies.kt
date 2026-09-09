package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class DorgeshKaanGeneralSupplies : ShopScript() {

    init {
        "Dorgesh-Kaan General Supplies"(144, ShopCurrency.COINS, STOCK_ONLY) {
            UNLIT_TORCH(5, -1, 5)
            TINDERBOX(2, -1, 1)
            POT(5, -1, 1)
            JUG(2, -1, 1)
            EMPTY_JUG_PACK(6, -1, 182)
            BOWL(2, -1, 5)
            CAKE_TIN(2, -1, 13)
            ROPE(3, -1, 23)
            GLASSBLOWING_PIPE(1, -1, 2)
        }
    }
}
