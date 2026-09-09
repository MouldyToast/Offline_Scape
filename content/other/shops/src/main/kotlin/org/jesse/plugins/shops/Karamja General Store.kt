package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class KaramjaGeneralStore : ShopScript() {

    init {
        "Karamja General Store"(104, ShopCurrency.COINS, CAN_SELL, 0.6, ShopDiscount.KARAMJA_DIARY) {
            POT(30, 1, 1)
            JUG(10, 1, 1)
            EMPTY_JUG_PACK(5, -1, 182)
            SHEARS(10, 1, 1)
            BUCKET(30, 1, 2)
            BOWL(10, 1, 4)
            CAKE_TIN(10, 3, 10)
            TINDERBOX(10, 1, 1)
            CHISEL(10, 1, 14)
            HAMMER(10, 1, 13)
        }
    }
}
